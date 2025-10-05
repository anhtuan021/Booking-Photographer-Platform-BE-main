package org.bookingplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.bookingplatform.config.APIRoute;
import org.bookingplatform.dto.BaseResponse;
import org.bookingplatform.dto.photographer.*;
import org.bookingplatform.service.ProfileService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
@Tag(name = "User Profile", description = "APIs for managing user profiles")
public class ProfileController {
    private final ProfileService profileService;

    @PutMapping(
            value = APIRoute.PROFILE_ME,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<BaseResponse<PhotographerProfileResponse>> updateProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("businessName") @NotBlank @Size(max = 200) String businessName,
            @RequestParam("firstName") @NotBlank @Size(max = 100) String firstName,
            @RequestParam("lastName") @NotBlank @Size(max = 100) String lastName,
            @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(value = "bio", required = false) @Size(max = 1000) String bio,
            @RequestParam("yearsExperience") @NotNull @Min(0) Integer yearsExperience,
            @RequestParam(value = "locationAddress", required = false) @Size(max = 500) String locationAddress,
            @RequestParam("city") @NotBlank @Size(max = 100) String city,
            @RequestParam(value = "ward", required = false) @Size(max = 100) String ward,
            @RequestParam("specialties") List<String> specialties,
            @RequestParam("languages") List<String> languages,
            @RequestParam("dateOfBirth") String dateOfBirth, // dd/MM/yyyy or mm/dd/yyyy
            @RequestPart(value = "avatar", required = false) MultipartFile avatarFile
    ) {
        try {
            String token = authHeader.startsWith("Bearer ")
                    ? authHeader.substring(7).trim()
                    : authHeader.trim();

            CreatePhotographerProfileRequest profileRequest = new CreatePhotographerProfileRequest();
            profileRequest.setBusinessName(businessName);
            profileRequest.setDateOfBirth(dateOfBirth);
            profileRequest.setMinPrice(minPrice);
            profileRequest.setBio(bio);
            profileRequest.setYearsExperience(yearsExperience);
            profileRequest.setLocationAddress(locationAddress);
            profileRequest.setCity(city);
            profileRequest.setWard(ward);
            profileRequest.setSpecialties(specialties);
            profileRequest.setLanguages(languages);
            profileRequest.setFirstName(firstName);
            profileRequest.setLastName(lastName);

            PhotographerProfileResponse response = profileService
                    .updatePhotographerProfile(token, profileRequest, avatarFile);

            return ResponseEntity.ok(new BaseResponse<>(
                    UUID.randomUUID().toString(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    UUID.randomUUID().toString(), "01030001", e.getMessage()));
        }
    }

    @GetMapping(APIRoute.PROFILE_ME)
    public ResponseEntity<BaseResponse<PhotographerProfileResponse>> getMyProfile(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.startsWith("Bearer ")
                    ? authHeader.substring(7).trim()
                    : authHeader.trim();

            PhotographerProfileResponse response = profileService.getPhotographerProfile(token);
            return ResponseEntity.ok(new BaseResponse<>(UUID.randomUUID().toString(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    UUID.randomUUID().toString(), "01030002", e.getMessage()));
        }
    }

    /**
     * Get photographer profile by ID (public)
     */
    @GetMapping(APIRoute.PROFILES + "/{photographerId}")
    public ResponseEntity<BaseResponse<PhotographerProfileResponse>> getPhotographerProfileById(
            @PathVariable BigInteger photographerId) {
        try {
            PhotographerProfileResponse response = profileService.getPhotographerProfileById(photographerId);
            return ResponseEntity.ok(new BaseResponse<>(UUID.randomUUID().toString(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    UUID.randomUUID().toString(), "01030003", e.getMessage()));
        }
    }

    @GetMapping(APIRoute.PROFILES)
    public ResponseEntity<BaseResponse<List<PhotographerProfileResponse>>> getAllPhotographers() {
        try {
            List<PhotographerProfileResponse> response = profileService.findAllPhotographers();
            return ResponseEntity.ok(new BaseResponse<>(UUID.randomUUID().toString(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    UUID.randomUUID().toString(), "01030004", e.getMessage()));
        }
    }

    @GetMapping(APIRoute.PHOTOGRAPHERS + "/search")
    @Operation(summary = "Search photographers with filters")
    public ResponseEntity<BaseResponse<List<PhotographerProfileResponse>>> searchPhotographers(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String ward,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer yearsExperience,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) List<String> specialties,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy")
            LocalDate create_at) {
        try {
            PhotographerSearchRequest req = new PhotographerSearchRequest();
            req.setId(id);
            req.setCity(city);
            req.setWard(ward);
            req.setCategory(category);
            req.setMinPrice(minPrice);
            req.setMaxPrice(maxPrice);
            req.setYearsExperience(yearsExperience);
            req.setMinRating(minRating);
            req.setSortBy(sortBy);
            req.setSortOrder(sortOrder);
            req.setKeyword(keyword);
            req.setSpecialties(specialties);
            req.setCreate_at(create_at);

            // Call the service method and map the result
            List<PhotographerProfileResponse> result = profileService.search(req);
            return ResponseEntity.ok(new BaseResponse<>(UUID.randomUUID().toString(), result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new BaseResponse<>(UUID.randomUUID().toString(), "01030020", e.getMessage())
            );
        }
    }
}
