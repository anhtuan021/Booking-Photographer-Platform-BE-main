package org.bookingplatform.dto.photographer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response object containing photographer profile information")
public class PhotographerDetailProfileResponse {
    private BigInteger id;
    private BigInteger photographerId;
    private String businessName;
    private String dateOfBirth; // dd/MM/yyyy
    private String bio;
    private Integer yearsExperience;
    private List<String> specialties;
    private List<String> languages;
    private String locationAddress;
    private String city;
    private String ward;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Additional fields for response
    private String firstName;
    private String lastName;
    private String email;
    private String avatarUrl;

    // Field statistic for photographer
    private BigDecimal minPrice;
    private Double averageRating;
    private Integer totalBookings;
    private Long totalFeedbacks;

    private List<ServicePackageResponse> servicePackages;
    private List<SimplePortfolioImageResponse> portfolioImages;

}