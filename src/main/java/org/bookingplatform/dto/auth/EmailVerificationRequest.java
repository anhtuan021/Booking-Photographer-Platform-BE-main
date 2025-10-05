package org.bookingplatform.dto.auth;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class EmailVerificationRequest {
    
    @NotBlank(message = "Verification code is required")
    @Size(min = 4, max = 6, message = "Verification code must be 4-6 characters")
    private String verificationCode;
}
