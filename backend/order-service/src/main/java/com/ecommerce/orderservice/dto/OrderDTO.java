package com.ecommerce.orderservice.dto;

import com.ecommerce.orderservice.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private String orderNumber;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    private BigDecimal totalAmount;
    private BigDecimal taxAmount;
    private BigDecimal shippingAmount;
    private BigDecimal discountAmount;
    private OrderStatus status;
    private String shippingAddress;
    private String billingAddress;
    private List<OrderItemDTO> items;
    private String orderDate;
    private String shippedDate;
    private String deliveredDate;
    private String createdAt;
    private String updatedAt;
}
