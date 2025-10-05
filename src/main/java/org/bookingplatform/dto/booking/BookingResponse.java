package org.bookingplatform.dto.booking;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BookingResponse {
    private BigInteger id;
    private String bookingCode;
    private BigInteger customerId;
    private BigInteger photographerId;
    private String speciality;
    private String reasonReject;

    private List<ServicePackageInfo> servicePackages;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private String status;
    private String note;
    private BigDecimal price;
    private BigDecimal totalDiscount;
    private BigDecimal totalPayment;
    private String paymentStatus;
    private String paymentMethod;
    private String paymentReference;
    private BigInteger cancelledBy;
    private LocalDateTime cancelledAt;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String customerName;
    private String customerEmail;
    private String customerPhone;

    private String photographerName;
    private String photographerEmail;
    private String photographerPhone;

    private BigDecimal commissionRate;
    private BigDecimal adminAmount;
    private BigDecimal photographerAmount;

    @Data
    @Builder
    public static class ServicePackageInfo {
        private BigInteger id;
        private String name;
        private String description;
        private BigDecimal price;
    }
}