package com.rabbitmqdemo.notification_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String ORDER_EXCHANGE = "spring.order.exchange";

    public static final String ORDER_QUEUE = "spring.order.queue";

    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";

    public static final String ORDER_DLX = "spring.order.dlx";

    public static final String ORDER_DLQ = "spring.order.dlq";

    public static final String ORDER_DEAD_ROUTING_KEY = "order.dead";

    public static final String ORDER_RETRY_EXCHANGE = "spring.order.retry.exchange";

    public static final String ORDER_RETRY_QUEUE = "spring.order.retry.queue";

    public static final String ORDER_RETRY_ROUTING_KEY = "order.retry";

    // Main exchange
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(
                ORDER_EXCHANGE,
                true,
                false);
    }

    // Main queue + dead-letter configuration
    @Bean
    public Queue orderQueue() {
        return QueueBuilder
                .durable(ORDER_QUEUE)
                .deadLetterExchange(ORDER_DLX)
                .deadLetterRoutingKey(ORDER_DEAD_ROUTING_KEY)
                .build();
    }

    // Main binding
    @Bean
    public Binding orderBinding(
            Queue orderQueue,
            DirectExchange orderExchange) {

        System.out.println(">>> Creating ORDER BINDING bean <<<");
        return BindingBuilder
                .bind(orderQueue)
                .to(orderExchange)
                .with(ORDER_CREATED_ROUTING_KEY);
    }

    // Dead Letter Exchange
    @Bean
    public DirectExchange orderDeadLetterExchange() {
        return new DirectExchange(
                ORDER_DLX,
                true,
                false
            );
    }

    // Dead Letter Queue
    @Bean
    public Queue orderDeadLetterQueue() {
        return QueueBuilder
                .durable(ORDER_DLQ)
                .build();
    }

    // DLX → DLQ binding
    @Bean
    public Binding orderDeadLetterBinding(
            Queue orderDeadLetterQueue,
            DirectExchange orderDeadLetterExchange) {

        return BindingBuilder
                .bind(orderDeadLetterQueue)
                .to(orderDeadLetterExchange)
                .with(ORDER_DEAD_ROUTING_KEY);
    }

    // Retry exchange
    @Bean
    public DirectExchange orderRetryExchange() {
        return new DirectExchange(
                ORDER_RETRY_EXCHANGE,
                true,
                false);
    }

    // Retry queue
    @Bean
    public Queue orderRetryQueue() {

        return QueueBuilder
                .durable(ORDER_RETRY_QUEUE)

                // Message waits here for 5 seconds
                .ttl(5_000)

                // After TTL, dead-letter it back
                // to the normal order exchange
                .deadLetterExchange(ORDER_EXCHANGE)

                // Route it back to the normal order queue
                .deadLetterRoutingKey(
                        ORDER_CREATED_ROUTING_KEY)

                .build();
    }

    // Retry binding
    @Bean
    public Binding orderRetryBinding(
            Queue orderRetryQueue,
            DirectExchange orderRetryExchange) {

        return BindingBuilder
                .bind(orderRetryQueue)
                .to(orderRetryExchange)
                .with(ORDER_RETRY_ROUTING_KEY);
    }
    
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}