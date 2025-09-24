package com.enterprise.ecommerce.service;

import com.enterprise.ecommerce.dto.AuthDTO;

/**
 * Service interface for authentication operations.
 */
public interface AuthService {
    
    AuthDTO.LoginResponse login(AuthDTO.LoginRequest request);
    
    AuthDTO.RegisterResponse register(AuthDTO.RegisterRequest request);
    
    AuthDTO.RefreshTokenResponse refreshToken(AuthDTO.RefreshTokenRequest request);
    
    void logout(String token);
    
    void forgotPassword(AuthDTO.ForgotPasswordRequest request);
    
    void resetPassword(AuthDTO.ResetPasswordRequest request);
    
    void verifyEmail(String token);
}