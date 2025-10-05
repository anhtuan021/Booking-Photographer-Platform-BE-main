package org.bookingplatform.service;

import org.bookingplatform.dto.photographer.*;

import java.time.LocalDate;
import java.util.List;

public interface PhotographerService {
    // Availability Management
    void blockAvailability(String token, BlockAvailabilityRequest request);
    void unblockAvailability(String token, LocalDate date, String time);
    List<BlockedDateResponse> getMyBlockedDates(String token, LocalDate startDate, LocalDate endDate);
}
