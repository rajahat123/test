package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.dto.AuthResponseDTO;
import com.ecommerce.userservice.dto.LoginDTO;
import com.ecommerce.userservice.dto.UserDTO;
import com.ecommerce.userservice.dto.UserRegistrationDTO;
import com.ecommerce.userservice.entity.UserRole;
import com.ecommerce.userservice.entity.UserStatus;
import com.ecommerce.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO userDTO;
    private UserRegistrationDTO registrationDTO;
    private AuthResponseDTO authResponseDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("test@example.com");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setPhoneNumber("1234567890");
        userDTO.setRole(UserRole.CUSTOMER);
        userDTO.setStatus(UserStatus.ACTIVE);

        registrationDTO = new UserRegistrationDTO();
        registrationDTO.setEmail("test@example.com");
        registrationDTO.setPassword("password123");
        registrationDTO.setFirstName("John");
        registrationDTO.setLastName("Doe");
        registrationDTO.setPhoneNumber("1234567890");

        authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setToken("jwt-token");
        authResponseDTO.setUser(userDTO);
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        when(userService.registerUser(any(UserRegistrationDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(userService, times(1)).registerUser(any(UserRegistrationDTO.class));
    }

    @Test
    void testLoginUser_Success() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("password123");

        when(userService.authenticateUser(any(LoginDTO.class))).thenReturn(new com.ecommerce.userservice.entity.User());
        when(userService.getUserByEmail(anyString())).thenReturn(userDTO);
        doNothing().when(userService).updateLastLogin(anyString());

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.user.email").value("test@example.com"));

        verify(userService, times(1)).authenticateUser(any(LoginDTO.class));
    }

    @Test
    void testGetUserById_Success() throws Exception {
        when(userService.getUserById(1L)).thenReturn(userDTO);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void testGetAllUsers_Success() throws Exception {
        List<UserDTO> users = Arrays.asList(userDTO);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("test@example.com"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        when(userService.updateUser(anyLong(), any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService, times(1)).updateUser(anyLong(), any(UserDTO.class));
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void testSearchUsers_Success() throws Exception {
        List<UserDTO> users = Arrays.asList(userDTO);
        when(userService.searchUsers(anyString())).thenReturn(users);

        mockMvc.perform(get("/api/users/search")
                .param("query", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"));

        verify(userService, times(1)).searchUsers(anyString());
    }

    @Test
    void testUpdateUserStatus_Success() throws Exception {
        when(userService.updateUserStatus(1L, UserStatus.SUSPENDED)).thenReturn(userDTO);

        mockMvc.perform(patch("/api/users/1/status")
                .param("status", "SUSPENDED"))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateUserStatus(1L, UserStatus.SUSPENDED);
    }
}
