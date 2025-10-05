package org.bookingplatform.dto.customer;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerSearchRequest {
    private Integer id;
    private String city;
    private String ward;
    private String keyword;
    private String sortBy;
    private String sortOrder;
    private LocalDate create_at;
}