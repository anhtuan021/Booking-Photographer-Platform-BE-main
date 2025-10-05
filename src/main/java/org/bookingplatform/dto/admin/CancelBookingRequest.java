package org.bookingplatform.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@Schema(description = "Request object for cancelling a booking")
public class CancelBookingRequest {
    
    @Schema(description = "Cancellation reason", example = "Customer requested cancellation", required = true)
    @NotBlank(message = "Cancellation reason is required")
    @Size(max = 500, message = "Cancellation reason must not exceed 500 characters")
    private String cancellationReason;
}
