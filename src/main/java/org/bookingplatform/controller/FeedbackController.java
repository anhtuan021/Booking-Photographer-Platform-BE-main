package org.bookingplatform.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bookingplatform.config.APIRoute;
import org.bookingplatform.dto.BaseRequest;
import org.bookingplatform.dto.BaseResponse;
import org.bookingplatform.dto.feedback.CreateFeedbackRequest;
import org.bookingplatform.dto.feedback.FeedbackResponse;
import org.bookingplatform.service.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(APIRoute.FEEDBACKS)
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    // Create Feedback
    @PostMapping
    public ResponseEntity<BaseResponse<FeedbackResponse>> createFeedback(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody BaseRequest<CreateFeedbackRequest> request) {
        try {
            String token = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7).trim()
                : authHeader.trim();

            FeedbackResponse response = feedbackService.createFeedback(token, request.getRequestParameters());
            return ResponseEntity.ok(new BaseResponse<>(
                request.getRequestTrace(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                request.getRequestTrace(), "05010001", e.getMessage()));
        }
    }
    
    @GetMapping("/customer/me")
    public ResponseEntity<BaseResponse<List<FeedbackResponse>>> getMyFeedbacks(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.startsWith("Bearer ")
                    ? authHeader.substring(7).trim()
                    : authHeader.trim();

            List<FeedbackResponse> response = feedbackService.getMyFeedbacks(token);
            return ResponseEntity.ok(new BaseResponse<>(
                    UUID.randomUUID().toString(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    UUID.randomUUID().toString(), "05010001", e.getMessage()));
        }
    }
    
    @GetMapping("/{feedbackId}")
    public ResponseEntity<BaseResponse<FeedbackResponse>> getFeedbackById(
            @PathVariable String feedbackId) {
        try {
            FeedbackResponse response = feedbackService.getFeedbackById(feedbackId);
            return ResponseEntity.ok(new BaseResponse<>(
                    UUID.randomUUID().toString(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    UUID.randomUUID().toString(), "05010001", e.getMessage()));
        }
    }
    
    // =============================================
    // PHOTOGRAPHER FEEDBACK MANAGEMENT
    // =============================================

    // Get feedbacks for the authenticated photographer
    @GetMapping("/photographer/me")
    public ResponseEntity<BaseResponse<List<FeedbackResponse>>> getPhotographerFeedbacks(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.startsWith("Bearer ")
                    ? authHeader.substring(7).trim()
                    : authHeader.trim();

            List<FeedbackResponse> response = feedbackService.getPhotographerFeedbacks(token);
            return ResponseEntity.ok(new BaseResponse<>(
                    UUID.randomUUID().toString(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    UUID.randomUUID().toString(), "05020001", e.getMessage()));
        }
    }

    // Get all feedbacks for a specific photographer by their ID
    @GetMapping("/photographer/{photographerId}")
    public ResponseEntity<BaseResponse<List<FeedbackResponse>>> getFeedbacksByPhotographerId(
            @PathVariable String photographerId) {
        try {
            List<FeedbackResponse> response = feedbackService.getPhotographerFeedbacksById(photographerId);
            return ResponseEntity.ok(new BaseResponse<>(
                    UUID.randomUUID().toString(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    UUID.randomUUID().toString(), "05020001", e.getMessage()));
        }
    }

    // =============================================
    // ADMIN FEEDBACK MANAGEMENT
    // =============================================
    @DeleteMapping("/admin/reject/{feedbackId}")
    public ResponseEntity<BaseResponse<Void>> rejectFeedback(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String feedbackId) {
        try {
            String token = authHeader.startsWith("Bearer ")
                    ? authHeader.substring(7).trim()
                    : authHeader.trim();

            feedbackService.rejectFeedback(token, feedbackId);
            return ResponseEntity.ok(new BaseResponse<>(
                    UUID.randomUUID().toString(), "00000000", "Feedback rejected successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    UUID.randomUUID().toString(), "05030001", e.getMessage()));
        }
    }
    
    @GetMapping("/admin")
    public ResponseEntity<BaseResponse<List<FeedbackResponse>>> getAllFeedbacks() {//@RequestHeader("Authorization") String authHeader) {
        try {
//            String token = authHeader.startsWith("Bearer ")
//                    ? authHeader.substring(7).trim()
//                    : authHeader.trim();

            List<FeedbackResponse> response = feedbackService.getAllFeedbacks();
            return ResponseEntity.ok(new BaseResponse<>(
                    UUID.randomUUID().toString(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    UUID.randomUUID().toString(), "05030001", e.getMessage()));
        }
    }
}
