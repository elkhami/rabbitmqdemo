package com.rabbitmqdemo.order_service.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String ORDER_EXCHANGE =
            "spring.order.exchange";

    public static final String ORDER_CREATED_ROUTING_KEY =
            "order.created";

    public RabbitMqConfig() {
        System.out.println(">>> RabbitMqConfig LOADED <<<");
    }

    @Bean
    public DirectExchange orderExchange() {

        System.out.println(">>> Creating ORDER EXCHANGE bean <<<");
        return new DirectExchange(
                ORDER_EXCHANGE,
                true,       // durable
                false       // autoDelete
        );
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
    
}
