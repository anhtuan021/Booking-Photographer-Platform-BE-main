package org.bookingplatform.dto.photographer;

import lombok.Data;
import lombok.Builder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PortfolioImageResponse {
    private BigInteger id;
    private BigInteger photographerId;
    private String businessName;
    private List<String> imageUrl;
    private String title;
    private String description;
    private String category;
    private List<String> tags;
    private Integer displayOrder;
    private Boolean isFeatured;
    private BigDecimal minPrice;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
