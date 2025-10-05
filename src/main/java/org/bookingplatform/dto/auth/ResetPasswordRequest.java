package org.bookingplatform.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request object for resetting password")
public class ResetPasswordRequest {
    
    @Schema(description = "Reset token received via email", example = "abc123def456", required = true)
    @NotBlank(message = "Reset token is required")
    private String resetToken;
    
    @Schema(description = "New password", example = "NewPass123!", required = true)
    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "New password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]", 
             message = "New password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character")
    private String newPassword;
    
    @Schema(description = "Confirm new password", example = "NewPass123!", required = true)
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}
