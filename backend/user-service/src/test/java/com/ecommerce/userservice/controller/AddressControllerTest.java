package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.dto.AddressDTO;
import com.ecommerce.userservice.entity.AddressType;
import com.ecommerce.userservice.service.AddressService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AddressController.class)
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    @Autowired
    private ObjectMapper objectMapper;

    private AddressDTO addressDTO;

    @BeforeEach
    void setUp() {
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
    void testCreateAddress_Success() throws Exception {
        when(addressService.createAddress(any(AddressDTO.class))).thenReturn(addressDTO);

        mockMvc.perform(post("/api/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.street").value("123 Main St"))
                .andExpect(jsonPath("$.city").value("New York"));

        verify(addressService, times(1)).createAddress(any(AddressDTO.class));
    }

    @Test
    void testGetAddressById_Success() throws Exception {
        when(addressService.getAddressById(1L)).thenReturn(addressDTO);

        mockMvc.perform(get("/api/addresses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.city").value("New York"));

        verify(addressService, times(1)).getAddressById(1L);
    }

    @Test
    void testGetAddressesByUserId_Success() throws Exception {
        List<AddressDTO> addresses = Arrays.asList(addressDTO);
        when(addressService.getAddressesByUserId(1L)).thenReturn(addresses);

        mockMvc.perform(get("/api/addresses/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city").value("New York"));

        verify(addressService, times(1)).getAddressesByUserId(1L);
    }

    @Test
    void testUpdateAddress_Success() throws Exception {
        when(addressService.updateAddress(anyLong(), any(AddressDTO.class))).thenReturn(addressDTO);

        mockMvc.perform(put("/api/addresses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("New York"));

        verify(addressService, times(1)).updateAddress(anyLong(), any(AddressDTO.class));
    }

    @Test
    void testDeleteAddress_Success() throws Exception {
        doNothing().when(addressService).deleteAddress(1L);

        mockMvc.perform(delete("/api/addresses/1"))
                .andExpect(status().isNoContent());

        verify(addressService, times(1)).deleteAddress(1L);
    }

    @Test
    void testSetDefaultAddress_Success() throws Exception {
        when(addressService.setDefaultAddress(anyLong(), anyLong())).thenReturn(addressDTO);

        mockMvc.perform(patch("/api/addresses/1/default")
                .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isDefault").value(true));

        verify(addressService, times(1)).setDefaultAddress(anyLong(), anyLong());
    }
}
