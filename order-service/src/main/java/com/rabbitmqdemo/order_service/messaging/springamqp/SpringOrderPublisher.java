package com.rabbitmqdemo.order_service.messaging.springamqp;

import com.rabbitmqdemo.order_service.config.RabbitMqConfig;
import com.rabbitmqdemo.order_service.event.OrderCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class SpringOrderPublisher {

    private final RabbitTemplate rabbitTemplate;

    public SpringOrderPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(OrderCreatedEvent event) {

        rabbitTemplate.convertAndSend(
                RabbitMqConfig.ORDER_EXCHANGE,
                RabbitMqConfig.ORDER_CREATED_ROUTING_KEY,
                event);
    }
}