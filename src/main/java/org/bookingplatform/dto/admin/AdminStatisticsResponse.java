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
@Schema(description = "Response object containing detailed admin statistics")
public class AdminStatisticsResponse {
    
    @Schema(description = "Total photographers count", example = "168")
    private Integer totalPhotographers;
    
    @Schema(description = "Total customers count", example = "487")
    private Integer totalCustomers;
    
    @Schema(description = "Total bookings count", example = "485")
    private Integer totalBookings;
    
    @Schema(description = "Total revenue", example = "62523.50")
    private Double totalRevenue;
    
    @Schema(description = "Active photographers count", example = "145")
    private Integer activePhotographers;
    
    @Schema(description = "Pending photographers count", example = "15")
    private Integer pendingPhotographers;
    
    @Schema(description = "Suspended photographers count", example = "8")
    private Integer suspendedPhotographers;
    
    @Schema(description = "Active customers count", example = "420")
    private Integer activeCustomers;
    
    @Schema(description = "Suspended customers count", example = "67")
    private Integer suspendedCustomers;
    
    @Schema(description = "Completed bookings count", example = "350")
    private Integer completedBookings;
    
    @Schema(description = "Pending bookings count", example = "85")
    private Integer pendingBookings;
    
    @Schema(description = "Cancelled bookings count", example = "50")
    private Integer cancelledBookings;
    
    @Schema(description = "Average booking value", example = "128.91")
    private Double averageBookingValue;
    
    @Schema(description = "Average photographer rating", example = "4.3")
    private Double averagePhotographerRating;
    
    @Schema(description = "Total reviews count", example = "1200")
    private Integer totalReviews;
    
    @Schema(description = "Approved reviews count", example = "1100")
    private Integer approvedReviews;
    
    @Schema(description = "Pending reviews count", example = "100")
    private Integer pendingReviews;
    
    @Schema(description = "Monthly statistics")
    private List<MonthlyStats> monthlyStats;
    
    @Schema(description = "Top photographers by revenue")
    private List<TopPhotographer> topPhotographersByRevenue;
    
    @Schema(description = "Top photographers by bookings")
    private List<TopPhotographer> topPhotographersByBookings;
    
    @Schema(description = "Top photographers by rating")
    private List<TopPhotographer> topPhotographersByRating;
    
    @Schema(description = "Recent activities")
    private List<RecentActivity> recentActivities;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Monthly statistics data")
    public static class MonthlyStats {
        @Schema(description = "Month", example = "2024-01")
        private String month;
        
        @Schema(description = "New photographers", example = "12")
        private Integer newPhotographers;
        
        @Schema(description = "New customers", example = "45")
        private Integer newCustomers;
        
        @Schema(description = "New bookings", example = "78")
        private Integer newBookings;
        
        @Schema(description = "Revenue", example = "8500.00")
        private Double revenue;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Top photographer information")
    public static class TopPhotographer {
        @Schema(description = "Photographer ID", example = "1")
        private BigInteger id;
        
        @Schema(description = "Photographer name", example = "Dr. Ruby Perrin")
        private String name;
        
        @Schema(description = "Specialty", example = "Dental")
        private String specialty;
        
        @Schema(description = "Value (revenue/bookings/rating)", example = "3200.00")
        private Double value;
        
        @Schema(description = "Profile image URL", example = "https://example.com/avatar.jpg")
        private String avatarUrl;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Recent activity information")
    public static class RecentActivity {
        @Schema(description = "Activity ID", example = "1")
        private BigInteger id;
        
        @Schema(description = "Activity type", example = "PHOTOGRAPHER_REGISTERED")
        private String type;
        
        @Schema(description = "Activity description", example = "New photographer registered: Dr. Ruby Perrin")
        private String description;
        
        @Schema(description = "User ID", example = "1")
        private BigInteger userId;
        
        @Schema(description = "User name", example = "Dr. Ruby Perrin")
        private String userName;
        
        @Schema(description = "Activity timestamp", example = "2024-01-01T10:00:00")
        private LocalDateTime timestamp;
        
        @Schema(description = "Additional data", example = "{\"specialty\": \"Dental\"}")
        private String metadata;
    }
}
