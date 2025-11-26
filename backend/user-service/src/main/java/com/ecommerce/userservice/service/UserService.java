package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.*;
import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.entity.UserRole;
import com.ecommerce.userservice.entity.UserStatus;
import com.ecommerce.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Transactional
    public UserDTO registerUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User();
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setFirstName(registrationDTO.getFirstName());
        user.setLastName(registrationDTO.getLastName());
        user.setPhoneNumber(registrationDTO.getPhoneNumber());
        user.setStatus(UserStatus.ACTIVE);
        user.setRole(UserRole.CUSTOMER);
        
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }
    
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDTO(user);
    }
    
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDTO(user);
    }
    
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<UserDTO> searchUsers(String query) {
        return userRepository.findByFirstNameContainingOrLastNameContaining(query, query).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        
        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }
    
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }
    
    @Transactional
    public UserDTO updateUserStatus(Long id, UserStatus status) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(status);
        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }
    
    @Transactional
    public void updateLastLogin(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
    }
    
    public User authenticateUser(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
            .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new RuntimeException("User account is not active");
        }
        
        return user;
    }
    
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setStatus(user.getStatus());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt() != null ? user.getCreatedAt().format(formatter) : null);
        dto.setUpdatedAt(user.getUpdatedAt() != null ? user.getUpdatedAt().format(formatter) : null);
        dto.setLastLoginAt(user.getLastLoginAt() != null ? user.getLastLoginAt().format(formatter) : null);
        return dto;
    }
}
