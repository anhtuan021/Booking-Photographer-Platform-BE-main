package org.bookingplatform.dto.ai;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
class SuggestedPhotographer {
    private String photographerId;
    private String photographerName;
    private String businessName;
    private String city;
    private String district;
    private Double averageRating;
    private Integer totalReviews;
    private java.math.BigDecimal basePrice;
    private List<String> specialties;
    private String matchReason;
    private Double matchScore;
    private List<SuggestedAction> suggestedActions;
}
