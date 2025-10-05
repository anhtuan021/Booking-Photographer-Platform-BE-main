package org.bookingplatform.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request object for admin registration")
public class AdminRegisterRequest {
    
    @Schema(description = "Full name of the admin", example = "John Doe", required = true)
    @NotNull(message = "Name is required")
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 200, message = "Name must not exceed 200 characters")
    private String name;
    
    @Schema(description = "Email address of the admin", example = "admin@example.com", required = true)
    @NotNull(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @Schema(description = "Password for the admin account", example = "AdminPass123!", required = true)
    @NotNull(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]", 
             message = "Password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character")
    private String password;
    
    @Schema(description = "Password confirmation", example = "AdminPass123!", required = true)
    @NotNull(message = "Confirm password is required")
    @NotBlank(message = "Confirm password cannot be blank")
    private String confirmPassword;
}
