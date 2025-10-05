package org.bookingplatform.entity;

import jakarta.persistence.*;
import lombok.*;
import org.bookingplatform.constant.UserFeedback;

import java.math.BigDecimal;

@Entity
@Table(name = "ai_suggestions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AISuggestion extends BaseEntity {
    @Column(name = "user_id", nullable = false)
    private String userId;
    
    @Column(name = "search_criteria", columnDefinition = "JSON")
    private String searchCriteria;
    
    @Column(name = "suggested_photographers", columnDefinition = "JSON")
    private String suggestedPhotographers;
    
    @Column(name = "suggestion_algorithm", nullable = false)
    private String suggestionAlgorithm;
    
    @Column(name = "confidence_score", precision = 3, scale = 2)
    private BigDecimal confidenceScore;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "user_feedback")
    private UserFeedback userFeedback;
}