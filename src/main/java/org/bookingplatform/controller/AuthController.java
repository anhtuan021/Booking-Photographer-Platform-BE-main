package org.bookingplatform.controller;

import java.math.BigInteger;
import java.util.UUID;

import org.bookingplatform.config.APIRoute;
import org.bookingplatform.dto.BaseRequest;
import org.bookingplatform.dto.BaseResponse;
import org.bookingplatform.dto.auth.AdminRegisterRequest;
import org.bookingplatform.dto.auth.AuthResponse;
import org.bookingplatform.dto.auth.ChangePasswordRequest;
import org.bookingplatform.dto.auth.ForgotPasswordRequest;
import org.bookingplatform.dto.auth.LoginRequest;
import org.bookingplatform.dto.auth.ResetPasswordRequest;
import org.bookingplatform.dto.auth.UserRegisterRequest;
import org.bookingplatform.dto.auth.VerifyTokenRequest;
import org.bookingplatform.entity.User;
import org.bookingplatform.repository.UserRepository;
import org.bookingplatform.service.AuthService;
import org.bookingplatform.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs for user authentication and registration")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Operation(
        summary = "Register Admin",
        description = "Register a new admin user with name, email, password, and confirm password"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Admin registered successfully"),
        @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input or user already exists")
    })
    @PostMapping(APIRoute.REGISTER + "/admin")
    public ResponseEntity<BaseResponse<AuthResponse>> registerAdmin(
            @Valid @RequestBody BaseRequest<AdminRegisterRequest> request) {
        try {
            AuthResponse response = authService.registerAdmin(request.getRequestParameters());
            return ResponseEntity.ok(new BaseResponse<>(
                    request.getRequestTrace(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    request.getRequestTrace(), "01020002", e.getMessage()));
        }
    }

    @Operation(
        summary = "Register User",
        description = "Register a new user (customer or photographer) with name, email, password, and role"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input or user already exists")
    })
    @PostMapping(APIRoute.REGISTER + "/user")
    public ResponseEntity<BaseResponse<AuthResponse>> registerUser(
            @Valid @RequestBody BaseRequest<UserRegisterRequest> request) {
        try {
            AuthResponse response = authService.registerUser(request.getRequestParameters());
            return ResponseEntity.ok(new BaseResponse<>(
                    request.getRequestTrace(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    request.getRequestTrace(), "01020003", e.getMessage()));
        }
    }

    @PostMapping(APIRoute.LOGIN)
    public ResponseEntity<BaseResponse<AuthResponse>> login(
            @Valid @RequestBody BaseRequest<LoginRequest> request) {
        try {
            AuthResponse response = authService.login(request.getRequestParameters());
            return ResponseEntity.ok(new BaseResponse<>(
                    request.getRequestTrace(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    request.getRequestTrace(), "01010001", e.getMessage()));
        }
    }

    @PostMapping(APIRoute.LOGOUT)
    public ResponseEntity<BaseResponse<Void>> logout(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.startsWith("Bearer ")
                    ? authHeader.substring(7)
                    : authHeader;
            authService.logout(token);
            return ResponseEntity.ok(new BaseResponse<>(
                    UUID.randomUUID().toString(), "00000000", "Logout successful"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    UUID.randomUUID().toString(), "01010001", e.getMessage()));
        }
    }

    @PostMapping("/resend-email-verification")
    public ResponseEntity<BaseResponse<Void>> resendEmailVerification(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody BaseRequest<Void> request) {
        try {
            authService.resendEmailVerification(token);
            return ResponseEntity.ok(new BaseResponse<>(
                    request.getRequestTrace(), "00000000", "Email verification sent"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    request.getRequestTrace(), "01020001", e.getMessage()));
        }
    }

    @PostMapping("/resend-phone-verification")
    public ResponseEntity<BaseResponse<Void>> resendPhoneVerification(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody BaseRequest<Void> request) {
        try {
            authService.resendPhoneVerification(token);
            return ResponseEntity.ok(new BaseResponse<>(
                    request.getRequestTrace(), "00000000", "Phone verification sent"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    request.getRequestTrace(), "01020001", e.getMessage()));
        }
    }

    @Operation(
        summary = "Change Password",
        description = "Change user password with current password verification"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password changed successfully"),
        @ApiResponse(responseCode = "400", description = "Bad Request - Invalid current password or validation error"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token")
    })
    @PostMapping(APIRoute.CHANGE_PASSWORD)
    public ResponseEntity<BaseResponse<Void>> changePassword(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody BaseRequest<ChangePasswordRequest> request) {
        try {
            String token = authHeader.startsWith("Bearer ")
                    ? authHeader.substring(7)
                    : authHeader;

            authService.changePassword(token, request.getRequestParameters());
            return ResponseEntity.ok(new BaseResponse<>(
                    request.getRequestTrace(), "00000000", "Password changed successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    request.getRequestTrace(), "01020001", e.getMessage()));
        }
    }

    @Operation(
        summary = "Forgot Password",
        description = "Request password reset by sending reset token to email"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password reset email sent successfully"),
        @ApiResponse(responseCode = "400", description = "Bad Request - User not found or invalid email")
    })
    @PostMapping(APIRoute.FORGOT_PASSWORD)
    public ResponseEntity<BaseResponse<Void>> forgotPassword(
            @Valid @RequestBody BaseRequest<ForgotPasswordRequest> request) {
        try {
            authService.forgotPassword(request.getRequestParameters().getEmail());
            return ResponseEntity.ok(new BaseResponse<>(
                    request.getRequestTrace(), "00000000", "Password reset email sent"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    request.getRequestTrace(), "01020001", e.getMessage()));
        }
    }

    @Operation(
        summary = "Reset Password",
        description = "Reset password using reset token from email"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password reset successfully"),
        @ApiResponse(responseCode = "400", description = "Bad Request - Invalid or expired token, or validation error")
    })
    @PostMapping(APIRoute.RESET_PASSWORD)
    public ResponseEntity<BaseResponse<Void>> resetPassword(
            @Valid @RequestBody BaseRequest<ResetPasswordRequest> request) {
        try {
            authService.resetPassword(request.getRequestTrace(), request.getRequestParameters());
            return ResponseEntity.ok(new BaseResponse<>(
                    request.getRequestTrace(), "00000000", "Password reset successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    request.getRequestTrace(), "01020001", e.getMessage()));
        }
    }

    @PostMapping(APIRoute.AUTH + "/verify-token")
    public ResponseEntity<BaseResponse<Void>> verifyToken(
            @Valid @RequestBody BaseRequest<VerifyTokenRequest> request) {
        try {
            String token = request.getRequestParameters().getToken();
            String userId = jwtService.extractUserId(token);
            User user = userRepository.findById(new BigInteger(userId)).orElse(null);

            boolean valid = user != null && jwtService.isTokenValid(token, user);
            String message = valid ? "Token is valid" : "Token is invalid";
            String code = valid ? "00000000" : "01010001";
            return ResponseEntity.ok(new BaseResponse<>(
                    request.getRequestTrace(), code, message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    request.getRequestTrace(), "01010001", e.getMessage()));
        }
    }
}
