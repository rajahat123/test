package com.ecommerce.inventoryservice.service;

import com.ecommerce.inventoryservice.dto.InventoryDTO;
import com.ecommerce.inventoryservice.entity.Inventory;
import com.ecommerce.inventoryservice.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    private Inventory testInventory;
    private InventoryDTO inventoryDTO;

    @BeforeEach
    void setUp() {
        testInventory = new Inventory();
        testInventory.setId(1L);
        testInventory.setProductId(1L);
        testInventory.setWarehouseId("WH-001");
        testInventory.setQuantity(100);
        testInventory.setReservedQuantity(10);
        testInventory.setReorderLevel(20);
        testInventory.setReorderQuantity(50);
        testInventory.setCreatedAt(LocalDateTime.now());
        testInventory.setUpdatedAt(LocalDateTime.now());

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
    void testCreateInventory_Success() {
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(testInventory);

        InventoryDTO result = inventoryService.createInventory(inventoryDTO);

        assertNotNull(result);
        assertEquals(1L, result.getProductId());
        assertEquals(100, result.getQuantity());
        verify(inventoryRepository, times(1)).save(any(Inventory.class));
    }

    @Test
    void testGetInventoryById_Success() {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(testInventory));

        InventoryDTO result = inventoryService.getInventoryById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(100, result.getQuantity());
        verify(inventoryRepository, times(1)).findById(1L);
    }

    @Test
    void testGetInventoryById_NotFound() {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> inventoryService.getInventoryById(1L));
    }

    @Test
    void testGetInventoryByProductId_Success() {
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(testInventory));

        InventoryDTO result = inventoryService.getInventoryByProductId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getProductId());
        verify(inventoryRepository, times(1)).findByProductId(1L);
    }

    @Test
    void testUpdateInventory_Success() {
        InventoryDTO updateDTO = new InventoryDTO();
        updateDTO.setQuantity(150);
        updateDTO.setReorderLevel(30);

        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(testInventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(testInventory);

        InventoryDTO result = inventoryService.updateInventory(1L, updateDTO);

        assertNotNull(result);
        verify(inventoryRepository, times(1)).findById(1L);
        verify(inventoryRepository, times(1)).save(any(Inventory.class));
    }

    @Test
    void testDeleteInventory_Success() {
        when(inventoryRepository.existsById(1L)).thenReturn(true);
        doNothing().when(inventoryRepository).deleteById(1L);

        inventoryService.deleteInventory(1L);

        verify(inventoryRepository, times(1)).existsById(1L);
        verify(inventoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteInventory_NotFound() {
        when(inventoryRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> inventoryService.deleteInventory(1L));
        verify(inventoryRepository, never()).deleteById(anyLong());
    }

    @Test
    void testReserveStock_Success() {
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(testInventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(testInventory);

        InventoryDTO result = inventoryService.reserveStock(1L, 5);

        assertNotNull(result);
        verify(inventoryRepository, times(1)).findByProductId(1L);
        verify(inventoryRepository, times(1)).save(any(Inventory.class));
    }

    @Test
    void testReserveStock_InsufficientStock() {
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(testInventory));

        assertThrows(RuntimeException.class, () -> inventoryService.reserveStock(1L, 200));
        verify(inventoryRepository, never()).save(any(Inventory.class));
    }

    @Test
    void testReleaseStock_Success() {
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(testInventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(testInventory);

        InventoryDTO result = inventoryService.releaseStock(1L, 5);

        assertNotNull(result);
        verify(inventoryRepository, times(1)).findByProductId(1L);
        verify(inventoryRepository, times(1)).save(any(Inventory.class));
    }

    @Test
    void testDeductStock_Success() {
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(testInventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(testInventory);

        InventoryDTO result = inventoryService.deductStock(1L, 5);

        assertNotNull(result);
        verify(inventoryRepository, times(1)).findByProductId(1L);
        verify(inventoryRepository, times(1)).save(any(Inventory.class));
    }

    @Test
    void testAddStock_Success() {
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(testInventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(testInventory);

        InventoryDTO result = inventoryService.addStock(1L, 50);

        assertNotNull(result);
        verify(inventoryRepository, times(1)).findByProductId(1L);
        verify(inventoryRepository, times(1)).save(any(Inventory.class));
    }

    @Test
    void testGetLowStockItems() {
        when(inventoryRepository.findLowStockItems()).thenReturn(Arrays.asList(testInventory));

        List<InventoryDTO> results = inventoryService.getLowStockItems();

        assertNotNull(results);
        assertEquals(1, results.size());
        verify(inventoryRepository, times(1)).findLowStockItems();
    }

    @Test
    void testGetInventoryByWarehouse() {
        when(inventoryRepository.findByWarehouseId("WH-001")).thenReturn(Arrays.asList(testInventory));

        List<InventoryDTO> results = inventoryService.getInventoryByWarehouse("WH-001");

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("WH-001", results.get(0).getWarehouseId());
        verify(inventoryRepository, times(1)).findByWarehouseId("WH-001");
    }
}
