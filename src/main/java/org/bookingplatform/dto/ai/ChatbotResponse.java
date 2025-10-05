package org.bookingplatform.dto.ai;

import lombok.Data;
import lombok.Builder;
import java.util.List;

@Data
@Builder
public class ChatbotResponse {
    
    private String sessionId;
    private String response;
    private String intent;
    private Double confidence;
    private Boolean isResolved;
    private List<SuggestedAction> suggestedActions;
    private String context;
    private List<String> relatedTopics;
    private java.time.LocalDateTime createdAt;
    
    @Data
    @Builder
    public static class SuggestedAction {
        private String text;
        private String action;
        private String url;
    }
}

