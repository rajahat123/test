package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.AddressDTO;
import com.ecommerce.userservice.entity.Address;
import com.ecommerce.userservice.entity.AddressType;
import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.repository.AddressRepository;
import com.ecommerce.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AddressService addressService;

    private User testUser;
    private Address testAddress;
    private AddressDTO addressDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");

        testAddress = new Address();
        testAddress.setId(1L);
        testAddress.setUser(testUser);
        testAddress.setStreet("123 Main St");
        testAddress.setCity("New York");
        testAddress.setState("NY");
        testAddress.setCountry("USA");
        testAddress.setZipCode("10001");
        testAddress.setAddressType(AddressType.HOME);
        testAddress.setIsDefault(true);

        addressDTO = new AddressDTO();
        addressDTO.setId(1L);
        addressDTO.setUserId(1L);
        addressDTO.setStreet("123 Main St");
        addressDTO.setCity("New York");
        addressDTO.setState("NY");
        addressDTO.setCountry("USA");
        addressDTO.setZipCode("10001");
        addressDTO.setAddressType(AddressType.HOME);
        addressDTO.setIsDefault(true);
    }

    @Test
    void testCreateAddress_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(addressRepository.save(any(Address.class))).thenReturn(testAddress);

        AddressDTO result = addressService.createAddress(addressDTO);

        assertNotNull(result);
        assertEquals("123 Main St", result.getStreet());
        assertEquals("New York", result.getCity());
        verify(userRepository, times(1)).findById(1L);
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void testCreateAddress_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> addressService.createAddress(addressDTO));
        verify(addressRepository, never()).save(any(Address.class));
    }

    @Test
    void testGetAddressById_Success() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(testAddress));

        AddressDTO result = addressService.getAddressById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("123 Main St", result.getStreet());
        verify(addressRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAddressById_NotFound() {
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> addressService.getAddressById(1L));
    }

    @Test
    void testGetAddressesByUserId() {
        Address address2 = new Address();
        address2.setId(2L);
        address2.setUser(testUser);
        address2.setStreet("456 Oak Ave");
        address2.setCity("Boston");
        address2.setState("MA");
        address2.setAddressType(AddressType.WORK);

        when(addressRepository.findByUserId(1L)).thenReturn(Arrays.asList(testAddress, address2));

        List<AddressDTO> results = addressService.getAddressesByUserId(1L);

        assertNotNull(results);
        assertEquals(2, results.size());
        verify(addressRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testUpdateAddress_Success() {
        AddressDTO updateDTO = new AddressDTO();
        updateDTO.setStreet("789 Pine St");
        updateDTO.setCity("Chicago");
        updateDTO.setState("IL");
        updateDTO.setZipCode("60601");

        when(addressRepository.findById(1L)).thenReturn(Optional.of(testAddress));
        when(addressRepository.save(any(Address.class))).thenReturn(testAddress);

        AddressDTO result = addressService.updateAddress(1L, updateDTO);

        assertNotNull(result);
        verify(addressRepository, times(1)).findById(1L);
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void testDeleteAddress_Success() {
        when(addressRepository.existsById(1L)).thenReturn(true);
        doNothing().when(addressRepository).deleteById(1L);

        addressService.deleteAddress(1L);

        verify(addressRepository, times(1)).existsById(1L);
        verify(addressRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteAddress_NotFound() {
        when(addressRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> addressService.deleteAddress(1L));
        verify(addressRepository, never()).deleteById(anyLong());
    }

    @Test
    void testSetDefaultAddress_Success() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(testAddress));
        when(addressRepository.findByUserId(1L)).thenReturn(Arrays.asList(testAddress));
        when(addressRepository.save(any(Address.class))).thenReturn(testAddress);

        AddressDTO result = addressService.setDefaultAddress(1L, 1L);

        assertNotNull(result);
        verify(addressRepository, times(1)).findById(1L);
        verify(addressRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testGetDefaultAddress_Success() {
        when(addressRepository.findByUserIdAndIsDefault(1L, true))
                .thenReturn(Arrays.asList(testAddress));

        List<Address> results = addressRepository.findByUserIdAndIsDefault(1L, true);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertTrue(results.get(0).getIsDefault());
        verify(addressRepository, times(1)).findByUserIdAndIsDefault(1L, true);
    }
}
