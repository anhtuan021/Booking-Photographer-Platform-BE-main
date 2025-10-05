package org.bookingplatform.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private String id;
    private String bookingCode;
    private String customerId;
    private String photographerId;
    private String servicePackageId;
    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private Integer durationHours;
    private String locationAddress;
    private String specialRequests;
    private String status;
    private BigDecimal totalAmount;
    private String paymentStatus;
    private String paymentMethod;
    private String paymentReference;
    private String cancellationReason;
    private String cancelledBy;
    private LocalDateTime cancelledAt;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
