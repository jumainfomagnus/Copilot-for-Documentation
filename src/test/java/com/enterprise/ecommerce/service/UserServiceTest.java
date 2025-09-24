package com.enterprise.ecommerce.service;

import com.enterprise.ecommerce.dto.UserDTO;
import com.enterprise.ecommerce.entity.User;
import com.enterprise.ecommerce.exception.ResourceNotFoundException;
import com.enterprise.ecommerce.exception.UserAlreadyExistsException;
import com.enterprise.ecommerce.mapper.UserMapper;
import com.enterprise.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test class for UserService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;
    private UserDTO.CreateUserRequest createUserRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEnabled(true);
        user.setEmailVerified(false);
        user.setStatus(User.UserStatus.PENDING_VERIFICATION);
        user.setCreatedDate(LocalDateTime.now());
        
        Set<User.Role> roles = new HashSet<>();
        roles.add(User.Role.USER);
        user.setRoles(roles);

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testuser");
        userDTO.setEmail("test@example.com");
        userDTO.setFirstName("Test");
        userDTO.setLastName("User");

        createUserRequest = new UserDTO.CreateUserRequest();
        createUserRequest.setUsername("testuser");
        createUserRequest.setEmail("test@example.com");
        createUserRequest.setPassword("password123");
        createUserRequest.setFirstName("Test");
        createUserRequest.setLastName("User");
    }

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

        // When
        UserDTO result = userService.createUser(createUserRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
        assertThat(result.getEmail()).isEqualTo("test@example.com");

        verify(userRepository).existsByUsername("testuser");
        verify(userRepository).existsByEmail("test@example.com");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
        verify(emailService).sendVerificationEmail(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when username already exists")
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.createUser(createUserRequest))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("Username already exists");

        verify(userRepository).existsByUsername("testuser");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.createUser(createUserRequest))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("Email already exists");

        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should get user by ID successfully")
    void shouldGetUserByIdSuccessfully() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        // When
        UserDTO result = userService.getUserById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("testuser");

        verify(userRepository).findById(1L);
        verify(userMapper).toDTO(user);
    }

    @Test
    @DisplayName("Should throw exception when user not found by ID")
    void shouldThrowExceptionWhenUserNotFoundById() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.getUserById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found with ID: 1");

        verify(userRepository).findById(1L);
        verify(userMapper, never()).toDTO(any(User.class));
    }

    @Test
    @DisplayName("Should get user by username successfully")
    void shouldGetUserByUsernameSuccessfully() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        // When
        UserDTO result = userService.getUserByUsername("testuser");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");

        verify(userRepository).findByUsername("testuser");
        verify(userMapper).toDTO(user);
    }

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUserSuccessfully() {
        // Given
        UserDTO.UpdateUserRequest updateRequest = new UserDTO.UpdateUserRequest();
        updateRequest.setFirstName("Updated");
        updateRequest.setLastName("User");
        updateRequest.setPhoneNumber("9876543210");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setFirstName("Updated");
        updatedUser.setLastName("User");

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId(1L);
        updatedUserDTO.setFirstName("Updated");
        updatedUserDTO.setLastName("User");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.toDTO(updatedUser)).thenReturn(updatedUserDTO);

        // When
        UserDTO result = userService.updateUser(1L, updateRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Updated");

        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
        verify(userMapper).toDTO(updatedUser);
    }

    @Test
    @DisplayName("Should change password successfully")
    void shouldChangePasswordSuccessfully() {
        // Given
        UserDTO.ChangePasswordRequest changePasswordRequest = new UserDTO.ChangePasswordRequest();
        changePasswordRequest.setCurrentPassword("oldPassword");
        changePasswordRequest.setNewPassword("newPassword");
        changePasswordRequest.setConfirmPassword("newPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword", user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        // When
        userService.changePassword(1L, changePasswordRequest);

        // Then
        verify(userRepository).findById(1L);
        verify(passwordEncoder).matches("oldPassword", user.getPassword());
        verify(passwordEncoder).encode("newPassword");
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Should throw exception for incorrect current password")
    void shouldThrowExceptionForIncorrectCurrentPassword() {
        // Given
        UserDTO.ChangePasswordRequest changePasswordRequest = new UserDTO.ChangePasswordRequest();
        changePasswordRequest.setCurrentPassword("wrongPassword");
        changePasswordRequest.setNewPassword("newPassword");
        changePasswordRequest.setConfirmPassword("newPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", user.getPassword())).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> userService.changePassword(1L, changePasswordRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Current password is incorrect");

        verify(userRepository).findById(1L);
        verify(passwordEncoder).matches("wrongPassword", user.getPassword());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception for mismatched new passwords")
    void shouldThrowExceptionForMismatchedNewPasswords() {
        // Given
        UserDTO.ChangePasswordRequest changePasswordRequest = new UserDTO.ChangePasswordRequest();
        changePasswordRequest.setCurrentPassword("oldPassword");
        changePasswordRequest.setNewPassword("newPassword1");
        changePasswordRequest.setConfirmPassword("newPassword2");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword", user.getPassword())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.changePassword(1L, changePasswordRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("New password and confirmation do not match");

        verify(userRepository).findById(1L);
        verify(passwordEncoder).matches("oldPassword", user.getPassword());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository).findById(1L);
        verify(userRepository).delete(user);
    }

    @Test
    @DisplayName("Should toggle user status successfully")
    void shouldToggleUserStatusSuccessfully() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        userService.toggleUserStatus(1L, false);

        // Then
        verify(userRepository).findById(1L);
        verify(userRepository).save(user);
        assertThat(user.getEnabled()).isFalse();
        assertThat(user.getStatus()).isEqualTo(User.UserStatus.INACTIVE);
    }

    @Test
    @DisplayName("Should verify email successfully")
    void shouldVerifyEmailSuccessfully() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        userService.verifyEmail(1L);

        // Then
        verify(userRepository).findById(1L);
        verify(userRepository).save(user);
        assertThat(user.getEmailVerified()).isTrue();
        assertThat(user.getStatus()).isEqualTo(User.UserStatus.ACTIVE);
    }
}