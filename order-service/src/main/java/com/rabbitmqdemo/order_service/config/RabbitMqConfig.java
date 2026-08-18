package com.rabbitmqdemo.order_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String ORDER_EXCHANGE =
            "spring.order.exchange";

    public static final String ORDER_QUEUE =
            "spring.order.queue";

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
    public Queue orderQueue() {
        
        System.out.println(">>> Creating ORDER QUEUE bean <<<");
        return new Queue(
                ORDER_QUEUE,
                true,       // durable
                false,      // exclusive
                false       // autoDelete
        );
    }


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

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
    
}
