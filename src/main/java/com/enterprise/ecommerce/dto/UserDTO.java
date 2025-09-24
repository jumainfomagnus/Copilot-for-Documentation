package com.enterprise.ecommerce.dto;

import com.enterprise.ecommerce.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Data Transfer Object for User entity.
 */
@Data
public class UserDTO {
    private Long id;
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
    
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastName;
    
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;
    
    private Boolean enabled;
    private Boolean emailVerified;
    private LocalDateTime lastLoginDate;
    private User.UserStatus status;
    private Set<User.Role> roles;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    /**
     * Request DTO for user registration
     */
    @Data
    public static class CreateUserRequest {
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        private String username;
        
        @Email(message = "Email should be valid")
        @NotBlank(message = "Email is required")
        private String email;
        
        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        private String password;
        
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
     * Request DTO for user update
     */
    @Data
    public static class UpdateUserRequest {
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
     * Request DTO for password change
     */
    @Data
    public static class ChangePasswordRequest {
        @NotBlank(message = "Current password is required")
        private String currentPassword;
        
        @NotBlank(message = "New password is required")
        @Size(min = 8, message = "New password must be at least 8 characters")
        private String newPassword;
        
        @NotBlank(message = "Confirm password is required")
        private String confirmPassword;
    }
}