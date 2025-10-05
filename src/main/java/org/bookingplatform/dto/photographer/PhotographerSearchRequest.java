package org.bookingplatform.dto.photographer;

import lombok.Data;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PhotographerSearchRequest {
    private Integer id;

    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;
    
    @Size(max = 100, message = "Ward must not exceed 100 characters")
    private String ward;
    
    private String category; // PORTRAIT, WEDDING, EVENT, etc.
    
    private BigDecimal minPrice;
    
    private BigDecimal maxPrice;
    
    private Integer yearsExperience;
    

    private Double minRating;
    
    private String sortBy; // rating, price, experience, created_at
    
    private String sortOrder; // asc, desc
    
    private String keyword; // General search query
    private List<String> specialties;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate create_at;
}