package org.bookingplatform.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bookingplatform.constant.Role;

@Data
public class SocialLoginRequest {
    
    @NotNull(message = "Provider is required")
    private String provider; // google, facebook
    
    @NotNull(message = "Access token is required")
    private String accessToken;
    
    @NotNull(message = "User type is required")
    private Role userType;
}
