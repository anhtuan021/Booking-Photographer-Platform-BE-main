package org.bookingplatform.dto.photographer;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.List;

@Data
public class UploadPortfolioImageRequest {
    @NotNull
    private BigInteger portfolioId;

//    @NotNull(message = "Image URL is required")
//    @Size(max = 500, message = "Image URL must not exceed 500 characters")
//    private String imageUrl;

    @NotNull(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
    
    @NotNull(message = "Category is required")
    private String category; // PORTRAIT, WEDDING, EVENT, etc.
    
    @Min(value = 0, message = "Display order must be non-negative")
    private Integer displayOrder;
    
    private Boolean isFeatured;
    
    private List<String> tags; // JSON string

    private List<MultipartFile> images;
}