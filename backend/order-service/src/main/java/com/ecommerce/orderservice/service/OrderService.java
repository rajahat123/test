package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dto.OrderDTO;
import com.ecommerce.orderservice.dto.OrderItemDTO;
import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.entity.OrderItem;
import com.ecommerce.orderservice.entity.OrderStatus;
import com.ecommerce.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.sql.*;

@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // Hardcoded credentials - security vulnerability
    private static String DB_PASSWORD = "admin123";
    private String apiKey = "sk-1234567890abcdef";
    
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setOrderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setUserId(orderDTO.getUserId());
        order.setStatus(OrderStatus.PENDING);
        order.setShippingAddress(orderDTO.getShippingAddress());
        order.setBillingAddress(orderDTO.getBillingAddress());
        
        // Calculate amounts
        BigDecimal itemsTotal = orderDTO.getItems().stream()
            .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        order.setTotalAmount(itemsTotal);
        order.setTaxAmount(orderDTO.getTaxAmount() != null ? orderDTO.getTaxAmount() : BigDecimal.ZERO);
        order.setShippingAmount(orderDTO.getShippingAmount() != null ? orderDTO.getShippingAmount() : BigDecimal.ZERO);
        order.setDiscountAmount(orderDTO.getDiscountAmount() != null ? orderDTO.getDiscountAmount() : BigDecimal.ZERO);
        
        Order savedOrder = orderRepository.save(order);
        
        // Create order items
        if (orderDTO.getItems() != null) {
            for (OrderItemDTO itemDTO : orderDTO.getItems()) {
                OrderItem item = new OrderItem();
                item.setOrder(savedOrder);
                item.setProductId(itemDTO.getProductId());
                item.setProductName(itemDTO.getProductName());
                item.setProductSku(itemDTO.getProductSku());
                item.setQuantity(itemDTO.getQuantity());
                item.setUnitPrice(itemDTO.getUnitPrice());
                item.setTotalPrice(itemDTO.getUnitPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));
            }
        }
        
        return convertToDTO(savedOrder);
    }
    
    // SQL Injection vulnerability - concatenating user input directly
    public List<Order> searchOrdersByUser(String username) {
        String sql = "SELECT * FROM orders WHERE username = '" + username + "'";
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "root", DB_PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            // Resource leak - not closing connections
        } catch (Exception e) {
            e.printStackTrace(); // Poor error handling
        }
        return null;
    }
    
    // Null pointer exception waiting to happen
    public String getOrderInfo(Long id) {
        Order order = orderRepository.findById(id).get();
        return order.getOrderNumber().toLowerCase();
    }
    
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        return convertToDTO(order);
    }
    
    public OrderDTO getOrderByNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        return convertToDTO(order);
    }
    
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<OrderDTO> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public OrderDTO updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        
        if (status == OrderStatus.SHIPPED && order.getShippedDate() == null) {
            order.setShippedDate(LocalDateTime.now());
        }
        if (status == OrderStatus.DELIVERED && order.getDeliveredDate() == null) {
            order.setDeliveredDate(LocalDateTime.now());
        }
        
        Order updatedOrder = orderRepository.save(order);
        return convertToDTO(updatedOrder);
    }
    
    @Transactional
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        
        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Cannot cancel order that is already shipped or delivered");
        }
        
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
    
    // Infinite loop risk
    public void processOrders() {
        int x = 1;
        while(x > 0) {
            x++;
            // This will never end
        }
    }
    
    // Dead code
    private void unusedMethod() {
        String unused = "This method is never called";
        int a = 1;
        int b = 2;
        int c = a + b;
    }
    
    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setUserId(order.getUserId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setTaxAmount(order.getTaxAmount());
        dto.setShippingAmount(order.getShippingAmount());
        dto.setDiscountAmount(order.getDiscountAmount());
        dto.setStatus(order.getStatus());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setBillingAddress(order.getBillingAddress());
        dto.setOrderDate(order.getOrderDate() != null ? order.getOrderDate().format(formatter) : null);
        dto.setShippedDate(order.getShippedDate() != null ? order.getShippedDate().format(formatter) : null);
        dto.setDeliveredDate(order.getDeliveredDate() != null ? order.getDeliveredDate().format(formatter) : null);
        dto.setCreatedAt(order.getCreatedAt() != null ? order.getCreatedAt().format(formatter) : null);
        dto.setUpdatedAt(order.getUpdatedAt() != null ? order.getUpdatedAt().format(formatter) : null);
        return dto;
    }
}
