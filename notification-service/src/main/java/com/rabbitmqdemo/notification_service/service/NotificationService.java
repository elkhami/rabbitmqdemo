package com.rabbitmqdemo.notification_service.service;

import com.rabbitmqdemo.notification_service.event.OrderCreatedEvent;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void sendOrderCreatedNotification(
            OrderCreatedEvent event) {

        System.out.println(
                "Sending notification for order: "
                        + event.orderId());

        System.out.println(
                "Order amount: " + event.amount());
    }
}
