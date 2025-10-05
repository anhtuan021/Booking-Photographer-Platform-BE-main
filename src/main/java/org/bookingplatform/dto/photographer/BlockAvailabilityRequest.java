package org.bookingplatform.dto.photographer;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class BlockAvailabilityRequest {
    
    @NotNull(message = "Blocked date is required")
    private LocalDate blockedDate;
    
    private String blockedTime; // HH:mm format
    
    @Size(max = 200, message = "Reason must not exceed 200 characters")
    private String reason;
    
    private Boolean isRecurring;
    
    private String recurringPattern; // weekly, monthly, etc.
}