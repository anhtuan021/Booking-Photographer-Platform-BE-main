package org.bookingplatform.dto.booking;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class UpdateBookingStatusRequest {
    
    @NotBlank(message = "Status is required")
    private String status;
    
    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
    
    @Size(max = 1000, message = "Cancellation reason must not exceed 1000 characters")
    private String cancellationReason;
}