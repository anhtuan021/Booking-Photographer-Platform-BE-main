package org.bookingplatform.dto.review;

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
public class ReviewResponse {
    
    private BigInteger id;
    private BigInteger bookingId;
    private BigInteger customerId;
    private BigInteger photographerId;
    private Integer rating;
    private String comment;
    private String status;
    private BigInteger reviewedBy;
    private LocalDateTime reviewedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Additional fields for response
    private String customerName;
    private String customerEmail;
    private String photographerName;
    private String photographerEmail;
    private String bookingCode;
    private String servicePackageName;
}