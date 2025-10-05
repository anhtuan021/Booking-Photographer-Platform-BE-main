package org.bookingplatform.dto.photographer;

import lombok.Data;
import lombok.Builder;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class PhotographerStatisticsResponse {
    
    private BigInteger photographerId;
    private String photographerName;
    private String businessName;
    
    // Booking Statistics
    private Integer totalBookings;
    private Integer completedBookings;
    private Integer cancelledBookings;
    private Integer pendingBookings;
    private Integer confirmedBookings;
    private Integer inProgressBookings;
    
    // Revenue Statistics
    private BigDecimal totalRevenue;
    private BigDecimal monthlyRevenue;
    private BigDecimal weeklyRevenue;
    private BigDecimal dailyRevenue;
    private BigDecimal averageBookingValue;
    
    // Review Statistics
    private Double averageRating;
    private Integer totalReviews;
    private Map<Integer, Integer> ratingDistribution; // rating -> count
    private Integer fiveStarReviews;
    private Integer fourStarReviews;
    private Integer threeStarReviews;
    private Integer twoStarReviews;
    private Integer oneStarReviews;
    
    // Portfolio Statistics
    private Integer totalPortfolioImages;
    private Integer activePortfolioImages;
    private Integer pendingPortfolioImages;
    private Integer featuredImages;
    
    // Service Package Statistics
    private Integer totalServicePackages;
    private Integer activeServicePackages;
    private Integer inactiveServicePackages;
    
    // Availability Statistics
    private Integer totalBlockedDays;
    private Integer upcomingBlockedDays;
    
    // Time-based Statistics
    private List<DailyBookingStats> dailyBookingStats;
    private List<MonthlyRevenueStats> monthlyRevenueStats;
    private List<CategoryStats> categoryStats;
    
    @Data
    @Builder
    public static class DailyBookingStats {
        private LocalDateTime date;
        private Integer bookings;
        private BigDecimal revenue;
    }
    
    @Data
    @Builder
    public static class MonthlyRevenueStats {
        private String month;
        private Integer year;
        private BigDecimal revenue;
        private Integer bookings;
    }
    
    @Data
    @Builder
    public static class CategoryStats {
        private String category;
        private Integer imageCount;
        private Integer viewCount;
        private Double averageRating;
    }
}
