package com.ecommerce.inventoryservice.controller;

import com.ecommerce.inventoryservice.dto.InventoryDTO;
import com.ecommerce.inventoryservice.service.InventoryService;
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

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private InventoryDTO inventoryDTO;

    @BeforeEach
    void setUp() {
        inventoryDTO = new InventoryDTO();
        inventoryDTO.setId(1L);
        inventoryDTO.setProductId(1L);
        inventoryDTO.setWarehouseId("WH-001");
        inventoryDTO.setQuantity(100);
        inventoryDTO.setReservedQuantity(10);
        inventoryDTO.setReorderLevel(20);
        inventoryDTO.setReorderQuantity(50);
    }

    @Test
    void testCreateInventory_Success() throws Exception {
        when(inventoryService.createInventory(any(InventoryDTO.class))).thenReturn(inventoryDTO);

        mockMvc.perform(post("/api/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inventoryDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.quantity").value(100));

        verify(inventoryService, times(1)).createInventory(any(InventoryDTO.class));
    }

    @Test
    void testGetInventoryById_Success() throws Exception {
        when(inventoryService.getInventoryById(1L)).thenReturn(inventoryDTO);

        mockMvc.perform(get("/api/inventory/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.quantity").value(100));

        verify(inventoryService, times(1)).getInventoryById(1L);
    }

    @Test
    void testGetInventoryByProductId_Success() throws Exception {
        when(inventoryService.getInventoryByProductId(1L)).thenReturn(inventoryDTO);

        mockMvc.perform(get("/api/inventory/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1));

        verify(inventoryService, times(1)).getInventoryByProductId(1L);
    }

    @Test
    void testUpdateInventory_Success() throws Exception {
        when(inventoryService.updateInventory(anyLong(), any(InventoryDTO.class))).thenReturn(inventoryDTO);

        mockMvc.perform(put("/api/inventory/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inventoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(100));

        verify(inventoryService, times(1)).updateInventory(anyLong(), any(InventoryDTO.class));
    }

    @Test
    void testDeleteInventory_Success() throws Exception {
        doNothing().when(inventoryService).deleteInventory(1L);

        mockMvc.perform(delete("/api/inventory/1"))
                .andExpect(status().isNoContent());

        verify(inventoryService, times(1)).deleteInventory(1L);
    }

    @Test
    void testReserveStock_Success() throws Exception {
        when(inventoryService.reserveStock(1L, 5)).thenReturn(inventoryDTO);

        mockMvc.perform(post("/api/inventory/product/1/reserve")
                .param("quantity", "5"))
                .andExpect(status().isOk());

        verify(inventoryService, times(1)).reserveStock(1L, 5);
    }

    @Test
    void testGetLowStockItems_Success() throws Exception {
        List<InventoryDTO> lowStockItems = Arrays.asList(inventoryDTO);
        when(inventoryService.getLowStockItems()).thenReturn(lowStockItems);

        mockMvc.perform(get("/api/inventory/low-stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].quantity").value(100));

        verify(inventoryService, times(1)).getLowStockItems();
    }
}
