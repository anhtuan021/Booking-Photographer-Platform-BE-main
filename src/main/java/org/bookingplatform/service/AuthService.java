package org.bookingplatform.service;

import org.bookingplatform.dto.auth.*;

public interface AuthService {
    AuthResponse registerAdmin(AdminRegisterRequest request);
    
    AuthResponse registerUser(UserRegisterRequest request);
    
    AuthResponse login(LoginRequest request);

    void changePassword(String token, ChangePasswordRequest request);

    void forgotPassword(String email);

    void resetPassword(String token, ResetPasswordRequest request);

    void logout(String token);
    
    void resendEmailVerification(String token);
    
    void resendPhoneVerification(String token);
}
