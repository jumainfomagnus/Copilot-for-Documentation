package com.enterprise.ecommerce.controller;

import com.enterprise.ecommerce.dto.AuthDTO;
import com.enterprise.ecommerce.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication operations.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "APIs for user authentication")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    @ApiResponse(responseCode = "200", description = "Login successful")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    @PostMapping("/login")
    public ResponseEntity<AuthDTO.LoginResponse> login(@Valid @RequestBody AuthDTO.LoginRequest request) {
        log.info("Login attempt for username: {}", request.getUsername());
        AuthDTO.LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "User registration", description = "Register a new user account")
    @ApiResponse(responseCode = "200", description = "Registration successful")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "409", description = "User already exists")
    @PostMapping("/register")
    public ResponseEntity<AuthDTO.RegisterResponse> register(@Valid @RequestBody AuthDTO.RegisterRequest request) {
        log.info("Registration attempt for username: {}", request.getUsername());
        AuthDTO.RegisterResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Refresh token", description = "Generate new access token using refresh token")
    @ApiResponse(responseCode = "200", description = "Token refreshed successfully")
    @ApiResponse(responseCode = "401", description = "Invalid refresh token")
    @PostMapping("/refresh")
    public ResponseEntity<AuthDTO.RefreshTokenResponse> refreshToken(@Valid @RequestBody AuthDTO.RefreshTokenRequest request) {
        log.info("Token refresh attempt");
        AuthDTO.RefreshTokenResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "User logout", description = "Logout user and invalidate token")
    @ApiResponse(responseCode = "200", description = "Logout successful")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        log.info("Logout attempt");
        authService.logout(token);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Forgot password", description = "Send password reset email")
    @ApiResponse(responseCode = "200", description = "Password reset email sent")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody AuthDTO.ForgotPasswordRequest request) {
        log.info("Password reset request for email: {}", request.getEmail());
        authService.forgotPassword(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Reset password", description = "Reset password using reset token")
    @ApiResponse(responseCode = "200", description = "Password reset successfully")
    @ApiResponse(responseCode = "400", description = "Invalid reset token")
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody AuthDTO.ResetPasswordRequest request) {
        log.info("Password reset attempt");
        authService.resetPassword(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Verify email", description = "Verify user email address")
    @ApiResponse(responseCode = "200", description = "Email verified successfully")
    @ApiResponse(responseCode = "400", description = "Invalid verification token")
    @GetMapping("/verify-email")
    public ResponseEntity<Void> verifyEmail(@RequestParam String token) {
        log.info("Email verification attempt");
        authService.verifyEmail(token);
        return ResponseEntity.ok().build();
    }
}