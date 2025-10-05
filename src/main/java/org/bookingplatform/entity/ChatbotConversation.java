package org.bookingplatform.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "chatbot_conversations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChatbotConversation extends BaseEntity {
    
    @Column(name = "user_id")
    private String userId;
    
    @Column(name = "session_id", nullable = false)
    private String sessionId;
    
    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;
    
    @Column(name = "response", columnDefinition = "TEXT")
    private String response;
    
    @Column(name = "intent")
    private String intent;
    
    @Column(name = "confidence", precision = 3, scale = 2)
    private BigDecimal confidence;
    
    @Column(name = "is_resolved", nullable = false)
    private Boolean resolved = false;
}