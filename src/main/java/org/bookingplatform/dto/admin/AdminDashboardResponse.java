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
@Schema(description = "Response object containing admin dashboard data")
public class AdminDashboardResponse {
    
    @Schema(description = "Total number of photographers", example = "168")
    private Integer totalPhotographers;
    
    @Schema(description = "Total number of customers", example = "487")
    private Integer totalCustomers;
    
    @Schema(description = "Total number of appointments/bookings", example = "485")
    private Integer totalAppointments;
    
    @Schema(description = "Total revenue", example = "62523.50")
    private Double totalRevenue;
    
    @Schema(description = "Photographers growth percentage", example = "12.5")
    private Double photographersGrowth;
    
    @Schema(description = "Customers growth percentage", example = "8.3")
    private Double customersGrowth;
    
    @Schema(description = "Appointments growth percentage", example = "15.2")
    private Double appointmentsGrowth;
    
    @Schema(description = "Revenue growth percentage", example = "22.1")
    private Double revenueGrowth;
    
    @Schema(description = "Recent photographers list")
    private List<PhotographerSummary> recentPhotographers;
    
    @Schema(description = "Recent customers list")
    private List<CustomerSummary> recentCustomers;
    
    @Schema(description = "Recent appointments list")
    private List<AppointmentSummary> recentAppointments;
    
    @Schema(description = "Revenue chart data")
    private List<RevenueChartData> revenueChartData;
    
    @Schema(description = "Booking status distribution")
    private List<BookingStatusData> bookingStatusData;
    
    @Schema(description = "Photographer status distribution")
    private List<PhotographerStatusData> photographerStatusData;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Photographer summary information")
    public static class PhotographerSummary {
        @Schema(description = "Photographer ID", example = "1")
        private BigInteger id;
        
        @Schema(description = "Photographer name", example = "Dr. Ruby Perrin")
        private String name;
        
        @Schema(description = "Specialty", example = "Dental")
        private String specialty;
        
        @Schema(description = "Total earned amount", example = "3200.00")
        private Double earned;
        
        @Schema(description = "Average rating", example = "4.5")
        private Double rating;
        
        @Schema(description = "Profile image URL", example = "https://example.com/avatar.jpg")
        private String avatarUrl;
        
        @Schema(description = "Status", example = "APPROVED")
        private String status;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Customer summary information")
    public static class CustomerSummary {
        @Schema(description = "Customer ID", example = "1")
        private BigInteger id;
        
        @Schema(description = "Customer name", example = "Charlene Reed")
        private String name;
        
        @Schema(description = "Phone number", example = "8286329170")
        private String phone;
        
        @Schema(description = "Last visit date", example = "2023-10-20")
        private LocalDateTime lastVisit;
        
        @Schema(description = "Total amount paid", example = "100.00")
        private Double paid;
        
        @Schema(description = "Profile image URL", example = "https://example.com/avatar.jpg")
        private String avatarUrl;
        
        @Schema(description = "Status", example = "ACTIVE")
        private String status;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Appointment summary information")
    public static class AppointmentSummary {
        @Schema(description = "Appointment ID", example = "1")
        private BigInteger id;
        
        @Schema(description = "Photographer name", example = "Dr. Ruby Perrin")
        private String photographerName;
        
        @Schema(description = "Specialty", example = "Dental")
        private String specialty;
        
        @Schema(description = "Customer name", example = "Charlene Reed")
        private String customerName;
        
        @Schema(description = "Appointment time", example = "2023-11-09T11:00:00")
        private LocalDateTime appointmentTime;
        
        @Schema(description = "Status", example = "CONFIRMED")
        private String status;
        
        @Schema(description = "Amount", example = "200.00")
        private Double amount;
        
        @Schema(description = "Photographer avatar URL", example = "https://example.com/avatar.jpg")
        private String photographerAvatarUrl;
        
        @Schema(description = "Customer avatar URL", example = "https://example.com/avatar.jpg")
        private String customerAvatarUrl;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Revenue chart data")
    public static class RevenueChartData {
        @Schema(description = "Date", example = "2024-01-01")
        private String date;
        
        @Schema(description = "Revenue amount", example = "1500.00")
        private Double revenue;
        
        @Schema(description = "Number of bookings", example = "5")
        private Integer bookings;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Booking status distribution")
    public static class BookingStatusData {
        @Schema(description = "Status name", example = "PENDING")
        private String status;
        
        @Schema(description = "Count", example = "25")
        private Integer count;
        
        @Schema(description = "Percentage", example = "15.5")
        private Double percentage;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Photographer status distribution")
    public static class PhotographerStatusData {
        @Schema(description = "Status name", example = "APPROVED")
        private String status;
        
        @Schema(description = "Count", example = "120")
        private Integer count;
        
        @Schema(description = "Percentage", example = "71.4")
        private Double percentage;
    }
}