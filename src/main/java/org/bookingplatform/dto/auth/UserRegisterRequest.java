package org.bookingplatform.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.bookingplatform.constant.Role;

@Data
@Schema(description = "Request object for user registration (customer/photographer)")
public class UserRegisterRequest {
    
    @Schema(description = "Full name of the user", example = "Jane Smith", required = true)
    @NotNull(message = "Name is required")
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 200, message = "Name must not exceed 200 characters")
    private String name;
    
    @Schema(description = "Email address of the user", example = "user@example.com", required = true)
    @NotNull(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @Schema(description = "Password for the user account", example = "UserPass123!", required = true)
    @NotNull(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]", 
             message = "Password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character")
    private String password;
    
    @Schema(description = "Role of the user", example = "CUSTOMER", required = true, allowableValues = {"CUSTOMER", "PHOTOGRAPHER"})
    @NotNull(message = "Role is required")
    private Role role; // CUSTOMER or PHOTOGRAPHER
}
