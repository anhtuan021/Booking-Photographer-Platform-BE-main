package org.bookingplatform.dto.booking;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class BookingSearchRequest {
    
    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;
    
    @Size(max = 100, message = "District must not exceed 100 characters")
    private String district;
    
    private String category;
    
    private java.math.BigDecimal minPrice;
    
    private java.math.BigDecimal maxPrice;
    
    private String sortBy; // "price", "rating", "distance", "availability"
    
    private String sortDirection; // "asc", "desc"
    
    private Integer page = 0;
    
    private Integer size = 20;
}
