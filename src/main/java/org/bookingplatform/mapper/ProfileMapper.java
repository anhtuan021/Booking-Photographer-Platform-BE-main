package org.bookingplatform.mapper;

import org.bookingplatform.dto.photographer.PhotographerProfileResponse;
import org.bookingplatform.dto.photographer.SimplePortfolioImageResponse;
import org.bookingplatform.entity.User;
import org.bookingplatform.entity.UserProfile;

import java.util.Collections;
import java.util.List;

import static org.bookingplatform.util.ObjectMapperUtil.fromJson;

public class ProfileMapper {

    public static PhotographerProfileResponse mapToProfileResponse(UserProfile profile,
                                                                   List<String> specialties) {

        User user = profile.getUser();
        // Map portfolio images -> Just id and imageUrl
        List<SimplePortfolioImageResponse> portfolioImageResponses = profile.getPortfolioImages() != null
                ? profile.getPortfolioImages().stream()
                .map(image -> SimplePortfolioImageResponse.builder()
                        .id(image.getId())
                        .imageUrl(image.getImageUrl())
                        .build())
                .toList()
                : Collections.emptyList();


        return PhotographerProfileResponse.builder()
                .id(profile.getId())
                .photographerId(profile.getUser().getId())
                .businessName(profile.getBusinessName())
                .email(profile.getUser().getEmail())
                .dateOfBirth(profile.getDateOfBirth() != null
                        ? profile.getDateOfBirth().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                        : null)
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .bio(profile.getBio())
                .minPrice(profile.getMinPrice())
                .yearsExperience(profile.getYearsExperience())
                .locationAddress(profile.getLocationAddress())
                .city(profile.getCity())
                .specialties(specialties)
                .languages(fromJson(profile.getLanguages()))
                .ward(profile.getWard())
                .status(profile.getStatus().name())
                .avatarUrl(profile.getAvatarUrl())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .portfolioImages(portfolioImageResponses)
                .build();
    }
}
