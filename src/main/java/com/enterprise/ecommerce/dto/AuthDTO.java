package com.enterprise.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Objects for authentication operations.
 */
public class AuthDTO {

    /**
     * Login request DTO
     */
    @Data
    public static class LoginRequest {
        @NotBlank(message = "Username is required")
        private String username;
        
        @NotBlank(message = "Password is required")
        private String password;
        
        private Boolean rememberMe = false;
    }

    /**
     * Login response DTO
     */
    @Data
    public static class LoginResponse {
        private String accessToken;
        private String refreshToken;
        private String tokenType = "Bearer";
        private Long expiresIn;
        private UserDTO user;
    }

    /**
     * Registration request DTO
     */
    @Data
    public static class RegisterRequest {
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        private String username;
        
        @Email(message = "Email should be valid")
        @NotBlank(message = "Email is required")
        private String email;
        
        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        private String password;
        
        @NotBlank(message = "Confirm password is required")
        private String confirmPassword;
        
        @NotBlank(message = "First name is required")
        @Size(max = 50, message = "First name must not exceed 50 characters")
        private String firstName;
        
        @NotBlank(message = "Last name is required")
        @Size(max = 50, message = "Last name must not exceed 50 characters")
        private String lastName;
        
        @Size(max = 20, message = "Phone number must not exceed 20 characters")
        private String phoneNumber;
    }

    /**
     * Registration response DTO
     */
    @Data
    public static class RegisterResponse {
        private String message;
        private UserDTO user;
    }

    /**
     * Refresh token request DTO
     */
    @Data
    public static class RefreshTokenRequest {
        @NotBlank(message = "Refresh token is required")
        private String refreshToken;
    }

    /**
     * Refresh token response DTO
     */
    @Data
    public static class RefreshTokenResponse {
        private String accessToken;
        private String tokenType = "Bearer";
        private Long expiresIn;
    }

    /**
     * Forgot password request DTO
     */
    @Data
    public static class ForgotPasswordRequest {
        @Email(message = "Email should be valid")
        @NotBlank(message = "Email is required")
        private String email;
    }

    /**
     * Reset password request DTO
     */
    @Data
    public static class ResetPasswordRequest {
        @NotBlank(message = "Reset token is required")
        private String token;
        
        @NotBlank(message = "New password is required")
        @Size(min = 8, message = "New password must be at least 8 characters")
        private String newPassword;
        
        @NotBlank(message = "Confirm password is required")
        private String confirmPassword;
    }
}