package org.bookingplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bookingplatform.config.APIRoute;
import org.bookingplatform.dto.BaseResponse;
import org.bookingplatform.dto.photographer.PortfolioImageResponse;
import org.bookingplatform.dto.photographer.UploadPortfolioImageRequest;
import org.bookingplatform.service.PortfolioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
@Tag(name = "Photographer Portfolio", description = "APIs for managing photographer portfolios")
public class PortfolioController {
    private final PortfolioService photographerService;

    // create new portfolio image
    @Operation(summary = "Upload a new portfolio image")
    @PostMapping(APIRoute.PORTFOLIOS)
    public ResponseEntity<BaseResponse<PortfolioImageResponse>> uploadPortfolioImage(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("images") MultipartFile[] images,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("category") String category,
            @RequestParam(value = "tags", required = false) String tags,
            @RequestParam(value = "displayOrder", required = false) Integer displayOrder,
            @RequestParam(value = "isFeatured", defaultValue = "false") Boolean isFeatured) {
        try {
            String token = authHeader.startsWith("Bearer ")
                    ? authHeader.substring(7).trim()
                    : authHeader.trim();

            // Build request DTO
            UploadPortfolioImageRequest request = new UploadPortfolioImageRequest();
            request.setTitle(title);
            request.setDescription(description);
            request.setCategory(category);
            request.setDisplayOrder(displayOrder);
            request.setIsFeatured(isFeatured);
            request.setImages(Arrays.asList(images));

            if (tags != null && !tags.trim().isEmpty()) {
                List<String> tagList = Arrays.stream(tags.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toList();
                request.setTags(tagList);
            }

            PortfolioImageResponse response = photographerService.uploadPortfolioImage(token, request);

            return ResponseEntity.ok(new BaseResponse<>(UUID.randomUUID().toString(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    UUID.randomUUID().toString(), "01030009", e.getMessage()));
        }
    }


    // update existing portfolio image
    @Operation(summary = "Update an existing portfolio image")
    @PutMapping(APIRoute.PORTFOLIOS)
    public ResponseEntity<BaseResponse<PortfolioImageResponse>> updatePortfolioImage(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("portfolioId") BigInteger portfolioId,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "tags", required = false) String tags,
            @RequestParam(value = "displayOrder", required = false) Integer displayOrder,
            @RequestParam(value = "isFeatured", defaultValue = "false") Boolean isFeatured
    ) {
        try {
            String token = authHeader.startsWith("Bearer ")
                    ? authHeader.substring(7).trim()
                    : authHeader.trim();

            UploadPortfolioImageRequest request = new UploadPortfolioImageRequest();
            request.setPortfolioId(portfolioId);
            request.setTitle(title);
            request.setDescription(description);
            request.setCategory(category);
            request.setDisplayOrder(displayOrder);
            request.setIsFeatured(isFeatured);

            if (tags != null && !tags.trim().isEmpty()) {
                List<String> tagList = Arrays.stream(tags.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toList();
                request.setTags(tagList);
            }

            PortfolioImageResponse response = photographerService.updatePortfolioImage(
                    token,
                    portfolioId,
                    request,
                    imageFile
            );

            return ResponseEntity.ok(new BaseResponse<>(UUID.randomUUID().toString(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    UUID.randomUUID().toString(), "01030011", e.getMessage()));
        }
    }

    @DeleteMapping(APIRoute.PORTFOLIOS + "/{imageId}")
    public ResponseEntity<BaseResponse<String>> deletePortfolioImage(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable BigInteger imageId) {
        try {
            String token = authHeader.startsWith("Bearer ")
                    ? authHeader.substring(7).trim()
                    : authHeader.trim();

            photographerService.deletePortfolioImage(token, imageId);
            return ResponseEntity.ok(new BaseResponse<>(UUID.randomUUID().toString(), "Portfolio image deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    UUID.randomUUID().toString(), "01030011", e.getMessage()));
        }
    }

    /**
     * Get my portfolio images
     */
    @Operation(summary = "Get all my portfolio images")
    @GetMapping(APIRoute.PORTFOLIOS + "/me")
    public ResponseEntity<BaseResponse<List<PortfolioImageResponse>>> getAllMyPortfolioImages(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.startsWith("Bearer ")
                    ? authHeader.substring(7).trim()
                    : authHeader.trim();

            List<PortfolioImageResponse> response = photographerService.getAllMyPortfolioImages(token);
            return ResponseEntity.ok(new BaseResponse<>(UUID.randomUUID().toString(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    UUID.randomUUID().toString(), "01030030", e.getMessage()));
        }
    }

    // get all photographer's portfolio images (public)
    @GetMapping(APIRoute.PORTFOLIOS)
    public ResponseEntity<BaseResponse<List<PortfolioImageResponse>>> getAllPortfolioImages() {
        try {
            List<PortfolioImageResponse> response = photographerService.getAllPortfolioImages();
            return ResponseEntity.ok(new BaseResponse<>(UUID.randomUUID().toString(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    UUID.randomUUID().toString(), "01030032", e.getMessage()));
        }
    }

    @Operation(summary = "Get portfolios of other photographers by Id")
    @GetMapping(APIRoute.PORTFOLIOS + "/{photographerId}")
    public ResponseEntity<BaseResponse<List<PortfolioImageResponse>>> getPortfolioByPhotographerId(
            @PathVariable BigInteger photographerId) {
        try {
            List<PortfolioImageResponse> response = photographerService.getPhotographerPortfolio(photographerId);
            return ResponseEntity.ok(new BaseResponse<>(UUID.randomUUID().toString(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new BaseResponse<>(UUID.randomUUID().toString(), "01030012", e.getMessage())
            );
        }
    }
}
