package com.ecommerce.paymentservice.service;

import com.ecommerce.paymentservice.dto.PaymentDTO;
import com.ecommerce.paymentservice.entity.Payment;
import com.ecommerce.paymentservice.entity.PaymentMethod;
import com.ecommerce.paymentservice.entity.PaymentStatus;
import com.ecommerce.paymentservice.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Payment testPayment;
    private PaymentDTO paymentDTO;

    @BeforeEach
    void setUp() {
        testPayment = new Payment();
        testPayment.setId(1L);
        testPayment.setOrderId(1L);
        testPayment.setUserId(1L);
        testPayment.setAmount(new BigDecimal("999.99"));
        testPayment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        testPayment.setStatus(PaymentStatus.PENDING);
        testPayment.setTransactionId("TXN-12345");
        testPayment.setCreatedAt(LocalDateTime.now());
        testPayment.setUpdatedAt(LocalDateTime.now());

        paymentDTO = new PaymentDTO();
        paymentDTO.setId(1L);
        paymentDTO.setOrderId(1L);
        paymentDTO.setUserId(1L);
        paymentDTO.setAmount(new BigDecimal("999.99"));
        paymentDTO.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        paymentDTO.setStatus(PaymentStatus.PENDING);
        paymentDTO.setTransactionId("TXN-12345");
    }

    @Test
    void testProcessPayment_Success() {
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);

        PaymentDTO result = paymentService.processPayment(paymentDTO);

        assertNotNull(result);
        assertEquals(PaymentStatus.PENDING, result.getStatus());
        assertNotNull(result.getTransactionId());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testGetPaymentById_Success() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));

        PaymentDTO result = paymentService.getPaymentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("TXN-12345", result.getTransactionId());
        verify(paymentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPaymentById_NotFound() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> paymentService.getPaymentById(1L));
    }

    @Test
    void testGetPaymentsByOrderId() {
        when(paymentRepository.findByOrderId(1L)).thenReturn(Arrays.asList(testPayment));

        List<PaymentDTO> results = paymentService.getPaymentsByOrderId(1L);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).getOrderId());
        verify(paymentRepository, times(1)).findByOrderId(1L);
    }

    @Test
    void testGetPaymentsByUserId() {
        when(paymentRepository.findByUserId(1L)).thenReturn(Arrays.asList(testPayment));

        List<PaymentDTO> results = paymentService.getPaymentsByUserId(1L);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).getUserId());
        verify(paymentRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testGetPaymentByTransactionId_Success() {
        when(paymentRepository.findByTransactionId("TXN-12345")).thenReturn(Optional.of(testPayment));

        PaymentDTO result = paymentService.getPaymentByTransactionId("TXN-12345");

        assertNotNull(result);
        assertEquals("TXN-12345", result.getTransactionId());
        verify(paymentRepository, times(1)).findByTransactionId("TXN-12345");
    }

    @Test
    void testRefundPayment_Success() {
        testPayment.setStatus(PaymentStatus.COMPLETED);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);

        PaymentDTO result = paymentService.refundPayment(1L);

        assertNotNull(result);
        verify(paymentRepository, times(1)).findById(1L);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testRefundPayment_NotCompleted() {
        testPayment.setStatus(PaymentStatus.PENDING);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));

        assertThrows(RuntimeException.class, () -> paymentService.refundPayment(1L));
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void testUpdatePaymentStatus_Success() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);

        PaymentDTO result = paymentService.updatePaymentStatus(1L, PaymentStatus.COMPLETED);

        assertNotNull(result);
        verify(paymentRepository, times(1)).findById(1L);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testGetPaymentsByStatus() {
        when(paymentRepository.findByStatus(PaymentStatus.COMPLETED))
                .thenReturn(Arrays.asList(testPayment));

        List<PaymentDTO> results = paymentService.getPaymentsByStatus(PaymentStatus.COMPLETED);

        assertNotNull(results);
        assertEquals(1, results.size());
        verify(paymentRepository, times(1)).findByStatus(PaymentStatus.COMPLETED);
    }
}
