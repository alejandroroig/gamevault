package com.aleroig.gamevault.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    public static final String ACTIVIDAD_EXCHANGE = "gamevault.actividad.exchange";
    public static final String ACTIVIDAD_QUEUE = "gamevault.actividad.queue";
    public static final String ACTIVIDAD_ROUTING_KEY = "actividad.videojuego";

    @Bean
    public DirectExchange actividadExchange() {
        return new DirectExchange(ACTIVIDAD_EXCHANGE, true, false);
    }

    @Bean
    public Queue actividadQueue() {
        return QueueBuilder.durable(ACTIVIDAD_QUEUE).build();
    }

    @Bean
    public Binding actividadBinding(Queue actividadQueue, DirectExchange actividadExchange) {
        return BindingBuilder
                .bind(actividadQueue)
                .to(actividadExchange)
                .with(ACTIVIDAD_ROUTING_KEY);
    }
}