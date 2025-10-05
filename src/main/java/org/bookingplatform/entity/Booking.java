package org.bookingplatform.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bookingplatform.constant.BookingStatus;
import org.bookingplatform.constant.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings", indexes = {
    @Index(name = "idx_bookings_customer", columnList = "customer_id"),
    @Index(name = "idx_bookings_photographer", columnList = "photographer_id"),
    @Index(name = "idx_bookings_date", columnList = "booking_date"),
    @Index(name = "idx_bookings_status", columnList = "status"),
    @Index(name = "idx_bookings_code", columnList = "booking_code")
})
@Data
@EqualsAndHashCode(callSuper = true)
public class Booking extends BaseEntity {

    @Column(name = "booking_code", length = 20, unique = true, nullable = false)
    private String bookingCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @Column(name = "specialities", length = 255)
    private String specialities; // e.g., "WEDDING,EVENT"

    @Column(name = "customer_phone", length = 200)
    private String customerPhone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photographer_id", nullable = false)
    private UserProfile photographer;

    @Column(name = "speciality", length = 50, nullable = false)
    private String speciality;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private BookingStatus status;

    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "total_discount", precision = 10, scale = 2)
    private BigDecimal totalDiscount;

    @Column(name = "total_payment", precision = 10, scale = 2)
    private BigDecimal totalPayment;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", length = 20, nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "payment_reference", length = 255)
    private String paymentReference;

    @Column(name = "reason_reject", columnDefinition = "TEXT")
    private String reasonReject;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancelled_by")
    private User cancelledBy;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "commission_rate", precision = 5, scale = 2, nullable = false)
    private BigDecimal commissionRate = BigDecimal.ZERO;

    @Column(name = "photographer_amount", precision = 10, scale = 2)
    private BigDecimal photographerAmount = BigDecimal.ZERO;

    @Column(name = "admin_amount", precision = 10, scale = 2)
    private BigDecimal adminAmount = BigDecimal.ZERO;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BookingService> bookingServices = new ArrayList<>();
}
