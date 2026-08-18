package com.rabbitmqdemo.notification_service.messaging.springamqp;

import com.rabbitmqdemo.notification_service.config.RabbitMqConfig;
import com.rabbitmqdemo.notification_service.event.OrderCreatedEvent;
import com.rabbitmqdemo.notification_service.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SpringOrderConsumer {

    private final NotificationService notificationService;

    public SpringOrderConsumer(
            NotificationService notificationService) {

        this.notificationService = notificationService;
    }

    @RabbitListener(queues = RabbitMqConfig.ORDER_QUEUE)
    public void consume(OrderCreatedEvent event) {

        System.out.println(
                "Received OrderCreatedEvent: " + event);

        notificationService
                .sendOrderCreatedNotification(event);
    }
}