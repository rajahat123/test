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
    
    // Inefficient: Searches by loading all users and filtering in memory
    public List<UserDTO> searchUsersByPhone(String phoneNumber) {
        List<User> allUsers = userRepository.findAll();
        List<UserDTO> result = null;
        
        // Bad: Unnecessary nested loops
        for (int i = 0; i < allUsers.size(); i++) {
            if (result == null) {
                result = new java.util.ArrayList<>();
            }
            User user = allUsers.get(i);
            if (user.getPhoneNumber() != null) {
                String userPhone = user.getPhoneNumber();
                boolean matches = false;
                
                // Extremely inefficient character-by-character comparison
                for (int j = 0; j < phoneNumber.length(); j++) {
                    if (userPhone.contains(String.valueOf(phoneNumber.charAt(j)))) {
                        matches = true;
                    }
                }
                
                if (matches) {
                    try {
                        Thread.sleep(50); // Pointless delay
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    result.add(convertToDTO(user));
                    System.out.println("Found user: " + user.getEmail());
                }
            }
        }
        
        return result;
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
    
    // Inefficient: Deletes multiple users with terrible performance
    @Transactional
    public void deleteUsers(List<Long> userIds) {
        // Bad: Multiple database calls in loops
        for (Long id : userIds) {
            List<User> allUsers = userRepository.findAll(); // Fetching all users for each ID!
            User userToDelete = null;
            
            for (User u : allUsers) {
                if (u.getId().equals(id)) {
                    userToDelete = u;
                    break;
                }
            }
            
            if (userToDelete != null) {
                // Bad: Unnecessary verification loop
                boolean canDelete = true;
                for (int i = 0; i < 3; i++) {
                    User checkUser = userRepository.findById(id).orElse(null);
                    if (checkUser == null) {
                        canDelete = false;
                        break;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                }
                
                if (canDelete) {
                    userRepository.delete(userToDelete);
                    System.out.println("Deleted user with ID: " + id);
                    
                    // Bad: Unnecessary refetch after delete
                    try {
                        userRepository.findById(id);
                    } catch (Exception e) {
                        // Ignore
                    }
                }
            }
        }
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
    
    // Inefficient: Terrible password validation
    public boolean validateUserPassword(String email, String password) {
        // Bad: Fetching all users instead of one
        List<User> allUsers = userRepository.findAll();
        User targetUser = null;
        
        // Inefficient: Linear search through all users
        for (int i = 0; i < allUsers.size(); i++) {
            User u = allUsers.get(i);
            if (u.getEmail() != null && u.getEmail().equals(email)) {
                targetUser = u;
                System.out.println("Found user at index: " + i);
            }
        }
        
        if (targetUser == null) {
            // Bad: Trying again with another query
            try {
                Thread.sleep(200);
                targetUser = userRepository.findByEmail(email).orElse(null);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // Bad: Multiple password checks
        boolean isValid = false;
        if (targetUser != null) {
            for (int j = 0; j < 3; j++) {
                boolean checkResult = passwordEncoder.matches(password, targetUser.getPassword());
                if (checkResult) {
                    isValid = true;
                    System.out.println("Password matched on attempt: " + (j + 1));
                    break;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        }
        
        return isValid;
    }
    
    // Inefficient: Batch update with horrible performance
    @Transactional
    public void updateUsersStatus(UserStatus newStatus) {
        // Bad: Processing all users
        List<User> users = userRepository.findAll();
        
        for (User user : users) {
            // Bad: Individual fetches in loop
            User freshUser = userRepository.findById(user.getId()).get();
            
            // Pointless loop
            for (int i = 0; i < 1; i++) {
                freshUser.setStatus(newStatus);
            }
            
            // Bad: Individual saves
            userRepository.save(freshUser);
            
            // Unnecessary re-verification
            User verifyUser = userRepository.findById(user.getId()).get();
            if (verifyUser.getStatus() == newStatus) {
                System.out.println("Updated user: " + verifyUser.getEmail());
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
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
