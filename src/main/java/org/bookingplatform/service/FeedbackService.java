package org.bookingplatform.service;

import org.bookingplatform.dto.feedback.CreateFeedbackRequest;
import org.bookingplatform.dto.feedback.FeedbackResponse;

import java.util.List;

public interface FeedbackService {

    FeedbackResponse createFeedback(String token, CreateFeedbackRequest request);

    // customer feedback management
    List<FeedbackResponse> getMyFeedbacks(String token);
    
    FeedbackResponse getFeedbackById(String feedbackId);
    
    // =============================================
    // PHOTOGRAPHER FEEDBACK MANAGEMENT
    // =============================================
    
    List<FeedbackResponse> getPhotographerFeedbacks(String token);
    
    List<FeedbackResponse> getPhotographerFeedbacksById(String photographerId);

    // =============================================
    // ADMIN FEEDBACK MANAGEMENT
    // =============================================

    void rejectFeedback(String token, String feedbackId);
    
    List<FeedbackResponse> getAllFeedbacks();
}
