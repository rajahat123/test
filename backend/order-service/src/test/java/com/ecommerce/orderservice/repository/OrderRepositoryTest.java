package com.ecommerce.orderservice.repository;

import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.entity.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    private Order testOrder;

    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setUserId(1L);
        testOrder.setOrderNumber("ORD-001");
        testOrder.setStatus(OrderStatus.PENDING);
        testOrder.setTotalAmount(new BigDecimal("999.99"));
        testOrder.setShippingAddress("123 Main St, New York, NY 10001");
        testOrder.setBillingAddress("123 Main St, New York, NY 10001");
        testOrder.setPaymentMethod("CREDIT_CARD");
        testOrder.setCreatedAt(LocalDateTime.now());
        testOrder.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testFindByOrderNumber_Success() {
        entityManager.persist(testOrder);
        entityManager.flush();

        Optional<Order> found = orderRepository.findByOrderNumber("ORD-001");

        assertThat(found).isPresent();
        assertThat(found.get().getOrderNumber()).isEqualTo("ORD-001");
    }

    @Test
    void testFindByUserId() {
        entityManager.persist(testOrder);

        Order order2 = new Order();
        order2.setUserId(1L);
        order2.setOrderNumber("ORD-002");
        order2.setStatus(OrderStatus.CONFIRMED);
        order2.setTotalAmount(new BigDecimal("500.00"));
        order2.setCreatedAt(LocalDateTime.now());
        order2.setUpdatedAt(LocalDateTime.now());
        entityManager.persist(order2);
        entityManager.flush();

        List<Order> orders = orderRepository.findByUserId(1L);

        assertThat(orders).hasSize(2);
        assertThat(orders).allMatch(order -> order.getUserId().equals(1L));
    }

    @Test
    void testFindByStatus() {
        entityManager.persist(testOrder);

        Order order2 = new Order();
        order2.setUserId(2L);
        order2.setOrderNumber("ORD-002");
        order2.setStatus(OrderStatus.CONFIRMED);
        order2.setTotalAmount(new BigDecimal("500.00"));
        order2.setCreatedAt(LocalDateTime.now());
        order2.setUpdatedAt(LocalDateTime.now());
        entityManager.persist(order2);
        entityManager.flush();

        List<Order> pendingOrders = orderRepository.findByStatus(OrderStatus.PENDING);

        assertThat(pendingOrders).hasSize(1);
        assertThat(pendingOrders.get(0).getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    void testSaveOrder() {
        Order saved = orderRepository.save(testOrder);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getOrderNumber()).isEqualTo("ORD-001");
    }

    @Test
    void testUpdateOrder() {
        entityManager.persist(testOrder);
        entityManager.flush();

        testOrder.setStatus(OrderStatus.CONFIRMED);
        Order updated = orderRepository.save(testOrder);

        assertThat(updated.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
    }

    @Test
    void testDeleteOrder() {
        Order saved = entityManager.persist(testOrder);
        entityManager.flush();

        orderRepository.deleteById(saved.getId());

        Optional<Order> deleted = orderRepository.findById(saved.getId());
        assertThat(deleted).isNotPresent();
    }

    @Test
    void testFindByUserIdOrderByCreatedAtDesc() {
        entityManager.persist(testOrder);

        Order order2 = new Order();
        order2.setUserId(1L);
        order2.setOrderNumber("ORD-002");
        order2.setStatus(OrderStatus.CONFIRMED);
        order2.setTotalAmount(new BigDecimal("500.00"));
        order2.setCreatedAt(LocalDateTime.now().plusMinutes(1));
        order2.setUpdatedAt(LocalDateTime.now());
        entityManager.persist(order2);
        entityManager.flush();

        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(1L);

        assertThat(orders).hasSize(2);
        assertThat(orders.get(0).getOrderNumber()).isEqualTo("ORD-002");
    }
}
