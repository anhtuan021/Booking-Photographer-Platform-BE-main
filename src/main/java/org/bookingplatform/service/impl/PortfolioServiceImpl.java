package org.bookingplatform.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookingplatform.constant.ImageCategory;
import org.bookingplatform.constant.ImageStatus;
import org.bookingplatform.dto.photographer.PortfolioImageResponse;
import org.bookingplatform.dto.photographer.UploadPortfolioImageRequest;
import org.bookingplatform.entity.PortfolioImage;
import org.bookingplatform.entity.UserProfile;
import org.bookingplatform.repository.*;
import org.bookingplatform.service.JwtService;
import org.bookingplatform.service.PortfolioService;
import org.bookingplatform.service.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PortfolioServiceImpl implements PortfolioService {
    private final UserProfileRepository userProfileRepository;
    private final PortfolioImageRepository portfolioImageRepository;

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final S3Service s3Service;

    @Override
    public PortfolioImageResponse uploadPortfolioImage(
            String token,
            UploadPortfolioImageRequest request) throws IOException {

        String userId = jwtService.extractUserId(token);
        BigInteger userIdBigInt = new BigInteger(userId);

        UserProfile profile = userProfileRepository.findByUser_Id(userIdBigInt)
                .orElseThrow(() -> new RuntimeException("Photographer profile not found"));

        log.debug("Photographer ID: {}", profile.getId());

        // upload multi images to S3
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile image : request.getImages()) {
            String uploadedUrl = s3Service.uploadFile(image);
            imageUrls.add(uploadedUrl);
        }
        // Build entity
        PortfolioImage portfolioImage = new PortfolioImage();
        portfolioImage.setPhotographer(profile);
        portfolioImage.setImageUrl(toJson(imageUrls)); // list -> JSON
        portfolioImage.setTitle(request.getTitle());
        portfolioImage.setDescription(request.getDescription());
        portfolioImage.setCategory(ImageCategory.valueOf(request.getCategory().toUpperCase()));
        portfolioImage.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);
        portfolioImage.setIsFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : false);
        portfolioImage.setTags(toJson(request.getTags())); // convert list → JSON
        portfolioImage.setStatus(ImageStatus.PENDING_APPROVAL);

        // Lưu DB
        PortfolioImage saved = portfolioImageRepository.save(portfolioImage);

        // Convert entity → response
        return mapToPortfolioImageResponse(saved);
    }

    @Override
    public PortfolioImageResponse updatePortfolioImage(
            String token,
            BigInteger imageId,
            UploadPortfolioImageRequest request,
            MultipartFile imageFile) {

        String userId = jwtService.extractUserId(token);
        BigInteger userIdBigInt = new BigInteger(userId);

        UserProfile profile = userProfileRepository.findByUser_Id(userIdBigInt)
                               .orElseThrow(() -> new RuntimeException("Photographer profile not found"));

        PortfolioImage image = portfolioImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Portfolio image not found"));

//        if (!image.getPhotographer().getUser().getId().equals(profile.getId())) {
//            throw new RuntimeException("You can only update your own portfolio images");
//        }

        // If a new image file is provided, upload to S3 and delete the old image
        if (imageFile != null && !imageFile.isEmpty()) {
            //String newKey = "portfolio/" + profile.getId() + "/" + UUID.randomUUID();

            try {
                String newImageUrl = s3Service.uploadFile(imageFile);

                // Delete old image from S3 if exists
                if (image.getImageUrl() != null) {
                    s3Service.deleteFileByUrl(image.getImageUrl());
                }

                // Store as JSON array
                image.setImageUrl(toJson(List.of(newImageUrl)));
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload new portfolio image", e);
            }
        }

        // Update fields if present
        if (request.getTitle() != null) image.setTitle(request.getTitle());
        if (request.getDescription() != null) image.setDescription(request.getDescription());
        if (request.getCategory() != null) image.setCategory(ImageCategory.valueOf(request.getCategory().toUpperCase()));
        if (request.getDisplayOrder() != null) image.setDisplayOrder(request.getDisplayOrder());
        if (request.getIsFeatured() != null) image.setIsFeatured(request.getIsFeatured());
        if (request.getTags() != null) image.setTags(toJson(request.getTags()));

        PortfolioImage saved = portfolioImageRepository.save(image);

        return PortfolioImageResponse.builder()
                .id(saved.getId())
                .photographerId(profile.getId())
                .businessName(profile.getBusinessName())
                .imageUrl(fromJson(saved.getImageUrl()))
                .minPrice(profile.getMinPrice())
                .title(saved.getTitle())
                .description(saved.getDescription())
                .category(saved.getCategory().name())
                .tags(fromJson(saved.getTags()))
                .displayOrder(saved.getDisplayOrder())
                .isFeatured(saved.getIsFeatured())
                .status(saved.getStatus().name())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    @Override
    public void deletePortfolioImage(String token, BigInteger imageId) {
        String userId = jwtService.extractUserId(token);
        BigInteger userIdBigInt = new BigInteger(userId);

        UserProfile profile = userProfileRepository.findById(userIdBigInt)
                .orElseThrow(() -> new RuntimeException("Photographer profile not found"));

        PortfolioImage image = portfolioImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Portfolio image not found"));

        // Check if photographer owns this image
        if (!image.getPhotographer().getId().equals(profile.getId())) {
            throw new RuntimeException("You can only delete your own portfolio images");
        }

        portfolioImageRepository.delete(image);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PortfolioImageResponse> getAllMyPortfolioImages(String token) {
        String userId = jwtService.extractUserId(token);
        BigInteger userIdBigInt = new BigInteger(userId);
        log.debug("User ID: {}", userIdBigInt);

        UserProfile profile = userProfileRepository.findByUser_Id(userIdBigInt)
                .orElseThrow(() -> new RuntimeException("Photographer profile not found"));

        List<PortfolioImage> images = portfolioImageRepository.findByPhotographerIdOrderByDisplayOrderAsc(profile.getId());


        return images.stream()
                .map(this::mapToPortfolioImageResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PortfolioImageResponse> getAllPortfolioImages() {
        List<PortfolioImage> images = portfolioImageRepository.findAll();
        return images.stream()
                .filter(img -> img.getPhotographer() != null)
                .map(this::mapToPortfolioImageResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PortfolioImageResponse> getPhotographerPortfolio(BigInteger photographerId) {
        List<PortfolioImage> images = portfolioImageRepository.findAllByUserId(photographerId);

        if (images.isEmpty()) {
            throw new RuntimeException("No portfolio found for userId: " + photographerId);
        }

        return images.stream()
                .map(this::mapToPortfolioImageResponse)
                .collect(Collectors.toList());
    }

    PortfolioImageResponse mapToPortfolioImageResponse(PortfolioImage image) {
        List<String> tags = fromJson(image.getTags());

        return PortfolioImageResponse.builder()
                .id(image.getId())
                .photographerId(image.getPhotographer().getId())
                .businessName(image.getPhotographer().getBusinessName())
                .imageUrl(fromJson(image.getImageUrl()))
                .minPrice(image.getPhotographer().getMinPrice())
                .title(image.getTitle())
                .description(image.getDescription())
                .category(image.getCategory().name())
                .displayOrder(image.getDisplayOrder())
                .isFeatured(image.getIsFeatured())
                .tags(tags)
                .status(image.getStatus().name())
                .createdAt(image.getCreatedAt())
                .updatedAt(image.getUpdatedAt())
                .build();
    }

    private List<String> fromJson(String json) {
        try {
            if (json == null || json.trim().isEmpty()) {
                return Collections.emptyList();
            }
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize list: " + json, e);
        }
    }

    private String toJson(List<String> list) {
        try {
            if (list == null || list.isEmpty()) {
                return "[]";
            }
            return objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize list", e);
        }
    }
}
