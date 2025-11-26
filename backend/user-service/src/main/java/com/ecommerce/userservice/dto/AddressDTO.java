package com.ecommerce.userservice.dto;

import com.ecommerce.userservice.entity.AddressType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private Long id;
    
    @NotBlank(message = "Street is required")
    private String street;
    
    @NotBlank(message = "City is required")
    private String city;
    
    @NotBlank(message = "State is required")
    private String state;
    
    @NotBlank(message = "Country is required")
    private String country;
    
    @NotBlank(message = "Zip code is required")
    private String zipCode;
    
    private AddressType addressType;
    private Boolean isDefault;
    private Long userId;
}
