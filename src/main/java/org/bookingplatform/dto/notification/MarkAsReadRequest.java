package org.bookingplatform.dto.notification;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class MarkAsReadRequest {
    
    @NotBlank(message = "Notification ID is required")
    private String notificationId;
    
    // Optional: mark multiple notifications as read
    private List<String> notificationIds;
}
