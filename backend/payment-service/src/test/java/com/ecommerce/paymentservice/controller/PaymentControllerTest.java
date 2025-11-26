package com.ecommerce.paymentservice.controller;

import com.ecommerce.paymentservice.dto.PaymentDTO;
import com.ecommerce.paymentservice.entity.PaymentMethod;
import com.ecommerce.paymentservice.entity.PaymentStatus;
import com.ecommerce.paymentservice.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    private PaymentDTO paymentDTO;

    @BeforeEach
    void setUp() {
        paymentDTO = new PaymentDTO();
        paymentDTO.setId(1L);
        paymentDTO.setOrderId(1L);
        paymentDTO.setUserId(1L);
        paymentDTO.setAmount(new BigDecimal("999.99"));
        paymentDTO.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        paymentDTO.setStatus(PaymentStatus.COMPLETED);
        paymentDTO.setTransactionId("TXN-12345");
    }

    @Test
    void testProcessPayment_Success() throws Exception {
        when(paymentService.processPayment(any(PaymentDTO.class))).thenReturn(paymentDTO);

        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId").value("TXN-12345"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        verify(paymentService, times(1)).processPayment(any(PaymentDTO.class));
    }

    @Test
    void testGetPaymentById_Success() throws Exception {
        when(paymentService.getPaymentById(1L)).thenReturn(paymentDTO);

        mockMvc.perform(get("/api/payments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.transactionId").value("TXN-12345"));

        verify(paymentService, times(1)).getPaymentById(1L);
    }

    @Test
    void testGetPaymentsByOrderId_Success() throws Exception {
        List<PaymentDTO> payments = Arrays.asList(paymentDTO);
        when(paymentService.getPaymentsByOrderId(1L)).thenReturn(payments);

        mockMvc.perform(get("/api/payments/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value(1));

        verify(paymentService, times(1)).getPaymentsByOrderId(1L);
    }

    @Test
    void testRefundPayment_Success() throws Exception {
        when(paymentService.refundPayment(1L)).thenReturn(paymentDTO);

        mockMvc.perform(post("/api/payments/1/refund"))
                .andExpect(status().isOk());

        verify(paymentService, times(1)).refundPayment(1L);
    }
}
