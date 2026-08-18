package com.rabbitmqdemo.order_service.service;

import com.rabbitmqdemo.order_service.event.OrderCreatedEvent;
import com.rabbitmqdemo.order_service.messaging.springamqp.SpringOrderPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
public class OrderService {

    private final SpringOrderPublisher publisher;

    public OrderService(SpringOrderPublisher publisher) {
        this.publisher = publisher;
    }

    public OrderCreatedEvent createOrder(BigDecimal amount) {

        OrderCreatedEvent event = new OrderCreatedEvent(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                amount,
                Instant.now());

        publisher.publish(event);

        return event;
    }
}
