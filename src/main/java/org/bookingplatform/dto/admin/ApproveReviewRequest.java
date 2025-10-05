package org.bookingplatform.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApproveReviewRequest {
    @NotBlank(message = "Status is required")
    private String status;
    
    @NotBlank(message = "Reviewed by is required")
    private String reviewedBy;
}
