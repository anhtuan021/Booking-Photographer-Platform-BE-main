package org.bookingplatform.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookingplatform.dto.photographer.*;
import org.bookingplatform.entity.*;
import org.bookingplatform.repository.*;
import org.bookingplatform.service.JwtService;
import org.bookingplatform.service.PhotographerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PhotographerServiceImpl implements PhotographerService {
    private final UserProfileRepository userProfileRepository;
    private final PhotographerAvailabilityRepository availabilityRepository;
    private final JwtService jwtService;

    // =============================================
    // AVAILABILITY MANAGEMENT
    // =============================================
    
    @Override
    public void blockAvailability(String token, BlockAvailabilityRequest request) {
        String userId = jwtService.extractUserId(token);
        BigInteger userIdBigInt = new BigInteger(userId);
        
        UserProfile profile = userProfileRepository.findByUser_Id(userIdBigInt)
            .orElseThrow(() -> new RuntimeException("Photographer profile not found"));
        
        // Create availability block
        PhotographerAvailability availability = new PhotographerAvailability();
        availability.setPhotographer(profile);
        availability.setBlockedDate(request.getBlockedDate());
        availability.setBlockedTime(request.getBlockedTime() != null ? LocalTime.parse(request.getBlockedTime()) : null);
        availability.setReason(request.getReason());
        availability.setIsRecurring(request.getIsRecurring() != null ? request.getIsRecurring() : false);
        availability.setRecurringPattern(request.getRecurringPattern());
        
        availabilityRepository.save(availability);
    }
    
    @Override
    public void unblockAvailability(String token, LocalDate date, String time) {
        String userId = jwtService.extractUserId(token);
        BigInteger userIdBigInt = new BigInteger(userId);
        
        UserProfile profile = userProfileRepository.findByUser_Id(userIdBigInt)
            .orElseThrow(() -> new RuntimeException("Photographer profile not found"));

        availabilityRepository.deleteByPhotographerIdAndBlockedDate(profile.getId(), date);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BlockedDateResponse> getMyBlockedDates(String token, LocalDate startDate, LocalDate endDate) {
        String userId = jwtService.extractUserId(token);
        BigInteger userIdBigInt = new BigInteger(userId);
        
        UserProfile profile = userProfileRepository.findByUser_Id(userIdBigInt)
            .orElseThrow(() -> new RuntimeException("Photographer profile not found"));
        
        List<PhotographerAvailability> availabilities;
        if (startDate != null && endDate != null) {
            availabilities = availabilityRepository.findByPhotographerIdAndDateRange(profile.getId(), startDate, endDate);
        } else {
            availabilities = availabilityRepository.findByPhotographerIdOrderByBlockedDateAsc(profile.getId());
        }

        return availabilities.stream()
                .map(a -> new BlockedDateResponse(a.getBlockedDate(), a.getReason()))
                .collect(Collectors.toList());
    }
}