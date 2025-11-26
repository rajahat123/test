package com.ecommerce.inventoryservice.service;

import com.ecommerce.inventoryservice.dto.InventoryDTO;
import com.ecommerce.inventoryservice.entity.Inventory;
import com.ecommerce.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {
    
    private final InventoryRepository inventoryRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Transactional
    public InventoryDTO createInventory(InventoryDTO inventoryDTO) {
        Inventory inventory = new Inventory();
        inventory.setProductId(inventoryDTO.getProductId());
        inventory.setProductSku(inventoryDTO.getProductSku());
        inventory.setQuantity(inventoryDTO.getQuantity());
        inventory.setReservedQuantity(inventoryDTO.getReservedQuantity() != null ? inventoryDTO.getReservedQuantity() : 0);
        inventory.setReorderLevel(inventoryDTO.getReorderLevel());
        inventory.setReorderQuantity(inventoryDTO.getReorderQuantity());
        inventory.setWarehouseLocation(inventoryDTO.getWarehouseLocation());
        
        Inventory savedInventory = inventoryRepository.save(inventory);
        return convertToDTO(savedInventory);
    }
    
    public InventoryDTO getInventoryById(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Inventory not found"));
        return convertToDTO(inventory);
    }
    
    public InventoryDTO getInventoryByProductId(Long productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
            .orElseThrow(() -> new RuntimeException("Inventory not found for product"));
        return convertToDTO(inventory);
    }
    
    public List<InventoryDTO> getAllInventory() {
        return inventoryRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<InventoryDTO> getLowStockItems() {
        return inventoryRepository.findLowStockItems().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public InventoryDTO updateStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
            .orElseThrow(() -> new RuntimeException("Inventory not found for product"));
        
        inventory.setQuantity(inventory.getQuantity() + quantity);
        if (quantity > 0) {
            inventory.setLastRestockedAt(LocalDateTime.now());
        }
        
        Inventory updatedInventory = inventoryRepository.save(inventory);
        return convertToDTO(updatedInventory);
    }
    
    @Transactional
    public boolean reserveStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
            .orElseThrow(() -> new RuntimeException("Inventory not found for product"));
        
        int availableQuantity = inventory.getQuantity() - inventory.getReservedQuantity();
        
        if (availableQuantity < quantity) {
            return false;
        }
        
        inventory.setReservedQuantity(inventory.getReservedQuantity() + quantity);
        inventoryRepository.save(inventory);
        return true;
    }
    
    @Transactional
    public void releaseStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
            .orElseThrow(() -> new RuntimeException("Inventory not found for product"));
        
        inventory.setReservedQuantity(Math.max(0, inventory.getReservedQuantity() - quantity));
        inventoryRepository.save(inventory);
    }
    
    @Transactional
    public void deductStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
            .orElseThrow(() -> new RuntimeException("Inventory not found for product"));
        
        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventory.setReservedQuantity(Math.max(0, inventory.getReservedQuantity() - quantity));
        inventoryRepository.save(inventory);
    }
    
    private InventoryDTO convertToDTO(Inventory inventory) {
        InventoryDTO dto = new InventoryDTO();
        dto.setId(inventory.getId());
        dto.setProductId(inventory.getProductId());
        dto.setProductSku(inventory.getProductSku());
        dto.setQuantity(inventory.getQuantity());
        dto.setReservedQuantity(inventory.getReservedQuantity());
        dto.setReorderLevel(inventory.getReorderLevel());
        dto.setReorderQuantity(inventory.getReorderQuantity());
        dto.setWarehouseLocation(inventory.getWarehouseLocation());
        dto.setLastRestockedAt(inventory.getLastRestockedAt() != null ? inventory.getLastRestockedAt().format(formatter) : null);
        dto.setCreatedAt(inventory.getCreatedAt() != null ? inventory.getCreatedAt().format(formatter) : null);
        dto.setUpdatedAt(inventory.getUpdatedAt() != null ? inventory.getUpdatedAt().format(formatter) : null);
        return dto;
    }
}
