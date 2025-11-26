package com.ecommerce.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDTO {
    private Long id;
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    private String productSku;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;
    
    private Integer reservedQuantity;
    private Integer reorderLevel;
    private Integer reorderQuantity;
    private String warehouseLocation;
    private String lastRestockedAt;
    private String createdAt;
    private String updatedAt;
}
