package com.rabbitmqdemo.order_service.controller;

import com.rabbitmqdemo.order_service.event.OrderCreatedEvent;
import com.rabbitmqdemo.order_service.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderCreatedEvent createOrder(
            @RequestParam BigDecimal amount) {

        return orderService.createOrder(amount);
    }
}