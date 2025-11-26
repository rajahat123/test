package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dto.OrderDTO;
import com.ecommerce.orderservice.dto.OrderItemDTO;
import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.entity.OrderItem;
import com.ecommerce.orderservice.entity.OrderStatus;
import com.ecommerce.orderservice.repository.OrderItemRepository;
import com.ecommerce.orderservice.repository.OrderRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderService orderService;

    private Order testOrder;
    private OrderItem testOrderItem;
    private OrderDTO orderDTO;
    private OrderItemDTO orderItemDTO;

    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setUserId(1L);
        testOrder.setOrderNumber("ORD-001");
        testOrder.setStatus(OrderStatus.PENDING);
        testOrder.setTotalAmount(new BigDecimal("999.99"));
        testOrder.setShippingAddress("123 Main St, New York, NY 10001");
        testOrder.setBillingAddress("123 Main St, New York, NY 10001");
        testOrder.setPaymentMethod("CREDIT_CARD");
        testOrder.setCreatedAt(LocalDateTime.now());
        testOrder.setUpdatedAt(LocalDateTime.now());

        testOrderItem = new OrderItem();
        testOrderItem.setId(1L);
        testOrderItem.setOrder(testOrder);
        testOrderItem.setProductId(1L);
        testOrderItem.setQuantity(2);
        testOrderItem.setUnitPrice(new BigDecimal("499.99"));
        testOrderItem.setTotalPrice(new BigDecimal("999.98"));

        orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setUserId(1L);
        orderDTO.setOrderNumber("ORD-001");
        orderDTO.setStatus(OrderStatus.PENDING);
        orderDTO.setTotalAmount(new BigDecimal("999.99"));
        orderDTO.setShippingAddress("123 Main St, New York, NY 10001");
        orderDTO.setBillingAddress("123 Main St, New York, NY 10001");
        orderDTO.setPaymentMethod("CREDIT_CARD");

        orderItemDTO = new OrderItemDTO();
        orderItemDTO.setProductId(1L);
        orderItemDTO.setQuantity(2);
        orderItemDTO.setUnitPrice(new BigDecimal("499.99"));
        orderItemDTO.setTotalPrice(new BigDecimal("999.98"));

        orderDTO.setOrderItems(Arrays.asList(orderItemDTO));
    }

    @Test
    void testCreateOrder_Success() {
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(testOrderItem);

        OrderDTO result = orderService.createOrder(orderDTO);

        assertNotNull(result);
        assertEquals("ORD-001", result.getOrderNumber());
        assertEquals(OrderStatus.PENDING, result.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testGetOrderById_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderItemRepository.findByOrderId(1L)).thenReturn(Arrays.asList(testOrderItem));

        OrderDTO result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("ORD-001", result.getOrderNumber());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetOrderById_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderService.getOrderById(1L));
    }

    @Test
    void testGetOrdersByUserId() {
        when(orderRepository.findByUserId(1L)).thenReturn(Arrays.asList(testOrder));
        when(orderItemRepository.findByOrderId(1L)).thenReturn(Arrays.asList(testOrderItem));

        List<OrderDTO> results = orderService.getOrdersByUserId(1L);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).getUserId());
        verify(orderRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testUpdateOrderStatus_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        OrderDTO result = orderService.updateOrderStatus(1L, OrderStatus.CONFIRMED);

        assertNotNull(result);
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testCancelOrder_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        OrderDTO result = orderService.cancelOrder(1L);

        assertNotNull(result);
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testCancelOrder_AlreadyShipped() {
        testOrder.setStatus(OrderStatus.SHIPPED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        assertThrows(RuntimeException.class, () -> orderService.cancelOrder(1L));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testGetOrdersByStatus() {
        when(orderRepository.findByStatus(OrderStatus.PENDING)).thenReturn(Arrays.asList(testOrder));
        when(orderItemRepository.findByOrderId(1L)).thenReturn(Arrays.asList(testOrderItem));

        List<OrderDTO> results = orderService.getOrdersByStatus(OrderStatus.PENDING);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(OrderStatus.PENDING, results.get(0).getStatus());
        verify(orderRepository, times(1)).findByStatus(OrderStatus.PENDING);
    }

    @Test
    void testGetOrderByOrderNumber_Success() {
        when(orderRepository.findByOrderNumber("ORD-001")).thenReturn(Optional.of(testOrder));
        when(orderItemRepository.findByOrderId(1L)).thenReturn(Arrays.asList(testOrderItem));

        OrderDTO result = orderService.getOrderByOrderNumber("ORD-001");

        assertNotNull(result);
        assertEquals("ORD-001", result.getOrderNumber());
        verify(orderRepository, times(1)).findByOrderNumber("ORD-001");
    }
}
