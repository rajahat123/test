package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.dto.OrderDTO;
import com.ecommerce.orderservice.entity.OrderStatus;
import com.ecommerce.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    
    // API 38: Create order
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        OrderDTO order = orderService.createOrder(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
    
    // API 39: Get order by ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        OrderDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }
    
    // API 40: Get order by number
    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<OrderDTO> getOrderByNumber(@PathVariable String orderNumber) {
        OrderDTO order = orderService.getOrderByNumber(orderNumber);
        return ResponseEntity.ok(order);
    }
    
    // API 41: Get orders by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderDTO> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }
    
    // API 42: Get orders by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@PathVariable OrderStatus status) {
        List<OrderDTO> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }
    
    // API 43: Update order status
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        OrderDTO order = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(order);
    }
    
    // API 44: Cancel order
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }
}
