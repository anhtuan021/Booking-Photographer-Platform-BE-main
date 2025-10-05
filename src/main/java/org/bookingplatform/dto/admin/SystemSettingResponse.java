package org.bookingplatform.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object containing system setting information")
public class SystemSettingResponse {
    
    @Schema(description = "Setting key", example = "MAX_FILE_SIZE")
    private String key;
    
    @Schema(description = "Setting value", example = "10485760")
    private String value;
    
    @Schema(description = "Setting description", example = "Maximum file size in bytes")
    private String description;
    
    @Schema(description = "Setting category", example = "FILE_UPLOAD")
    private String category;
    
    @Schema(description = "Data type", example = "NUMBER")
    private String dataType;
    
    @Schema(description = "Is editable", example = "true")
    private Boolean isEditable;
    
    @Schema(description = "Created date", example = "2024-01-01T10:00:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Updated date", example = "2024-01-01T10:00:00")
    private LocalDateTime updatedAt;
}