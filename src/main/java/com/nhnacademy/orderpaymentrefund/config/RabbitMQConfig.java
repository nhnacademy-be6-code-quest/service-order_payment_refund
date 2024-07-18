package com.nhnacademy.orderpaymentrefund.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {
    private final KeyManagerConfig keyManagerConfig;
//    private final String rabbitHost;
//    private final int rabbitPort;
//    private final String rabbitUsername;
//    private final String rabbitPassword;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(keyManagerConfig.rabbitHost(), keyManagerConfig.rabbitPort());
        connectionFactory.setUsername(keyManagerConfig.rabbitUsername());
        connectionFactory.setPassword(keyManagerConfig.rabbitPassword());
        return connectionFactory;
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

}
