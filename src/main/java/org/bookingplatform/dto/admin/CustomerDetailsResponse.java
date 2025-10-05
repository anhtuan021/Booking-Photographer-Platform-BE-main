package org.bookingplatform.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object containing detailed customer information")
public class CustomerDetailsResponse {
    
    @Schema(description = "Customer ID", example = "1")
    private BigInteger id;
    
    @Schema(description = "First name", example = "Charlene")
    private String firstName;
    
    @Schema(description = "Last name", example = "Reed")
    private String lastName;
    
    @Schema(description = "Email address", example = "charlene.reed@example.com")
    private String email;
    
    @Schema(description = "Phone number", example = "8286329170")
    private String phone;
    
    @Schema(description = "Avatar URL", example = "https://example.com/avatar.jpg")
    private String avatarUrl;
    
    @Schema(description = "Status", example = "ACTIVE")
    private String status;
    
    @Schema(description = "Registration date", example = "2023-01-15T10:00:00")
    private LocalDateTime registrationDate;
    
    @Schema(description = "Last login date", example = "2023-10-20T15:30:00")
    private LocalDateTime lastLoginDate;
    
    @Schema(description = "Total bookings count", example = "5")
    private Integer totalBookings;
    
    @Schema(description = "Completed bookings count", example = "4")
    private Integer completedBookings;
    
    @Schema(description = "Cancelled bookings count", example = "1")
    private Integer cancelledBookings;
    
    @Schema(description = "Total amount spent", example = "500.00")
    private Double totalAmountSpent;
    
    @Schema(description = "Average booking value", example = "100.00")
    private Double averageBookingValue;
    
    @Schema(description = "Total reviews written", example = "3")
    private Integer totalReviews;
    
    @Schema(description = "Average rating given", example = "4.2")
    private Double averageRatingGiven;
    
    @Schema(description = "Recent bookings")
    private List<BookingSummary> recentBookings;
    
    @Schema(description = "Recent reviews")
    private List<ReviewSummary> recentReviews;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Booking summary information")
    public static class BookingSummary {
        @Schema(description = "Booking ID", example = "1")
        private BigInteger id;
        
        @Schema(description = "Photographer name", example = "Dr. Ruby Perrin")
        private String photographerName;
        
        @Schema(description = "Service package name", example = "Wedding Photography")
        private String servicePackageName;
        
        @Schema(description = "Booking date", example = "2023-10-20T11:00:00")
        private LocalDateTime bookingDate;
        
        @Schema(description = "Status", example = "COMPLETED")
        private String status;
        
        @Schema(description = "Amount", example = "200.00")
        private Double amount;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Review summary information")
    public static class ReviewSummary {
        @Schema(description = "Review ID", example = "1")
        private BigInteger id;
        
        @Schema(description = "Photographer name", example = "Dr. Ruby Perrin")
        private String photographerName;
        
        @Schema(description = "Rating", example = "5")
        private Integer rating;
        
        @Schema(description = "Comment", example = "Excellent service!")
        private String comment;
        
        @Schema(description = "Review date", example = "2023-10-21T10:00:00")
        private LocalDateTime reviewDate;
        
        @Schema(description = "Status", example = "APPROVED")
        private String status;
    }
}
