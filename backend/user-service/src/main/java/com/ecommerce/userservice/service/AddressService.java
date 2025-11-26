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
