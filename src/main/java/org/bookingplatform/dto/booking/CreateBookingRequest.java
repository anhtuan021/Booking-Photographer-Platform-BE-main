package org.bookingplatform.dto.booking;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import org.bookingplatform.constant.PaymentStatus;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class CreateBookingRequest {
    @NotNull(message = "Photographer ID is required")
    private BigInteger photographerId;

    @NotBlank(message = "Speciality is required")
    private String speciality;

    @NotNull(message = "Service package names are required")
    private List<String> services;

    @NotNull(message = "Booking date is required")
    private String date;

    @NotBlank(message = "Booking time slot is required")
    private String timeSlot;

    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String note;
    private BigDecimal price;
    private BigDecimal totalDiscount;
    private BigDecimal totalPayment;
    // private BigDecimal totalAmount;
    private String paymentStatus;
}