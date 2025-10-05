package org.bookingplatform.dto.photographer;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PhotographerSearchResponse {
    private BigInteger id;
    private BigInteger userId;
    private String businessName;
    private String bio;
    private Integer yearsExperience;
    private List<String> specialties;
    private List<String> languages;
    private String locationAddress;
    private String city;
    private String ward;    
    private String status;
    private Integer totalReviews;
    private Integer totalBookings;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<ServicePackageResponse> servicePackages;
    private List<PortfolioImageResponse> featuredImages;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String avatarUrl;
}