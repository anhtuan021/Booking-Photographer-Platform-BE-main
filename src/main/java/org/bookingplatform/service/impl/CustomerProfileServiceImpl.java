package org.bookingplatform.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.bookingplatform.constant.Gender;
import org.bookingplatform.constant.Role;
import org.bookingplatform.dto.customer.CreateCustomerProfileRequest;
import org.bookingplatform.dto.customer.CustomerProfileResponse;
import org.bookingplatform.entity.CustomerProfile;
import org.bookingplatform.entity.User;
import org.bookingplatform.repository.CustomerProfileRepository;
import org.bookingplatform.service.CustomerProfileService;
import org.bookingplatform.service.JwtService;
import org.bookingplatform.service.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerProfileServiceImpl implements CustomerProfileService {
    private final CustomerProfileRepository customerProfileRepository;
    private final JwtService jwtService;
    private final S3Service s3Service;

    @Override
    @Transactional
    public CustomerProfileResponse updateCustomerProfile(
            String token,
            CreateCustomerProfileRequest request,
            MultipartFile avatarFile
    ) {
        String userId = jwtService.extractUserId(token);
        BigInteger userIdBigInt = new BigInteger(userId);

        CustomerProfile profile = customerProfileRepository.findByUserIdWithUser(userIdBigInt)
                .orElseThrow(() -> new RuntimeException("Customer profile not found"));

        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                String avatarUrl = s3Service.uploadFile(avatarFile);

                profile.setAvatarUrl(avatarUrl);

            } catch (IOException e) {
                throw new RuntimeException("Failed to upload avatar", e);
            }
        }

        if (request.getAddress() != null) profile.setAddress(request.getAddress());
        if (request.getCity() != null) profile.setCity(request.getCity());
        if (request.getWard() != null) profile.setWard(request.getWard());
        if (request.getGender() != null) {
            try {
                profile.setGender(Gender.valueOf(request.getGender().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid gender value: " + request.getGender());
            }
        }

        if (request.getDateOfBirth() != null && !request.getDateOfBirth().isEmpty()) {
            profile.setDateOfBirth(parseDateOfBirth(request.getDateOfBirth()));
        }

        // Update user info
        User user = profile.getUser();
        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
       // if (request.getEmail() != null) user.setEmail(request.getEmail());

        CustomerProfile savedProfile = customerProfileRepository.save(profile);

        return mapToResponse(savedProfile);
    }

    private LocalDate parseDateOfBirth(String dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isEmpty()) {
            return null;
        }
        DateTimeParseException lastException = null;
        for (String pattern : new String[]{"dd/MM/yyyy", "MM/dd/yyyy"}) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                return LocalDate.parse(dateOfBirth, formatter);
            } catch (DateTimeParseException e) {
                lastException = e;
            }
        }
        throw new RuntimeException("Invalid dateOfBirth format. Expected dd/MM/yyyy or MM/dd/yyyy", lastException);
    }

    @Override
    @Transactional
    public CustomerProfileResponse getProfile(String token) {
        String userId = jwtService.extractUserId(token);
        BigInteger userIdBigInt = new BigInteger(userId);

        CustomerProfile profile = customerProfileRepository.findByUser_Id(userIdBigInt)
                .orElseThrow(() -> new RuntimeException("Customer profile not found"));

        return mapToResponse(profile);
    }

    @Override
    public List<CustomerProfileResponse> searchCustomers() {
        List<CustomerProfile> profiles = customerProfileRepository.findAllWithUser();
        return profiles.stream()
                .filter(profile -> profile.getUser().getRole() == Role.CUSTOMER)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private CustomerProfileResponse mapToResponse(CustomerProfile profile) {
        User user = profile.getUser();
        return CustomerProfileResponse.builder()
                .id(profile.getId())
                .userId(user.getId())
                .dateOfBirth(profile.getDateOfBirth() != null
                        ? profile.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                        : null)
                .gender(profile.getGender())
                .avatarUrl(profile.getAvatarUrl())
                .address(profile.getAddress())
                .city(profile.getCity())
                .ward(profile.getWard())
                .phoneVerified(profile.getPhoneVerified())
                .emailVerified(profile.getEmailVerified())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                //.email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }
}