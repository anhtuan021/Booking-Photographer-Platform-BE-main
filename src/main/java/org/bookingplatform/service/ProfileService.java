package org.bookingplatform.service;

import org.bookingplatform.dto.photographer.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.List;

public interface ProfileService {
    PhotographerProfileResponse updatePhotographerProfile(
            String token,
            CreatePhotographerProfileRequest request,
            MultipartFile avatarFile);

    // Get My profile
    PhotographerProfileResponse getPhotographerProfile(String token);

    // Get profile by id
    PhotographerProfileResponse getPhotographerProfileById(BigInteger photographerId);

    List<PhotographerProfileResponse> search(PhotographerSearchRequest req);

    List<PhotographerProfileResponse> findAllPhotographers();
}
