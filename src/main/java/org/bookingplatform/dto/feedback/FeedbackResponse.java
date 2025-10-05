package org.bookingplatform.dto.feedback;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponse {
    
    private BigInteger id;
    private BigInteger bookingId;
    private BigInteger customerId;
    private BigInteger photographerId;
    private Integer rating;
    private String comment;
    private Boolean isApproved;
    private BigInteger approvedBy;
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Additional fields for response
    private String customerName;
    private String customerEmail;
    private String photographerName;
    private String photographerEmail;
    private String bookingCode;
}
