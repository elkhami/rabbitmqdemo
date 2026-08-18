package com.rabbitmqdemo.notification_service.messaging.springamqp;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

import com.rabbitmqdemo.notification_service.config.RabbitMqConfig;
import com.rabbitmqdemo.notification_service.event.OrderCreatedEvent;
import com.rabbitmqdemo.notification_service.service.NotificationService;

import java.io.IOException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class SpringOrderConsumer {

    private final NotificationService notificationService;
    private final RabbitTemplate rabbitTemplate;

    private static final int MAX_RETRIES = 3;
    private static final String RETRY_HEADER = "x-retry-count";

    public SpringOrderConsumer(
            NotificationService notificationService,
            RabbitTemplate rabbitTemplate) {

        this.notificationService = notificationService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMqConfig.ORDER_QUEUE)
    public void consume(
            OrderCreatedEvent event,
            Message message,
            Channel channel) throws IOException {
        
        long deliveryTag = message.getMessageProperties()
                .getDeliveryTag();

        Integer retryCount = (Integer) message
                .getMessageProperties()
                .getHeaders()
                .getOrDefault(RETRY_HEADER, 0);

        System.out.println(
                "Received event: " + event.eventId());

        System.out.println(
                "Retry count: " + retryCount);

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
            if (retryCount < MAX_RETRIES) {

                sendToRetryQueue(
                        event,
                        retryCount + 1);

                channel.basicAck(
                        deliveryTag,
                        false);

                System.out.println(
                        "Retry scheduled: "
                                + (retryCount + 1));

            } else {

                channel.basicNack(
                        deliveryTag,
                        false,
                        false);

                System.out.println(
                        "Retries exhausted, sent to DLQ");
            }
        }
    }

    private void sendToRetryQueue(
            OrderCreatedEvent event,
            int retryCount) {

        rabbitTemplate.convertAndSend(
                RabbitMqConfig.ORDER_RETRY_EXCHANGE,
                RabbitMqConfig.ORDER_RETRY_ROUTING_KEY,
                event,
                message -> {

                    message.getMessageProperties()
                            .setHeader(
                                    RETRY_HEADER,
                                    retryCount);

                    return message;
                });
    }
}