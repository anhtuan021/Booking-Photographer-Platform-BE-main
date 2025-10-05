package org.bookingplatform.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bookingplatform.constant.NotificationType;
import org.bookingplatform.constant.SentVia;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications", indexes = {
    @Index(name = "idx_notifications_user", columnList = "user_id"),
    @Index(name = "idx_notifications_type", columnList = "type"),
    @Index(name = "idx_notifications_read", columnList = "is_read"),
    @Index(name = "idx_notifications_created", columnList = "created_at")
})
@Data
@EqualsAndHashCode(callSuper = true)
public class Notification extends BaseEntity {
    
    @Column(nullable = false)
    private BigInteger userId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;
    
    @Column(columnDefinition = "JSON")
    private String data; // JSON data
    
    private Boolean isRead = false;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SentVia sentVia = SentVia.IN_APP;
    
    private LocalDateTime sentAt;
}
