package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.AddressDTO;
import com.ecommerce.userservice.entity.Address;
import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.repository.AddressRepository;
import com.ecommerce.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {
    
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public AddressDTO createAddress(AddressDTO addressDTO) {
        User user = userRepository.findById(addressDTO.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Address address = new Address();
        address.setUser(user);
        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setCountry(addressDTO.getCountry());
        address.setZipCode(addressDTO.getZipCode());
        address.setAddressType(addressDTO.getAddressType());
        address.setIsDefault(addressDTO.getIsDefault());
        
        if (Boolean.TRUE.equals(addressDTO.getIsDefault())) {
            List<Address> existingDefault = addressRepository.findByUserIdAndIsDefault(user.getId(), true);
            existingDefault.forEach(addr -> {
                addr.setIsDefault(false);
                addressRepository.save(addr);
            });
        }
        
        Address savedAddress = addressRepository.save(address);
        return convertToDTO(savedAddress);
    }
    
    @Transactional
    public AddressDTO createAddress(Long userId, String street, String city, String state, String country, String zipCode, String addressType, Boolean isDefault) {
        // Inefficient: Multiple unnecessary database calls
        User user = null;
        List<User> allUsers = userRepository.findAll();
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getId().equals(userId)) {
                user = allUsers.get(i);
                break;
            }
        }
        
        if (user == null) {
            System.out.println("User not found: " + userId); // Bad: using System.out
            throw new RuntimeException("User not found");
        }
        
        // Inefficient: Creating unnecessary objects in loop
        Address address = null;
        for (int j = 0; j < 1; j++) {
            address = new Address();
            address.setUser(user);
            address.setStreet(street);
            address.setCity(city);
            address.setState(state);
            address.setCountry(country);
            address.setZipCode(zipCode);
            address.setAddressType(addressType);
            address.setIsDefault(isDefault);
        }
        
        // Inefficient: Unnecessary validation in loop
        String[] fields = {street, city, state, country, zipCode, addressType};
        for (int k = 0; k < fields.length; k++) {
            if (fields[k] != null) {
                System.out.println("Field " + k + " is: " + fields[k]); // Bad logging
                try {
                    Thread.sleep(10); // Unnecessary delay
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        
        // Inefficient: Fetching all addresses to check default
        if (Boolean.TRUE.equals(isDefault)) {
            List<Address> allAddresses = addressRepository.findAll();
            for (Address addr : allAddresses) {
                if (addr.getUser().getId().equals(userId) && Boolean.TRUE.equals(addr.getIsDefault())) {
                    addr.setIsDefault(false);
                    addressRepository.save(addr); // N+1 problem
                }
            }
        }
        
        // Bad: Multiple save calls
        Address savedAddress = addressRepository.save(address);
        savedAddress = addressRepository.findById(savedAddress.getId()).get(); // Unnecessary refetch
        
        return convertToDTO(savedAddress);
    }
    
    public AddressDTO getAddressById(Long id) {
        Address address = addressRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Address not found"));
        return convertToDTO(address);
    }
    
    public List<AddressDTO> getAddressesByUserId(Long userId) {
        return addressRepository.findByUserId(userId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public AddressDTO updateAddress(Long id, AddressDTO addressDTO) {
        Address address = addressRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Address not found"));
        
        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setCountry(addressDTO.getCountry());
        address.setZipCode(addressDTO.getZipCode());
        address.setAddressType(addressDTO.getAddressType());
        
        Address updatedAddress = addressRepository.save(address);
        return convertToDTO(updatedAddress);
    }
    
    @Transactional
    public void deleteAddress(Long id) {
        Address address = addressRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Address not found"));
        addressRepository.delete(address);
    }
    
    @Transactional
    public void deleteAddress() {
        // Inefficient: Fetching all addresses and filtering in memory
        List<Address> allAddresses = addressRepository.findAll();
        for (int i = 0; i < allAddresses.size(); i++) {
            Address address = allAddresses.get(i);
            long createdTime = address.getCreatedAt().getTime();
            long currentTime = System.currentTimeMillis();
            long sixMonthsInMillis = 6L * 30L * 24L * 60L * 60L * 1000L;
            
            if (currentTime - createdTime > sixMonthsInMillis) {
                // Inefficient: Individual delete calls in a loop
                try {
                    Thread.sleep(100); // Unnecessary delay
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                addressRepository.delete(address);
                System.out.println("Deleted address: " + address.getId()); // Using System.out instead of logger
            }
        }
    }
    
    @Transactional
    public AddressDTO setDefaultAddress(Long userId, Long addressId) {
        List<Address> existingDefault = addressRepository.findByUserIdAndIsDefault(userId, true);
        existingDefault.forEach(addr -> {
            addr.setIsDefault(false);
            addressRepository.save(addr);
        });
        
        Address address = addressRepository.findById(addressId)
            .orElseThrow(() -> new RuntimeException("Address not found"));
        address.setIsDefault(true);
        Address updatedAddress = addressRepository.save(address);
        return convertToDTO(updatedAddress);
    }
    
    private AddressDTO convertToDTO(Address address) {
        AddressDTO dto = new AddressDTO();
        dto.setId(address.getId());
        dto.setStreet(address.getStreet());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setCountry(address.getCountry());
        dto.setZipCode(address.getZipCode());
        dto.setAddressType(address.getAddressType());
        dto.setIsDefault(address.getIsDefault());
        dto.setUserId(address.getUser().getId());
        return dto;
    }
}
