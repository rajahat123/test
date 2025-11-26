package com.ecommerce.paymentservice.controller;

import com.ecommerce.paymentservice.dto.PaymentDTO;
import com.ecommerce.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    
    private final PaymentService paymentService;
    
    // API 45: Process payment
    @PostMapping
    public ResponseEntity<PaymentDTO> processPayment(@Valid @RequestBody PaymentDTO paymentDTO) {
        PaymentDTO payment = paymentService.processPayment(paymentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }
    
    // API 46: Get payment by ID
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long id) {
        PaymentDTO payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }
    
    // API 47: Get payment by transaction ID
    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<PaymentDTO> getPaymentByTransactionId(@PathVariable String transactionId) {
        PaymentDTO payment = paymentService.getPaymentByTransactionId(transactionId);
        return ResponseEntity.ok(payment);
    }
    
    // API 48: Get payments by order ID
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByOrderId(@PathVariable Long orderId) {
        List<PaymentDTO> payments = paymentService.getPaymentsByOrderId(orderId);
        return ResponseEntity.ok(payments);
    }
    
    // API 49: Get payments by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByUserId(@PathVariable Long userId) {
        List<PaymentDTO> payments = paymentService.getPaymentsByUserId(userId);
        return ResponseEntity.ok(payments);
    }
    
    // API 50: Refund payment
    @PostMapping("/{id}/refund")
    public ResponseEntity<PaymentDTO> refundPayment(@PathVariable Long id) {
        PaymentDTO payment = paymentService.refundPayment(id);
        return ResponseEntity.ok(payment);
    }
}
