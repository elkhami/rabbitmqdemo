package com.rabbitmqdemo.notification_service.messaging.springamqp;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

import com.rabbitmqdemo.notification_service.config.RabbitMqConfig;
import com.rabbitmqdemo.notification_service.event.OrderCreatedEvent;
import com.rabbitmqdemo.notification_service.service.NotificationService;

import java.io.IOException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SpringOrderConsumer {

    private final NotificationService notificationService;

    public SpringOrderConsumer(
            NotificationService notificationService) {

        this.notificationService = notificationService;
    }
    /*
    @RabbitListener(queues = RabbitMqConfig.ORDER_QUEUE)
    public void consume(OrderCreatedEvent event) {

        System.out.println(
                "Received OrderCreatedEvent: " + event);

        notificationService
                .sendOrderCreatedNotification(event);

        System.out.println("Processing finished.");
    } */

    @RabbitListener(queues = RabbitMqConfig.ORDER_QUEUE)
    public void consume(
            OrderCreatedEvent event,
            Message message,
            Channel channel) throws IOException {
        
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        System.out.println("Received: " + event);
        System.out.println("Delivery tag: " + deliveryTag);

        try {

            notificationService
                    .sendOrderCreatedNotification(event);

            // Processing successful
            channel.basicAck(
                    deliveryTag,
                    false);

            System.out.println("ACK sent");

        } catch (Exception e) {

            // Processing failed
            channel.basicNack(
                    deliveryTag,
                    false,
                    false); // Requeue the message

            System.out.println("NACK sent - message requeued");
        }
    }
}