package org.bookingplatform.dto.photographer;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class CreateServicePackageRequest {
    @NotBlank(message = "Package name is required")
    @Size(max = 200, message = "Package name must not exceed 200 characters")
    private String name;

    private String code;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.0", message = "Base price must be non-negative")
    private BigDecimal basePrice;

    @Min(value = 1, message = "Max photos must be at least 1")
    private Integer maxPhotos;

    @NotBlank(message = "Speciality is required")
    private String specialityCode;

    private Boolean isPremium = false;
    private BigDecimal discount = BigDecimal.ZERO;
}