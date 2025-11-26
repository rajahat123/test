package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.dto.*;
import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.entity.UserStatus;
import com.ecommerce.userservice.service.UserService;
import com.ecommerce.userservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    private final JwtUtil jwtUtil;
    
    // API 1: Register new user
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        UserDTO user = userService.registerUser(registrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    
    // API 2: User login
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        User user = userService.authenticateUser(loginDTO);
        String token = jwtUtil.generateToken(user.getEmail());
        userService.updateLastLogin(user.getEmail());
        
        UserDTO userDTO = userService.getUserByEmail(user.getEmail());
        AuthResponseDTO response = new AuthResponseDTO(token, userDTO);
        return ResponseEntity.ok(response);
    }
    
    // API 3: Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    // API 4: Get user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        UserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }
    
    // API 5: Get all users
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    // API 6: Search users by name
    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String query) {
        List<UserDTO> users = userService.searchUsers(query);
        return ResponseEntity.ok(users);
    }
    
    // API 7: Update user
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }
    
    // API 8: Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    // API 9: Update user status
    @PatchMapping("/{id}/status")
    public ResponseEntity<UserDTO> updateUserStatus(@PathVariable Long id, @RequestParam UserStatus status) {
        UserDTO user = userService.updateUserStatus(id, status);
        return ResponseEntity.ok(user);
    }
}
