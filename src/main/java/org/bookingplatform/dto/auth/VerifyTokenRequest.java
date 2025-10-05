package org.bookingplatform.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VerifyTokenRequest {
    @NotNull
    private String token;
}
