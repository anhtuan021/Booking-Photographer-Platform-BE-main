package org.bookingplatform.service.impl;

import lombok.RequiredArgsConstructor;
import org.bookingplatform.constant.BookingStatus;
import org.bookingplatform.dto.feedback.CreateFeedbackRequest;
import org.bookingplatform.dto.feedback.FeedbackResponse;
import org.bookingplatform.entity.*;
import org.bookingplatform.exception.ResourceNotFoundException;
import org.bookingplatform.repository.*;
import org.bookingplatform.service.FeedbackService;
import org.bookingplatform.service.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final JwtService jwtService;


    // Create feedback for a booking
    @Override
    @Transactional
    public FeedbackResponse createFeedback(String token, CreateFeedbackRequest request) {
        // Get customer from token
        String userId = jwtService.extractUserId(token);
        BigInteger userIdBigInt = new BigInteger(userId);
        User customer = userRepository.findById(userIdBigInt)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Get booking
        Booking booking = bookingRepository.findById(new BigInteger(request.getBookingId()))
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        // Check if the booking is rejected, then cannot give feedback
        if (booking.getStatus().toString().equals(BookingStatus.REJECTED.toString())) {
            throw new IllegalArgumentException("You cannot give feedback for rejected bookings");
        }
        
        // Create feedback
        Feedback feedback = new Feedback();
        feedback.setBooking(booking);
        feedback.setCustomer(customer);
        feedback.setPhotographer(booking.getPhotographer());
        feedback.setRating(request.getRating());
        feedback.setComment(request.getComment());
        feedback.setIsApproved(true);
        
        Feedback savedFeedback = feedbackRepository.save(feedback);
        
        return convertToResponse(savedFeedback);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FeedbackResponse> getMyFeedbacks(String token) {
        String userId = jwtService.extractUserId(token);
        BigInteger userIdBigInt = new BigInteger(userId);

        User customer = userRepository.findById(userIdBigInt)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Feedback> feedbacks = feedbackRepository.findByCustomerIdWithDetails(customer.getId());

        return feedbacks.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public FeedbackResponse getFeedbackById(String feedbackId) {
        Feedback feedback = feedbackRepository.findById(new BigInteger(feedbackId))
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found"));

        return convertToResponse(feedback);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackResponse> getPhotographerFeedbacks(String token) {
        String userId = jwtService.extractUserId(token);
        BigInteger userIdBigInt = new BigInteger(userId);

        UserProfile photographer = userProfileRepository.findByUser_Id(userIdBigInt)
                .orElseThrow(() -> new ResourceNotFoundException("Photographer profile not found"));

        List<Feedback> feedbacks = feedbackRepository.findByPhotographerIdAndIsApprovedWithDetails(
                photographer.getId(), true);

        return feedbacks.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackResponse> getPhotographerFeedbacksById(String photographerId) {
        List<Feedback> feedbacks = feedbackRepository.findByPhotographerIdAndIsApprovedWithDetails(
                new BigInteger(photographerId), true);

        return feedbacks.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void rejectFeedback(String token, String feedbackId) {
        String userId = jwtService.extractUserId(token);
        BigInteger userIdBigInt = new BigInteger(userId);

        User user = userRepository.findById(userIdBigInt)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getRole().toString().equals("ADMIN")) {
            throw new IllegalArgumentException("Only admins can reject feedbacks");
        }
        
        Feedback feedback = feedbackRepository.findById(new BigInteger(feedbackId))
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found"));
        
        feedbackRepository.delete(feedback);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FeedbackResponse> getAllFeedbacks() {
        List<Feedback> feedbacks = feedbackRepository.findAllWithDetails();
        
        return feedbacks.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    private FeedbackResponse convertToResponse(Feedback feedback) {
        return FeedbackResponse.builder()
                .id(feedback.getId())
                .bookingId(feedback.getBooking().getId())
                .customerId(feedback.getCustomer().getId())
                .photographerId(feedback.getPhotographer().getId())
                .rating(feedback.getRating())
                .comment(feedback.getComment())
                .isApproved(feedback.getIsApproved())
                .approvedBy(feedback.getApprovedBy() != null ? feedback.getApprovedBy().getId() : null)
                .approvedAt(feedback.getApprovedAt())
                .createdAt(feedback.getCreatedAt()
                        .atZone(ZoneOffset.UTC)
                        .withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh"))
                        .toLocalDateTime())
                .updatedAt(feedback.getUpdatedAt()
                        .atZone(ZoneOffset.UTC)
                        .withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh"))
                        .toLocalDateTime())
                .customerName(feedback.getBooking().getFirstName() + " " + feedback.getBooking().getLastName())
                .customerEmail(feedback.getBooking().getEmail())
                .photographerName(feedback.getBooking().getPhotographer().getBusinessName())
                .photographerEmail(feedback.getBooking().getPhotographer().getUser().getEmail())
                .bookingCode(feedback.getBooking().getBookingCode())
                .build();
    }
}
