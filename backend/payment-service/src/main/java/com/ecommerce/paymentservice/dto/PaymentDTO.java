package com.ecommerce.paymentservice.dto;

import com.ecommerce.paymentservice.entity.PaymentMethod;
import com.ecommerce.paymentservice.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Long id;
    private String transactionId;
    
    @NotNull(message = "Order ID is required")
    private Long orderId;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Amount is required")
    private BigDecimal amount;
    
    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
    
    private PaymentStatus status;
    private String paymentGatewayResponse;
    private String paymentDate;
    private String createdAt;
    private String updatedAt;
}
