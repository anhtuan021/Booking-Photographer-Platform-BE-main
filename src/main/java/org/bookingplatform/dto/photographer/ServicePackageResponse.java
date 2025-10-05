package org.bookingplatform.dto.photographer;

import lombok.Data;
import lombok.Builder;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Builder
public class ServicePackageResponse {
    private BigInteger id;
    private BigInteger specialityId;
    private String speciality;
    private String name;
    private String code;
    private String description;
    private BigDecimal basePrice;
    private Integer maxPhotos;
    private Boolean isActive;
    private Boolean isPremium;
    private BigDecimal discount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
