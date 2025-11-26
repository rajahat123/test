package com.ecommerce.paymentservice.service;

import com.ecommerce.paymentservice.dto.PaymentDTO;
import com.ecommerce.paymentservice.entity.Payment;
import com.ecommerce.paymentservice.entity.PaymentStatus;
import com.ecommerce.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Transactional
    public PaymentDTO processPayment(PaymentDTO paymentDTO) {
        Payment payment = new Payment();
        payment.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase());
        payment.setOrderId(paymentDTO.getOrderId());
        payment.setUserId(paymentDTO.getUserId());
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setStatus(PaymentStatus.PROCESSING);
        
        // Simulate payment processing
        try {
            // Payment gateway integration would go here
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setPaymentDate(LocalDateTime.now());
            payment.setPaymentGatewayResponse("Payment processed successfully");
        } catch (Exception e) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setPaymentGatewayResponse("Payment processing failed: " + e.getMessage());
        }
        
        Payment savedPayment = paymentRepository.save(payment);
        return convertToDTO(savedPayment);
    }
    
    public PaymentDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Payment not found"));
        return convertToDTO(payment);
    }
    
    public PaymentDTO getPaymentByTransactionId(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));
        return convertToDTO(payment);
    }
    
    public List<PaymentDTO> getPaymentsByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<PaymentDTO> getPaymentsByUserId(Long userId) {
        return paymentRepository.findByUserId(userId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public PaymentDTO refundPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new RuntimeException("Only completed payments can be refunded");
        }
        
        payment.setStatus(PaymentStatus.REFUNDED);
        Payment updatedPayment = paymentRepository.save(payment);
        return convertToDTO(updatedPayment);
    }
    
    private PaymentDTO convertToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setTransactionId(payment.getTransactionId());
        dto.setOrderId(payment.getOrderId());
        dto.setUserId(payment.getUserId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setStatus(payment.getStatus());
        dto.setPaymentGatewayResponse(payment.getPaymentGatewayResponse());
        dto.setPaymentDate(payment.getPaymentDate() != null ? payment.getPaymentDate().format(formatter) : null);
        dto.setCreatedAt(payment.getCreatedAt() != null ? payment.getCreatedAt().format(formatter) : null);
        dto.setUpdatedAt(payment.getUpdatedAt() != null ? payment.getUpdatedAt().format(formatter) : null);
        return dto;
    }
}
