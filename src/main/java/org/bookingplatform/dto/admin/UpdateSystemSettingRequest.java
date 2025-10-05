package org.bookingplatform.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@Schema(description = "Request object for updating a system setting")
public class UpdateSystemSettingRequest {
    
    @Schema(description = "New setting value", example = "20971520", required = true)
    @NotBlank(message = "Setting value is required")
    @Size(max = 1000, message = "Setting value must not exceed 1000 characters")
    private String value;
}