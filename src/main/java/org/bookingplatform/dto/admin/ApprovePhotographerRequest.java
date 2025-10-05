package org.bookingplatform.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.Size;

@Data
@Schema(description = "Request object for approving a photographer")
public class ApprovePhotographerRequest {
    
    @Schema(description = "Approval notes", example = "Profile looks good, approved for platform", maxLength = 500)
    @Size(max = 500, message = "Approval notes must not exceed 500 characters")
    private String approvalNotes;
}