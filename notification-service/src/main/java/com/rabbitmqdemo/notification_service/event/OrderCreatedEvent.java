package com.rabbitmqdemo.notification_service.event;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderCreatedEvent(
        String eventId,
        String orderId,
        BigDecimal amount,
        Instant createdAt) {
}