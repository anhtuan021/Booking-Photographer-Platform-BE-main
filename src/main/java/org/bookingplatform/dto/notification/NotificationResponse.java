package org.bookingplatform.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private BigInteger id;
    private BigInteger userId;
    private String type;
    private String title;
    private String message;
    private Map<String, Object> data;
    private Boolean isRead;
    private String sentVia;
    private LocalDateTime sentAt;
    private LocalDateTime createdAt;
    private BigInteger senderId;
    private String senderName;
}