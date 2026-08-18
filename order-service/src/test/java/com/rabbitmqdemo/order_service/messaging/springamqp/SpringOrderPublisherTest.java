package com.rabbitmqdemo.order_service.messaging.springamqp;

import com.rabbitmqdemo.order_service.event.OrderCreatedEvent;
import com.rabbitmqdemo.order_service.config.RabbitMqConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.time.Instant;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SpringOrderPublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private SpringOrderPublisher publisher;

    @Test
    void shouldPublishOrderCreatedEvent() {

        OrderCreatedEvent event = new OrderCreatedEvent(
                "event-123",
                "order-123",
                new BigDecimal("149.99"),
                Instant.now());

        publisher.publish(event);

        verify(rabbitTemplate).convertAndSend(
                RabbitMqConfig.ORDER_EXCHANGE,
                RabbitMqConfig.ORDER_CREATED_ROUTING_KEY,
                event);
    }
}