package org.bookingplatform.dto.photographer;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ServicePackagesResponse {
    
    private String photographerId;
    private String photographerName;
    private List<ServicePackageResponse> packages;
    private Integer totalPackages;
    
    @Data
    public static class ServicePackageResponse {
        private String id;
        private String name;
        private String description;
        private BigDecimal basePrice;
        private Integer durationHours;
        private Integer maxPhotos;
        private List<String> includes;
        private List<String> addOns;
        private Boolean isActive;
    }
}
