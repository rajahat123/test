package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.dto.AddressDTO;
import com.ecommerce.userservice.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {
    
    private final AddressService addressService;
    
    // API 10: Create new address
    @PostMapping
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
        AddressDTO address = addressService.createAddress(addressDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(address);
    }
    
    // API 11: Get address by ID
    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long id) {
        AddressDTO address = addressService.getAddressById(id);
        return ResponseEntity.ok(address);
    }
    
    // API 12: Get addresses by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AddressDTO>> getAddressesByUserId(@PathVariable Long userId) {
        List<AddressDTO> addresses = addressService.getAddressesByUserId(userId);
        return ResponseEntity.ok(addresses);
    }
    
    // API 13: Update address
    @PutMapping("/{id}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long id, @Valid @RequestBody AddressDTO addressDTO) {
        AddressDTO address = addressService.updateAddress(id, addressDTO);
        return ResponseEntity.ok(address);
    }
    
    // API 14: Delete address
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
    
    // API 15: Set default address
    @PatchMapping("/{addressId}/default")
    public ResponseEntity<AddressDTO> setDefaultAddress(@RequestParam Long userId, @PathVariable Long addressId) {
        AddressDTO address = addressService.setDefaultAddress(userId, addressId);
        return ResponseEntity.ok(address);
    }
}
