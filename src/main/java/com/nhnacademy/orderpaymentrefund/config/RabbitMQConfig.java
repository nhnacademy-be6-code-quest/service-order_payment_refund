package com.nhnacademy.orderpaymentrefund.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

//    @Value("${rabbit.cart.checkout.exchange.name}")
//    private String cartCheckoutExchangeName;
//    @Value("${rabbit.cart.checkout.routing.key}")
//    private String cartCheckoutRoutingKey;
//    @Value("${rabbit.inventory.decrease.exchange.name}")
//    private String inventoryDecreaseExchangeName;
//    @Value("${rabbit.inventory.decrease.routing.key}")
//    private String inventoryDecreaseRoutingKey;
//    @Value("${rabbit.inventory.increase.exchange.name}")
//    private String inventoryIncreaseExchangeName;
//    @Value("${rabbit.inventory.increase.routing.key}")
//    private String inventoryIncreaseRoutingKey;

//    @Bean
//    DirectExchange cartCheckoutExchange() {
//        return new DirectExchange(cartCheckoutExchangeName);
//    }
//
//    @Bean
//    Binding cartCheckoutBinding(Queue cartCheckoutQueue, DirectExchange cartCheckoutExchange){
//        return BindingBuilder.bind(cartCheckoutQueue).to(cartCheckoutExchange).with(cartCheckoutRoutingKey);
//    }
//
//    @Bean
//    DirectExchange inventoryDecreaseExchange() {
//        return new DirectExchange(inventoryDecreaseExchangeName);
//    }
//
//    @Bean
//    Queue inventoryDecreaseQueue(){
//        return new Queue(inventoryDecreaseQueueName);
//    }
//
//    @Bean
//    Binding inventoryDecreaseBinding(Queue inventoryDecreaseQueue, DirectExchange inventoryDecreaseExchange){
//        return BindingBuilder.bind(inventoryDecreaseQueue).to(inventoryDecreaseExchange).with(inventoryDecreaseRoutingKey);
//    }
//
//    @Bean
//    DirectExchange inventoryIncreaseExchange() {
//        return new DirectExchange(inventoryIncreaseExchangeName);
//    }
//
//    @Bean
//    Queue inventoryIncreaseQueue(){
//        return new Queue(inventoryIncreaseQueueName);
//    }
//
//    @Bean
//    Binding inventoryIncreaseBinding(Queue inventoryIncreaseQueue, DirectExchange inventoryIncreaseExchange){
//        return BindingBuilder.bind(inventoryIncreaseQueue).to(inventoryIncreaseExchange).with(inventoryIncreaseRoutingKey);
//    }


    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

}
