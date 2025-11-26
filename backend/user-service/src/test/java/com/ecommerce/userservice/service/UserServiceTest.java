package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.AuthResponseDTO;
import com.ecommerce.userservice.dto.LoginDTO;
import com.ecommerce.userservice.dto.UserDTO;
import com.ecommerce.userservice.dto.UserRegistrationDTO;
import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.entity.UserRole;
import com.ecommerce.userservice.entity.UserStatus;
import com.ecommerce.userservice.repository.UserRepository;
import com.ecommerce.userservice.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserRegistrationDTO registrationDTO;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setPhoneNumber("1234567890");
        testUser.setRole(UserRole.CUSTOMER);
        testUser.setStatus(UserStatus.ACTIVE);
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());

        registrationDTO = new UserRegistrationDTO();
        registrationDTO.setEmail("test@example.com");
        registrationDTO.setPassword("password123");
        registrationDTO.setFirstName("John");
        registrationDTO.setLastName("Doe");
        registrationDTO.setPhoneNumber("1234567890");

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("test@example.com");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setPhoneNumber("1234567890");
        userDTO.setRole(UserRole.CUSTOMER);
        userDTO.setStatus(UserStatus.ACTIVE);
    }

    @Test
    void testRegisterUser_Success() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserDTO result = userService.registerUser(registrationDTO);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        assertEquals("John", result.getFirstName());
        verify(userRepository, times(1)).existsByEmail("test@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.registerUser(registrationDTO));
        verify(userRepository, times(1)).existsByEmail("test@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testAuthenticateUser_Success() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("password123");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        User result = userService.authenticateUser(loginDTO);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testAuthenticateUser_InvalidCredentials() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("wrongPassword");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.authenticateUser(loginDTO));
    }

    @Test
    void testAuthenticateUser_UserNotFound() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("nonexistent@example.com");
        loginDTO.setPassword("password123");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.authenticateUser(loginDTO));
    }

    @Test
    void testGetUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        UserDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserById(1L));
    }

    @Test
    void testGetAllUsers() {
        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");
        user2.setFirstName("Jane");
        user2.setLastName("Smith");

        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser, user2));

        List<UserDTO> results = userService.getAllUsers();

        assertNotNull(results);
        assertEquals(2, results.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testUpdateUser_Success() {
        UserDTO updateDTO = new UserDTO();
        updateDTO.setFirstName("UpdatedJohn");
        updateDTO.setLastName("UpdatedDoe");
        updateDTO.setPhoneNumber("9876543210");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserDTO result = userService.updateUser(1L, updateDTO);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void testSearchUsers() {
        when(userRepository.findByFirstNameContainingOrLastNameContaining(anyString(), anyString()))
                .thenReturn(Arrays.asList(testUser));

        List<UserDTO> results = userService.searchUsers("John");

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("John", results.get(0).getFirstName());
        verify(userRepository, times(1)).findByFirstNameContainingOrLastNameContaining(anyString(), anyString());
    }

    @Test
    void testUpdateUserStatus_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserDTO result = userService.updateUserStatus(1L, UserStatus.SUSPENDED);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }
}
