package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.dto.OrderDTO;
import com.ecommerce.orderservice.entity.OrderStatus;
import com.ecommerce.orderservice.service.OrderService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderDTO orderDTO;

    @BeforeEach
    void setUp() {
        orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setUserId(1L);
        orderDTO.setOrderNumber("ORD-001");
        orderDTO.setStatus(OrderStatus.PENDING);
        orderDTO.setTotalAmount(new BigDecimal("999.99"));
        orderDTO.setShippingAddress("123 Main St, New York, NY 10001");
        orderDTO.setBillingAddress("123 Main St, New York, NY 10001");
        orderDTO.setPaymentMethod("CREDIT_CARD");
    }

    @Test
    void testCreateOrder_Success() throws Exception {
        when(orderService.createOrder(any(OrderDTO.class))).thenReturn(orderDTO);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderNumber").value("ORD-001"))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(orderService, times(1)).createOrder(any(OrderDTO.class));
    }

    @Test
    void testGetOrderById_Success() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(orderDTO);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.orderNumber").value("ORD-001"));

        verify(orderService, times(1)).getOrderById(1L);
    }

    @Test
    void testGetOrdersByUserId_Success() throws Exception {
        List<OrderDTO> orders = Arrays.asList(orderDTO);
        when(orderService.getOrdersByUserId(1L)).thenReturn(orders);

        mockMvc.perform(get("/api/orders/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1));

        verify(orderService, times(1)).getOrdersByUserId(1L);
    }

    @Test
    void testUpdateOrderStatus_Success() throws Exception {
        when(orderService.updateOrderStatus(anyLong(), any(OrderStatus.class))).thenReturn(orderDTO);

        mockMvc.perform(patch("/api/orders/1/status")
                .param("status", "CONFIRMED"))
                .andExpect(status().isOk());

        verify(orderService, times(1)).updateOrderStatus(1L, OrderStatus.CONFIRMED);
    }

    @Test
    void testCancelOrder_Success() throws Exception {
        when(orderService.cancelOrder(1L)).thenReturn(orderDTO);

        mockMvc.perform(patch("/api/orders/1/cancel"))
                .andExpect(status().isOk());

        verify(orderService, times(1)).cancelOrder(1L);
    }
}
