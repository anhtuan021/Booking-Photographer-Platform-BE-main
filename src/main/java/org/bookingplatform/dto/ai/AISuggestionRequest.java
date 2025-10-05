package org.bookingplatform.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AISuggestionRequest {
    private String sessionId;
    private String location;
    private List<String> specialties;
    private Double budget;
    private String eventType;
    private String date;
}