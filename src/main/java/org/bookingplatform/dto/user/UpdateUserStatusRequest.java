package org.bookingplatform.dto.user;

import lombok.Data;

@Data
public class UpdateUserStatusRequest {
    private String status; // e.g., "ACTIVE", "BANNED", "APPROVED"
}