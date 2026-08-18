package com.rabbitmqdemo.notification_service.messaging.springamqp;

import com.rabbitmqdemo.notification_service.event.OrderCreatedEvent;
import com.rabbitmqdemo.notification_service.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SpringOrderConsumerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private SpringOrderConsumer consumer;

    @Test
    void shouldProcessOrderCreatedEvent() {

        OrderCreatedEvent event = new OrderCreatedEvent(
                "event-123",
                "order-123",
                new BigDecimal("149.99"),
                Instant.now());

        consumer.consume(event);

        verify(notificationService)
                .sendOrderCreatedNotification(event);
    }
}