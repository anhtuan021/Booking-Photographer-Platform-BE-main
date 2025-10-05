package org.bookingplatform.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "photographer_availability", indexes = {
    @Index(name = "idx_availability_photographer", columnList = "photographer_id"),
    @Index(name = "idx_availability_date", columnList = "blocked_date")
})
@Data
@EqualsAndHashCode(callSuper = true)
public class PhotographerAvailability extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photographer_id", nullable = false)
    private UserProfile photographer;
    
    @Column(nullable = false)
    private LocalDate blockedDate;
    
    private LocalTime blockedTime;
    
    @Column(length = 200)
    private String reason;
    
    @Column(nullable = false)
    private Boolean isRecurring = false;
    
    @Column(length = 50)
    private String recurringPattern;
}
