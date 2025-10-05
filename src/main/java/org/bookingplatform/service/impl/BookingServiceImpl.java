package org.bookingplatform.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookingplatform.constant.BookingStatus;
import org.bookingplatform.constant.PaymentMethod;
import org.bookingplatform.constant.PaymentStatus;
import org.bookingplatform.constant.ProfileStatus;
import org.bookingplatform.dto.booking.*;
import org.bookingplatform.entity.Booking;
import org.bookingplatform.entity.UserProfile;
import org.bookingplatform.entity.ServicePackage;
import org.bookingplatform.entity.User;
import org.bookingplatform.exception.NotFoundException;
import org.bookingplatform.exception.UnauthorizedException;
import org.bookingplatform.repository.*;
import org.bookingplatform.service.BookingService;
import org.bookingplatform.service.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final ServicePackageRepository servicePackageRepository;
    private final JwtService jwtService;

    @Override
    public BookingResponse createBooking(String token, CreateBookingRequest request) {
        // Step 1: Validate token and get customer
        String customerId = jwtService.extractUserId(token);
        BigInteger customerIdBigInt = new BigInteger(customerId);

        User customer = userRepository.findById(customerIdBigInt)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Step 2: Validate photographer
        UserProfile photographer = userProfileRepository.findById(new BigInteger(String.valueOf(request.getPhotographerId())))
                .orElseThrow(() -> new RuntimeException("Photographer not found"));

        // check if photographer is active
        if (photographer.getStatus().equals(ProfileStatus.INACTIVE)) {
            throw new RuntimeException("Photographer is not active");
        }

        // Step 3: Validate service packages
        List<ServicePackage> servicePackages =
                servicePackageRepository.findActiveByUserIdAndSpecialityAndCodes(
                        request.getSpeciality(),
                        request.getServices()
                );

        LocalDate date = LocalDate.parse(request.getDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        if (bookingRepository.countBookingsByPhotographerAndDate(photographer.getId(), date) > 5) {
            throw new RuntimeException("Photographer is not available on the requested date");
        }

        String[] timeParts = request.getTimeSlot().split("-");
        if (timeParts.length != 2) {
            throw new RuntimeException("Invalid time slot format. Expected: 'HH:mm - HH:mm'");
        }
        LocalTime startTime = LocalTime.parse(timeParts[0].trim(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime endTime = LocalTime.parse(timeParts[1].trim(), DateTimeFormatter.ofPattern("HH:mm"));

        String bookingCode = generateBookingCode();

        Booking booking = new Booking();
        booking.setBookingCode(bookingCode);
        booking.setCustomerPhone(request.getPhone());
        booking.setSpecialities(request.getSpeciality());
        booking.setCustomer(customer);
        booking.setPhotographer(photographer);
        booking.setSpeciality(request.getSpeciality());
        booking.setDate(date);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setNote(request.getNote());
        booking.setStatus(BookingStatus.IN_PROGRESS);
        booking.setPaymentMethod(PaymentMethod.QR.name());
        booking.setTotalAmount(request.getPrice());
        booking.setTotalDiscount(request.getTotalDiscount());
        booking.setTotalPayment(request.getTotalPayment());
        booking.setPaymentStatus(PaymentStatus.PAID);
        booking.setFirstName(request.getFirstName());
        booking.setLastName(request.getLastName());
        booking.setPhone(request.getPhone());
        booking.setEmail(request.getEmail());
        booking.setReasonReject(null);

        // Logic commission rate and amounts
        BigDecimal totalPayment = booking.getTotalPayment();
        BigDecimal commissionRate;

        if (totalPayment.compareTo(new BigDecimal("1000000")) < 0) {
            commissionRate = new BigDecimal("0.30"); // 30%
        } else if (totalPayment.compareTo(new BigDecimal("5000000")) < 0) {
            commissionRate = new BigDecimal("0.20"); // 20%
        } else {
            commissionRate = new BigDecimal("0.15"); // 15%
        }

        BigDecimal adminAmount = totalPayment.multiply(commissionRate);
        BigDecimal photographerAmount = totalPayment.subtract(adminAmount);

        // Save follow %
        booking.setCommissionRate(commissionRate.multiply(BigDecimal.valueOf(100)));
        booking.setAdminAmount(adminAmount);
        booking.setPhotographerAmount(photographerAmount);

        // Save booking_services
        for (ServicePackage sp : servicePackages) {
            org.bookingplatform.entity.BookingService bs = new org.bookingplatform.entity.BookingService();
            bs.setBooking(booking);
            bs.setServicePackage(sp);
            bs.setPrice(sp.getBasePrice());
            booking.getBookingServices().add(bs);
        }

        Booking savedBooking = bookingRepository.save(booking);

        return mapToBookingResponse(savedBooking);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getMyBookings(String token) {
        String userId = jwtService.extractUserId(token);
        BigInteger userIdBigInt = new BigInteger(userId);

        List<Booking> bookings;
        bookings = bookingRepository.findByCustomerIdOrderByCreatedAtDesc(userIdBigInt);

        return bookings.stream()
                .map(this::mapToBookingResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBookingById(String bookingId) {

        BigInteger bookingIdBigInt = new BigInteger(bookingId);

        Booking booking = bookingRepository.findById(bookingIdBigInt)
            .orElseThrow(() -> new RuntimeException("Booking not found"));

        return mapToBookingResponse(booking);
    }

    // =============================================
    // PHOTOGRAPHER BOOKING MANAGEMENT
    // =============================================

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getAllPhotographerBookings(String token) {
        String userId = jwtService.extractUserId(token);
        BigInteger userIdBigInt = new BigInteger(userId);

        // Find photographer profile
        UserProfile profile = userProfileRepository.findByUser_Id(userIdBigInt)
                .orElseThrow(() -> new RuntimeException("Photographer profile not found"));

        // Get all bookings for this photographer
        List<Booking> bookings = bookingRepository.findByPhotographer_IdOrderByCreatedAtDesc(profile.getId());

        return bookings.stream()
                .map(this::mapToBookingResponse)
                .collect(Collectors.toList());
    }

    // Method that getAll bookings - for admin use only
    @Override
    public List<BookingResponse> getAll() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream()
                .map(this::mapToBookingResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void approveBooking(String token, BigInteger bookingId) {
        String userId = jwtService.extractUserId(token);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getPhotographer().getUser().getId().toString().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to reject this booking");
        }

        booking.setStatus(BookingStatus.COMPLETED);

        bookingRepository.save(booking);
    }

    @Override
    public void rejectBooking(String token, BigInteger bookingId, String reason) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        booking.setStatus(BookingStatus.REJECTED);
        booking.setReasonReject(reason);
        bookingRepository.save(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public Object getPhotographerBookingStats(String token) {
        String userId = jwtService.extractUserId(token);
        BigInteger userIdBigInt = new BigInteger(userId);
        UserProfile photographer = userProfileRepository.findByUser_Id(userIdBigInt)
            .orElseThrow(() -> new RuntimeException("Photographer profile not found"));
        long total = bookingRepository.findByPhotographerIdOrderByCreatedAtDesc(photographer.getId(), org.springframework.data.domain.PageRequest.of(0, 1)).getTotalElements();
        long completed = bookingRepository.findByPhotographerIdAndStatusOrderByCreatedAtDesc(photographer.getId(), BookingStatus.COMPLETED, org.springframework.data.domain.PageRequest.of(0, 1)).getTotalElements();
        return java.util.Map.of("total", total, "completed", completed);
    }

    private String generateBookingCode() {
        String prefix = "BK" + LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        String bookingCode;
        int counter = 1;
        
        do {
            bookingCode = prefix + String.format("%04d", counter);
            counter++;
        } while (bookingRepository.existsByBookingCode(bookingCode));
        
        return bookingCode;
    }

    private BookingResponse mapToBookingResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .bookingCode(booking.getBookingCode())
                .customerId(booking.getCustomer().getId())
                .speciality(booking.getSpeciality())
                .photographerId(booking.getPhotographer().getId())
                .servicePackages(
                        booking.getBookingServices().stream()
                                .map(bs -> {
                                    ServicePackage sp = bs.getServicePackage();
                                    return BookingResponse.ServicePackageInfo.builder()
                                            .id(sp.getId())
                                            .name(sp.getName())
                                            .description(sp.getDescription())
                                            .price(sp.getBasePrice())
                                            .build();
                                }).toList()
                )
                .date(booking.getDate())
                .reasonReject(booking.getReasonReject())
                .note(booking.getNote())
                .totalDiscount(booking.getTotalDiscount())
                .totalPayment(booking.getTotalPayment())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .status(booking.getStatus().name())
                .price(booking.getTotalAmount())
                .paymentStatus(booking.getPaymentStatus().name())
                .paymentMethod(booking.getPaymentMethod())
                .completedAt(booking.getCompletedAt())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .customerName(booking.getFirstName() + " " + booking.getLastName())
                .customerEmail(booking.getEmail())
                .customerPhone(booking.getPhone())
                .photographerName(booking.getPhotographer().getUser().getFirstName() + " " + booking.getPhotographer().getUser().getLastName())
                .photographerEmail(booking.getPhotographer().getUser().getEmail())
                .photographerPhone(booking.getPhotographer().getUser().getPhone())

                .commissionRate(booking.getCommissionRate())
                .adminAmount(booking.getAdminAmount())
                .photographerAmount(booking.getPhotographerAmount())
                .build();
    }
}
