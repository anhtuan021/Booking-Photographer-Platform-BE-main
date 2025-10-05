package org.bookingplatform.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedbacks", indexes = {
    @Index(name = "idx_feedbacks_photographer", columnList = "photographer_id"),
    @Index(name = "idx_feedbacks_customer", columnList = "customer_id"),
    @Index(name = "idx_feedbacks_booking", columnList = "booking_id")
})
@Data
@EqualsAndHashCode(callSuper = true)
public class Feedback extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photographer_id", nullable = false)
    private UserProfile photographer;
    
    @Column(nullable = false)
    private Integer rating; // 1-5 stars
    
    @Column(columnDefinition = "TEXT")
    private String comment;
    
    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved = true;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;
    
    private LocalDateTime approvedAt;
}
