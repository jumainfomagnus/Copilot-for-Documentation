package com.enterprise.ecommerce.service;

import com.enterprise.ecommerce.dto.UserDTO;
import com.enterprise.ecommerce.entity.User;
import com.enterprise.ecommerce.exception.ResourceNotFoundException;
import com.enterprise.ecommerce.exception.UserAlreadyExistsException;
import com.enterprise.ecommerce.mapper.UserMapper;
import com.enterprise.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service class for managing user-related operations.
 * Implements UserDetailsService for Spring Security integration.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    /**
     * Load user by username for Spring Security
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    /**
     * Create a new user
     */
    public UserDTO createUser(UserDTO.CreateUserRequest request) {
        log.info("Creating new user with username: {}", request.getUsername());
        
        // Check if user already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists: " + request.getUsername());
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists: " + request.getEmail());
        }

        // Create new user entity
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        
        // Set default values
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEmailVerified(false);
        user.setStatus(User.UserStatus.PENDING_VERIFICATION);
        
        // Set default role
        Set<User.Role> roles = new HashSet<>();
        roles.add(User.Role.USER);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());
        
        // Send verification email
        emailService.sendVerificationEmail(savedUser);
        
        return userMapper.toDTO(savedUser);
    }

    /**
     * Get user by ID
     */
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        return userMapper.toDTO(user);
    }

    /**
     * Get user by username
     */
    @Transactional(readOnly = true)
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return userMapper.toDTO(user);
    }

    /**
     * Get user by email
     */
    @Transactional(readOnly = true)
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return userMapper.toDTO(user);
    }

    /**
     * Update user information
     */
    public UserDTO updateUser(Long id, UserDTO.UpdateUserRequest request) {
        log.info("Updating user with ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());

        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with ID: {}", updatedUser.getId());
        
        return userMapper.toDTO(updatedUser);
    }

    /**
     * Change user password
     */
    public void changePassword(Long id, UserDTO.ChangePasswordRequest request) {
        log.info("Changing password for user ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        // Verify new password confirmation
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirmation do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        
        log.info("Password changed successfully for user ID: {}", id);
    }

    /**
     * Delete user
     */
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        userRepository.delete(user);
        log.info("User deleted successfully with ID: {}", id);
    }

    /**
     * Get all users with pagination
     */
    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDTO);
    }

    /**
     * Search users
     */
    @Transactional(readOnly = true)
    public Page<UserDTO> searchUsers(String search, Pageable pageable) {
        return userRepository.findUsersWithSearch(search, pageable)
                .map(userMapper::toDTO);
    }

    /**
     * Enable/disable user
     */
    public void toggleUserStatus(Long id, boolean enabled) {
        log.info("Toggling user status for ID: {} to {}", id, enabled);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        user.setEnabled(enabled);
        user.setStatus(enabled ? User.UserStatus.ACTIVE : User.UserStatus.INACTIVE);
        
        userRepository.save(user);
        log.info("User status toggled successfully for ID: {}", id);
    }

    /**
     * Lock/unlock user account
     */
    public void toggleUserLock(Long id, boolean locked) {
        log.info("Toggling user lock for ID: {} to {}", id, locked);
        
        if (locked) {
            userRepository.lockUserAccount(id, LocalDateTime.now());
        } else {
            userRepository.unlockUserAccount(id);
        }
        
        log.info("User lock toggled successfully for ID: {}", id);
    }

    /**
     * Verify user email
     */
    public void verifyEmail(Long id) {
        log.info("Verifying email for user ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        user.setEmailVerified(true);
        user.setStatus(User.UserStatus.ACTIVE);
        
        userRepository.save(user);
        log.info("Email verified successfully for user ID: {}", id);
    }

    /**
     * Update user roles
     */
    public void updateUserRoles(Long id, Set<User.Role> roles) {
        log.info("Updating roles for user ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        user.setRoles(roles);
        userRepository.save(user);
        
        log.info("Roles updated successfully for user ID: {}", id);
    }

    /**
     * Record successful login
     */
    public void recordSuccessfulLogin(Long id) {
        userRepository.updateLastLoginDate(id, LocalDateTime.now());
        userRepository.updateFailedLoginAttempts(id, 0);
    }

    /**
     * Record failed login attempt
     */
    public void recordFailedLoginAttempt(String username) {
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(username, username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            int attempts = user.getFailedLoginAttempts() + 1;
            userRepository.updateFailedLoginAttempts(user.getId(), attempts);
            
            // Lock account after 5 failed attempts
            if (attempts >= 5) {
                userRepository.lockUserAccount(user.getId(), LocalDateTime.now());
                log.warn("User account locked due to failed login attempts: {}", username);
            }
        }
    }
}