package org.bookingplatform.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@Schema(description = "Request object for suspending a customer")
public class SuspendCustomerRequest {
    
    @Schema(description = "Suspension reason", example = "Violation of platform terms", required = true)
    @NotBlank(message = "Suspension reason is required")
    @Size(max = 500, message = "Suspension reason must not exceed 500 characters")
    private String suspensionReason;
    
    @Schema(description = "Suspension duration in days", example = "30")
    private Integer suspensionDurationDays;
}
