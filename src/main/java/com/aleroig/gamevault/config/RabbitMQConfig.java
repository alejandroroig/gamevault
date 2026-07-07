package com.aleroig.gamevault.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    public static final String VIDEOJUEGO_EXCHANGE = "gamevault.videojuego.exchange";

    public static final String ACTIVIDAD_VIDEOJUEGO_QUEUE = "gamevault.actividad.videojuego.queue";
    public static final String REVIEWS_VIDEOJUEGO_QUEUE = "gamevault.reviews.videojuego.queue";

    public static final String VIDEOJUEGO_CREADO_ROUTING_KEY = "videojuego.creado";
    public static final String VIDEOJUEGO_ACTUALIZADO_ROUTING_KEY = "videojuego.actualizado";
    public static final String VIDEOJUEGO_ELIMINADO_ROUTING_KEY = "videojuego.eliminado";
    public static final String TODOS_VIDEOJUEGOS_ROUTING_KEY = "videojuego.*";

    @Bean
    public TopicExchange videojuegoExchange() {
        return new TopicExchange(VIDEOJUEGO_EXCHANGE, true, false);
    }

    @Bean
    public Queue actividadVideojuegoQueue() {
        return QueueBuilder.durable(ACTIVIDAD_VIDEOJUEGO_QUEUE).build();
    }

    @Bean
    public Queue reviewsVideojuegoQueue() {
        return QueueBuilder.durable(REVIEWS_VIDEOJUEGO_QUEUE).build();
    }

    @Bean
    public Binding actividadVideojuegoBinding(Queue actividadVideojuegoQueue, TopicExchange videojuegoExchange) {
        return BindingBuilder
                .bind(actividadVideojuegoQueue)
                .to(videojuegoExchange)
                .with(TODOS_VIDEOJUEGOS_ROUTING_KEY);
    }

    @Bean
    public Binding reviewsVideojuegoBinding(Queue reviewsVideojuegoQueue, TopicExchange videojuegoExchange) {
        return BindingBuilder
                .bind(reviewsVideojuegoQueue)
                .to(videojuegoExchange)
                .with(VIDEOJUEGO_ELIMINADO_ROUTING_KEY);
    }
}