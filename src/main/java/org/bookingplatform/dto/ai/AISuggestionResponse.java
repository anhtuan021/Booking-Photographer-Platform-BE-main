package org.bookingplatform.dto.ai;

import lombok.Data;
import lombok.Builder;
import java.util.List;

@Data
@Builder
public class AISuggestionResponse {
    
    private String sessionId;
    private List<SuggestedPhotographer> suggestions;
    private String algorithm;
    private Double confidenceScore;
    private String reasoning;
    private List<String> alternativeOptions;
    private java.time.LocalDateTime createdAt;
    private String feedback;
    
    @Data
    @Builder
    public static class SuggestedPhotographer {
        private String photographerId;
        private String photographerName;
        private String businessName;
        private java.util.List<String> specialties;
        private String city;
        private String district;
        private Double averageRating;
        private Integer totalReviews;
        private String priceRange;
        private String profileImageUrl;
    }
}

