package com.ecommerce.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Long id;
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    private String productName;
    private String productSku;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
    
    @NotNull(message = "Unit price is required")
    private BigDecimal unitPrice;
    
    private BigDecimal totalPrice;
}
