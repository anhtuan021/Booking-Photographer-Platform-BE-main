package org.bookingplatform.dto.photographer;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PortfolioResponse {
    
    private String photographerId;
    private String photographerName;
    private List<PortfolioImageResponse> images;
    private Integer totalImages;
    
    @Data
    public static class PortfolioImageResponse {
        private String id;
        private String imageUrl;
        private String thumbnailUrl;
        private String title;
        private String description;
        private String category;
        private List<String> tags;
        private Integer displayOrder;
        private Boolean isFeatured;
        private String status;
        private LocalDateTime createdAt;
    }
}
