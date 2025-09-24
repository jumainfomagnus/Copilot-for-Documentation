package com.enterprise.ecommerce.controller;

import com.enterprise.ecommerce.dto.UserDTO;
import com.enterprise.ecommerce.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for UserController.
 */
@WebMvcTest(UserController.class)
@DisplayName("User Controller Tests")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO userDTO;
    private UserDTO.CreateUserRequest createUserRequest;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testuser");
        userDTO.setEmail("test@example.com");
        userDTO.setFirstName("Test");
        userDTO.setLastName("User");
        userDTO.setPhoneNumber("1234567890");
        userDTO.setEnabled(true);
        userDTO.setEmailVerified(true);
        userDTO.setCreatedDate(LocalDateTime.now());

        createUserRequest = new UserDTO.CreateUserRequest();
        createUserRequest.setUsername("testuser");
        createUserRequest.setEmail("test@example.com");
        createUserRequest.setPassword("password123");
        createUserRequest.setFirstName("Test");
        createUserRequest.setLastName("User");
        createUserRequest.setPhoneNumber("1234567890");
    }

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() throws Exception {
        // Given
        when(userService.createUser(any(UserDTO.CreateUserRequest.class))).thenReturn(userDTO);

        // When & Then
        mockMvc.perform(post("/api/v1/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @DisplayName("Should return validation error for invalid user data")
    void shouldReturnValidationErrorForInvalidUserData() throws Exception {
        // Given
        createUserRequest.setUsername(""); // Invalid username
        createUserRequest.setEmail("invalid-email"); // Invalid email

        // When & Then
        mockMvc.perform(post("/api/v1/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should get user by ID successfully")
    @WithMockUser(roles = "ADMIN")
    void shouldGetUserByIdSuccessfully() throws Exception {
        // Given
        when(userService.getUserById(1L)).thenReturn(userDTO);

        // When & Then
        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpected(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @DisplayName("Should get all users successfully")
    @WithMockUser(roles = "ADMIN")
    void shouldGetAllUsersSuccessfully() throws Exception {
        // Given
        Page<UserDTO> userPage = new PageImpl<>(List.of(userDTO), PageRequest.of(0, 10), 1);
        when(userService.getAllUsers(any(PageRequest.class))).thenReturn(userPage);

        // When & Then
        mockMvc.perform(get("/api/v1/users")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("Should search users successfully")
    @WithMockUser(roles = "ADMIN")
    void shouldSearchUsersSuccessfully() throws Exception {
        // Given
        Page<UserDTO> userPage = new PageImpl<>(List.of(userDTO), PageRequest.of(0, 10), 1);
        when(userService.searchUsers(eq("test"), any(PageRequest.class))).thenReturn(userPage);

        // When & Then
        mockMvc.perform(get("/api/v1/users/search")
                .param("search", "test")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].username").value("testuser"));
    }

    @Test
    @DisplayName("Should update user successfully")
    @WithMockUser(username = "testuser", roles = "USER")
    void shouldUpdateUserSuccessfully() throws Exception {
        // Given
        UserDTO.UpdateUserRequest updateRequest = new UserDTO.UpdateUserRequest();
        updateRequest.setFirstName("Updated");
        updateRequest.setLastName("User");
        updateRequest.setPhoneNumber("9876543210");

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId(1L);
        updatedUserDTO.setFirstName("Updated");
        updatedUserDTO.setLastName("User");

        when(userService.getUserById(1L)).thenReturn(userDTO);
        when(userService.updateUser(eq(1L), any(UserDTO.UpdateUserRequest.class))).thenReturn(updatedUserDTO);

        // When & Then
        mockMvc.perform(put("/api/v1/users/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"));
    }

    @Test
    @DisplayName("Should delete user successfully")
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteUserSuccessfully() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/v1/users/1")
                .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should toggle user status successfully")
    @WithMockUser(roles = "ADMIN")
    void shouldToggleUserStatusSuccessfully() throws Exception {
        // When & Then
        mockMvc.perform(put("/api/v1/users/1/status")
                .with(csrf())
                .param("enabled", "false"))
                .andExpect(status().isOk());
    }
}