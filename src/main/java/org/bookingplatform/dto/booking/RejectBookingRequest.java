package org.bookingplatform.dto.booking;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RejectBookingRequest {
    @NotBlank(message = "Reason reject cannot be null")
    private String reasonReject;
}