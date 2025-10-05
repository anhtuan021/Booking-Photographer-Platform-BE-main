package org.bookingplatform.dto.photographer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "Request object for creating or updating a photographer profile")
public class CreatePhotographerProfileRequest {
    
    @Schema(description = "Business name of the photographer", example = "Mike Photography Studio", required = true)
    @NotBlank(message = "Business name is required")
    @Size(max = 200, message = "Business name must not exceed 200 characters")
    private String businessName;

    private String firstName;

    private String lastName;

    private String dateOfBirth; // dd/MM/yyyy
    
    @Schema(description = "Biography or description of the photographer", example = "Professional photographer with 5+ years experience in wedding and portrait photography", maxLength = 1000)
    @Size(max = 1000, message = "Bio must not exceed 1000 characters")
    private String bio;

    private BigDecimal minPrice;
    
    @Schema(description = "Years of professional photography experience", example = "5", minimum = "0", required = true)
    @NotNull(message = "Years of experience is required")
    @Min(value = 0, message = "Years of experience must be non-negative")
    private Integer yearsExperience;
    
    @Schema(description = "Full address of the photographer's location", example = "123 Main Street, District 1, Ho Chi Minh City", maxLength = 500)
    @Size(max = 500, message = "Location address must not exceed 500 characters")
    private String locationAddress;
    
    @Schema(description = "City where the photographer is located", example = "Ho Chi Minh City", required = true)
    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;
    
    @Schema(description = "Ward or district within the city", example = "District 1", maxLength = 100)
    @Size(max = 100, message = "Ward must not exceed 100 characters")
    private String ward;

    @Schema(description = "List of photography specialties", example = "[\"WEDDING\", \"PORTRAIT\", \"EVENT\"]", required = true)
    @NotNull(message = "Specialties are required")
    private List<String> specialties;

    @Schema(description = "List of languages spoken by the photographer", example = "[\"Vietnamese\", \"English\"]", required = true)
    @NotNull(message = "Languages are required")
    private List<String> languages;


}