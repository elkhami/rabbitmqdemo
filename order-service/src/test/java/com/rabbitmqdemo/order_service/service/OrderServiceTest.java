package com.rabbitmqdemo.order_service.service;

import com.rabbitmqdemo.order_service.event.OrderCreatedEvent;
import com.rabbitmqdemo.order_service.messaging.springamqp.SpringOrderPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private SpringOrderPublisher publisher;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldPublishEventWhenOrderIsCreated() {

        BigDecimal amount = new BigDecimal("149.99");

        OrderCreatedEvent event = orderService.createOrder(amount);

        assertEquals(amount, event.amount());
        assertNotNull(event.orderId());
        assertNotNull(event.eventId());
        assertNotNull(event.createdAt());

        verify(publisher).publish(event);
    }
}