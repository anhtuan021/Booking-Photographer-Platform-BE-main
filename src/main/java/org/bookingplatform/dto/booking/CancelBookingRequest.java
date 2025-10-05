package org.bookingplatform.dto.booking;

import lombok.Data;
import jakarta.validation.constraints.Size;

@Data
public class CancelBookingRequest {
    
    @Size(max = 1000, message = "Cancellation reason must not exceed 1000 characters")
    private String cancellationReason;
}