package org.bookingplatform.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class AuthResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String accessToken;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String refreshToken;
    private String tokenType = "Bearer";
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long expiresIn; // seconds
    private UserInfo user;

    @Data
    public static class UserInfo {
        private BigInteger id;
        private String email;
        private String firstName;
        private String lastName;
        private String role;
        private String status;
        private Boolean emailVerified;
        private Boolean phoneVerified;
        private LocalDateTime lastLoginAt;
    }
}
