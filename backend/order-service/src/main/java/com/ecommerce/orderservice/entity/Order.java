package com.ecommerce.orderservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String orderNumber;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private BigDecimal totalAmount;
    
    private BigDecimal taxAmount;
    private BigDecimal shippingAmount;
    private BigDecimal discountAmount;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    
    private String shippingAddress;
    private String billingAddress;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OrderItem> orderItems;
    
    private LocalDateTime orderDate;
    private LocalDateTime shippedDate;
    private LocalDateTime deliveredDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        orderDate = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
