package com.enterprise.ecommerce.controller;

import com.enterprise.ecommerce.dto.UserDTO;
import com.enterprise.ecommerce.entity.User;
import com.enterprise.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * REST controller for user management operations.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create a new user", description = "Register a new user in the system")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "409", description = "User already exists")
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO.CreateUserRequest request) {
        log.info("Received request to create user with username: {}", request.getUsername());
        UserDTO createdUser = userService.createUser(request);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Get user by ID", description = "Retrieve user details by user ID")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or authentication.name == @userService.getUserById(#id).username")
    public ResponseEntity<UserDTO> getUserById(
            @Parameter(description = "User ID") @PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Get user by username", description = "Retrieve user details by username")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN') or authentication.name == #username")
    public ResponseEntity<UserDTO> getUserByUsername(
            @Parameter(description = "Username") @PathVariable String username) {
        UserDTO user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Update user information", description = "Update user profile information")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or authentication.name == @userService.getUserById(#id).username")
    public ResponseEntity<UserDTO> updateUser(
            @Parameter(description = "User ID") @PathVariable Long id,
            @Valid @RequestBody UserDTO.UpdateUserRequest request) {
        log.info("Received request to update user with ID: {}", id);
        UserDTO updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Change user password", description = "Change user's password")
    @ApiResponse(responseCode = "200", description = "Password changed successfully")
    @ApiResponse(responseCode = "400", description = "Invalid password data")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/{id}/password")
    @PreAuthorize("hasRole('ADMIN') or authentication.name == @userService.getUserById(#id).username")
    public ResponseEntity<Void> changePassword(
            @Parameter(description = "User ID") @PathVariable Long id,
            @Valid @RequestBody UserDTO.ChangePasswordRequest request) {
        log.info("Received request to change password for user ID: {}", id);
        userService.changePassword(id, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete user", description = "Delete a user from the system")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID") @PathVariable Long id) {
        log.info("Received request to delete user with ID: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all users", description = "Retrieve all users with pagination")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable pageable) {
        Page<UserDTO> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Search users", description = "Search users by various criteria")
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDTO>> searchUsers(
            @Parameter(description = "Search query") @RequestParam(required = false) String search,
            Pageable pageable) {
        Page<UserDTO> users = userService.searchUsers(search, pageable);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Enable/disable user", description = "Toggle user enabled status")
    @ApiResponse(responseCode = "200", description = "User status updated successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> toggleUserStatus(
            @Parameter(description = "User ID") @PathVariable Long id,
            @Parameter(description = "Enabled status") @RequestParam boolean enabled) {
        log.info("Received request to toggle status for user ID: {} to {}", id, enabled);
        userService.toggleUserStatus(id, enabled);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Lock/unlock user account", description = "Toggle user account lock status")
    @ApiResponse(responseCode = "200", description = "User lock status updated successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/{id}/lock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> toggleUserLock(
            @Parameter(description = "User ID") @PathVariable Long id,
            @Parameter(description = "Locked status") @RequestParam boolean locked) {
        log.info("Received request to toggle lock for user ID: {} to {}", id, locked);
        userService.toggleUserLock(id, locked);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Verify user email", description = "Mark user email as verified")
    @ApiResponse(responseCode = "200", description = "Email verified successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/{id}/verify-email")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> verifyEmail(
            @Parameter(description = "User ID") @PathVariable Long id) {
        log.info("Received request to verify email for user ID: {}", id);
        userService.verifyEmail(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update user roles", description = "Update user's assigned roles")
    @ApiResponse(responseCode = "200", description = "Roles updated successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateUserRoles(
            @Parameter(description = "User ID") @PathVariable Long id,
            @RequestBody Set<User.Role> roles) {
        log.info("Received request to update roles for user ID: {}", id);
        userService.updateUserRoles(id, roles);
        return ResponseEntity.ok().build();
    }
}