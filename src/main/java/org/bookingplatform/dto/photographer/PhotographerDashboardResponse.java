package org.bookingplatform.dto.photographer;

import lombok.Data;
import lombok.Builder;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PhotographerDashboardResponse {
    
    private BigInteger photographerId;
    private String photographerName;
    private String businessName;
    private String status;
    private Double averageRating;
    private Integer totalReviews;
    private Integer totalBookings;
    private Integer activeBookings;
    private Integer completedBookings;
    private Integer cancelledBookings;
    private BigDecimal totalRevenue;
    private BigDecimal monthlyRevenue;
    private Integer portfolioImages;
    private Integer servicePackages;
    private Integer pendingBookings;
    private Integer todayBookings;
    private List<BookingSummary> recentBookings;
    private List<ReviewSummary> recentReviews;
    private List<NotificationSummary> recentNotifications;
    
    @Data
    @Builder
    public static class BookingSummary {
        private BigInteger bookingId;
        private String bookingCode;
        private String customerName;
        private String servicePackageName;
        private String status;
        private LocalDateTime bookingDate;
        private BigDecimal totalAmount;
    }
    
    @Data
    @Builder
    public static class ReviewSummary {
        private BigInteger reviewId;
        private String customerName;
        private Integer rating;
        private String comment;
        private LocalDateTime createdAt;
    }
    
    @Data
    @Builder
    public static class NotificationSummary {
        private BigInteger notificationId;
        private String title;
        private String message;
        private String type;
        private Boolean isRead;
        private LocalDateTime createdAt;
    }
}
