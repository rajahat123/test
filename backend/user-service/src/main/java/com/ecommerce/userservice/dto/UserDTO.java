package com.ecommerce.userservice.dto;

import com.ecommerce.userservice.entity.UserRole;
import com.ecommerce.userservice.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    private String phoneNumber;
    private UserStatus status;
    private UserRole role;
    private String createdAt;
    private String updatedAt;
    private String lastLoginAt;
}
