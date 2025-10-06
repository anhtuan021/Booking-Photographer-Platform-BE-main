package org.bookingplatform.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookingplatform.constant.Role;
import org.bookingplatform.constant.UserStatus;
import org.bookingplatform.dto.auth.*;
import org.bookingplatform.entity.*;
import org.bookingplatform.exception.DuplicateUserException;
import org.bookingplatform.repository.*;
import org.bookingplatform.service.AuthService;
import org.bookingplatform.service.EmailService;
import org.bookingplatform.service.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final CustomerProfileRepository customerProfileRepository;
    private final PortfolioImageRepository portfolioImageRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Override
    public AuthResponse registerAdmin(AdminRegisterRequest request) {
        // Validate password confirmation
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Password and confirm password do not match");
        }
        
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateUserException("User with this email already exists");
        }
        
        // Split name into first and last name
        String[] nameParts = request.getName().trim().split("\\s+", 2);
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        
        // Create new admin user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(Role.ADMIN);
        user.setStatus(UserStatus.ACTIVE); // Admin is immediately active
        user.setEmailVerified(true); // Admin email is pre-verified
        user.setPhoneVerified(false);
        
        User savedUser = userRepository.save(user);
        
        // Create CustomerProfile for admin
        CustomerProfile customerProfile = new CustomerProfile();
        customerProfile.setUser(savedUser);
        customerProfileRepository.save(customerProfile);
        
        // Create response
        AuthResponse response = new AuthResponse();
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo();
        userInfo.setId(savedUser.getId());
        userInfo.setEmail(savedUser.getEmail());
        userInfo.setFirstName(savedUser.getFirstName());
        userInfo.setLastName(savedUser.getLastName());
        userInfo.setRole(savedUser.getRole().name());
        userInfo.setStatus(savedUser.getStatus().name());
        userInfo.setEmailVerified(savedUser.getEmailVerified());
        userInfo.setPhoneVerified(savedUser.getPhoneVerified());
        userInfo.setLastLoginAt(LocalDateTime.now());
        response.setUser(userInfo);
        
        return response;
    }

    @Override
    public AuthResponse registerUser(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateUserException("User with this email already exists");
        }

        // Split name into first and last name
        String[] nameParts = request.getName().trim().split("\\s+", 2);
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        
        // Create new user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(request.getRole());
        user.setStatus(UserStatus.PENDING_VERIFICATION);
        user.setEmailVerified(false);
        user.setPhoneVerified(false);

        User savedUser = userRepository.save(user);

        if (savedUser.getRole() == Role.PHOTOGRAPHER) {
            // Create UserProfile for photographer
            UserProfile profile = new UserProfile();
            profile.setUser(savedUser);
            userProfileRepository.save(profile);

            // Create Portfolio for photographer
            PortfolioImage portfolio = new PortfolioImage();
            portfolio.setPhotographer(profile);
            portfolioImageRepository.save(portfolio);
        } else if (savedUser.getRole() == Role.CUSTOMER) {
            // Create CustomerProfile for customer
            CustomerProfile customerProfile = new CustomerProfile();
            customerProfile.setUser(savedUser);
            // Set default values if needed
            customerProfileRepository.save(customerProfile);
        }

        // Create response
        AuthResponse response = new AuthResponse();
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo();
        userInfo.setId(savedUser.getId());
        userInfo.setEmail(savedUser.getEmail());
        userInfo.setFirstName(savedUser.getFirstName());
        userInfo.setLastName(savedUser.getLastName());
        userInfo.setRole(savedUser.getRole().name());
        userInfo.setStatus(savedUser.getStatus().name());
        userInfo.setEmailVerified(savedUser.getEmailVerified());
        userInfo.setPhoneVerified(savedUser.getPhoneVerified());
        userInfo.setLastLoginAt(LocalDateTime.now());
        response.setUser(userInfo);
        
        return response;
    }
    
    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("No user found with this email"));

        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new RuntimeException("Your account is was not active. Current status: " + user.getStatus().name());
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }
        log.debug("User found: {}", user.getEmail());
        
        // Update last login
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
        
        // Generate JWT token
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        
        // Create response
        AuthResponse response = new AuthResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(86400L); // 24 hours
        
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setEmail(user.getEmail());
        userInfo.setFirstName(user.getFirstName());
        userInfo.setLastName(user.getLastName());
//        userInfo.setPhone(user.getPhone());
//        userInfo.setAvatarUrl(user.getAvatarUrl());
        userInfo.setRole(user.getRole().name());
        userInfo.setStatus(user.getStatus().name());
        userInfo.setEmailVerified(user.getEmailVerified());
        userInfo.setPhoneVerified(user.getPhoneVerified());
        userInfo.setLastLoginAt(user.getLastLoginAt());
        
        response.setUser(userInfo);
        
        return response;
    }
    
//    @Override
//    public AuthResponse socialLogin(SocialLoginRequest request) {
//        // Validate social login token with the provider
//        SocialLoginService.SocialUserInfo socialUserInfo = socialLoginService.validateSocialToken(
//                request.getProvider(), request.getAccessToken());
//
//        // Check if user already exists with this social login
//        User user = userRepository.findBySocialLoginProviderAndSocialLoginId(
//                request.getProvider(), socialUserInfo.getProviderId())
//                .orElse(null);
//
//        if (user == null) {
//            // Check if user exists with the same email
//            user = userRepository.findByEmail(socialUserInfo.getEmail()).orElse(null);
//
//            if (user == null) {
//                // Create new user from social login
//                user = new User();
//                user.setEmail(socialUserInfo.getEmail());
//                user.setFirstName(socialUserInfo.getFirstName());
//                user.setLastName(socialUserInfo.getLastName());
//                user.setPhone(socialUserInfo.getPhone());
//                user.setAvatarUrl(socialUserInfo.getAvatarUrl());
//                user.setRole(request.getUserType());
//                user.setStatus(UserStatus.ACTIVE);
//                user.setEmailVerified(true); // Social login users are considered verified
//                user.setPhoneVerified(false);
//                user.setSocialLoginProvider(request.getProvider());
//                user.setSocialLoginId(socialUserInfo.getProviderId());
//                user.setLastLoginAt(LocalDateTime.now());
//            } else {
//                // Link existing user with social login
//                user.setSocialLoginProvider(request.getProvider());
//                user.setSocialLoginId(socialUserInfo.getProviderId());
//                user.setLastLoginAt(LocalDateTime.now());
//            }
//
//            user = userRepository.save(user);
//        } else {
//            // Update last login for existing user
//            user.setLastLoginAt(LocalDateTime.now());
//            user = userRepository.save(user);
//        }
//
//        // Generate JWT token
//        String accessToken = jwtService.generateToken(user);
//        String refreshToken = jwtService.generateRefreshToken(user);
//
//        // Create response
//        AuthResponse response = new AuthResponse();
//        response.setAccessToken(accessToken);
//        response.setRefreshToken(refreshToken);
//        response.setExpiresIn(86400L); // 24 hours
//
//        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo();
//        userInfo.setId(user.getId());
//        userInfo.setEmail(user.getEmail());
//        userInfo.setFirstName(user.getFirstName());
//        userInfo.setLastName(user.getLastName());
//        userInfo.setPhone(user.getPhone());
//        userInfo.setAvatarUrl(user.getAvatarUrl());
//        userInfo.setRole(user.getRole().name());
//        userInfo.setStatus(user.getStatus().name());
//        userInfo.setEmailVerified(user.getEmailVerified());
//        userInfo.setPhoneVerified(user.getPhoneVerified());
//        userInfo.setLastLoginAt(user.getLastLoginAt());
//
//        response.setUser(userInfo);
//
//        return response;
//    }

    @Override
    public void changePassword(String token, ChangePasswordRequest request) {
        String userId = jwtService.extractUserId(token);
        BigInteger userIdBigInt = new BigInteger(userId);

        User user = userRepository.findById(userIdBigInt)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // Verify new password confirmation
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("New password and confirmation do not match");
        }

        // Check if new password is different from current password
        if (passwordEncoder.matches(request.getNewPassword(), user.getPasswordHash())) {
            throw new RuntimeException("New password must be different from current password");
        }

        // Update password
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        log.info("Password changed successfully for user: {}", user.getEmail());
    }

    @Override
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate reset token
        String resetToken = UUID.randomUUID().toString();

        // Create password reset token entity
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(resetToken);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiresAt(LocalDateTime.now().plusHours(1)); // Token expires in 1 hour
        passwordResetToken.setIsUsed(false);

        // Generate secure token
        String rawToken = generateSecureToken();

        // Save token to database
        passwordResetTokenRepository.save(passwordResetToken);

        // TODO: Send email with reset link
        String baseUrl = "http://localhost:5000"; // Replace with actual base URL
        String resetLink = baseUrl + "/reset-password?token=" + rawToken;
        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
    }

    private String generateSecureToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    @Override
    public void resetPassword(String token, ResetPasswordRequest request) {
        // Validate new password confirmation
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("New password and confirmation do not match");
        }

        // Find valid reset token
        PasswordResetToken resetToken = passwordResetTokenRepository.findValidToken(
                        request.getResetToken(), LocalDateTime.now())
                .orElseThrow(() -> new RuntimeException("Invalid or expired reset token"));

        // Get user from token
        User user = resetToken.getUser();

        // Check if new password is different from current password
        if (passwordEncoder.matches(request.getNewPassword(), user.getPasswordHash())) {
            throw new RuntimeException("New password must be different from current password");
        }

        // Update user password
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        // Mark token as used
        passwordResetTokenRepository.markTokenAsUsed(request.getResetToken(), LocalDateTime.now());

        log.info("Password reset successfully for user: {}", user.getEmail());
    }

    @Override
    @Transactional
    public void logout(String token) {
        // 1. Extract user ID from token
        String userId = jwtService.extractUserId(token);
        BigInteger userIdBigInt;
        try {
            userIdBigInt = new BigInteger(userId);
        } catch (NumberFormatException e) {
            log.warn("Invalid userId extracted from token: {}", userId);
            throw new IllegalArgumentException("Invalid userId in token");
        }

        // 2. Blacklist token (for stateless JWT)
//        long expirationMillis = jwtService.getExpirationTime(token);
//        long ttl = expirationMillis - System.currentTimeMillis();
//        if (ttl > 0) {
//            tokenBlacklistService.addToBlacklist(token, ttl);
//            log.info("Token blacklisted for userId={}, ttl={}ms", userIdBigInt, ttl);
//        } else {
//            log.warn("Token already expired for userId={}", userIdBigInt);
//        }
//
//        // 3. Update user's last logout time in DB (optional but useful for auditing)
//        userRepository.findById(userIdBigInt).ifPresent(user -> {
//            user.setLastLogoutAt(LocalDateTime.now());
//            userRepository.save(user);
//            log.info("User {} (email={}) logged out at {}", user.getId(), user.getEmail(), user.getLastLogoutAt());
//        });
        System.out.println("afafas");
    }

    @Override
    public void resendEmailVerification(String token) {
        String userId = jwtService.extractUserId(token);
        BigInteger userIdBigInt = new BigInteger(userId);

        User user = userRepository.findById(userIdBigInt)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // TODO: Implement email verification resend logic
        // Generate verification code, send email, etc.
        System.out.println("Resending email verification to: " + user.getEmail());
    }

    @Override
    public void resendPhoneVerification(String token) {
        String userId = jwtService.extractUserId(token);
        BigInteger userIdBigInt = new BigInteger(userId);

        User user = userRepository.findById(userIdBigInt)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // TODO: Implement phone verification resend logic
        // Generate verification code, send SMS, etc.
        System.out.println("Resending phone verification to: " + user.getPhone());
    }
}
