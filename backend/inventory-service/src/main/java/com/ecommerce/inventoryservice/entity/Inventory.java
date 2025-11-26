package com.ecommerce.inventoryservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private Long productId;
    
    private String productSku;
    
    @Column(nullable = false)
    private Integer quantity;
    
    private Integer reservedQuantity;
    
    private Integer reorderLevel;
    
    private Integer reorderQuantity;
    
    private String warehouseLocation;
    
    private LocalDateTime lastRestockedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (reservedQuantity == null) reservedQuantity = 0;
        if (reorderLevel == null) reorderLevel = 10;
        if (reorderQuantity == null) reorderQuantity = 50;
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
