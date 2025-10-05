package org.bookingplatform.dto.review;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class UpdateReviewRequest {
    
    @NotBlank(message = "Status is required")
    private String status;
    
    @Size(max = 500, message = "Admin notes must not exceed 500 characters")
    private String adminNotes;
}
