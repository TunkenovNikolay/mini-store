package org.example.orderservice.integration.payment.amqp.config;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.integration.payment.amqp.config.properties.RabbitMqPaymentServiceProperties;
import org.example.orderservice.integration.payment.dto.PaymentRequest;
import org.example.orderservice.integration.payment.dto.PaymentResponseMessage;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурационный класс для настройки RabbitMQ для сервиса оплаты
 */
@Configuration
@RequiredArgsConstructor
public class RabbitMqPaymentServiceConfig {

    private final RabbitMqPaymentServiceProperties properties;

    /**
     * Создает очередь для запросов оплаты
     *
     * @return очередь
     */
    @Bean
    public Queue paymentRequstQueue() {
        return QueueBuilder.durable(properties.queueRequestName())
            .build();
    }

    /**
     * Создает прямой обменник для запросов оплаты
     *
     * @return обменник
     */
    @Bean
    public DirectExchange paymentRequestExchange() {
        return new DirectExchange(properties.exchangeRequestName());
    }

    /**
     * Создает связывание между очередью и обменником.
     *
     * @param paymentRequestQueue    очередь
     * @param paymentRequestExchange обменник
     * @return связывание
     */
    @Bean
    public Binding paymentBinding(Queue paymentRequestQueue, DirectExchange paymentRequestExchange) {
        return BindingBuilder
            .bind(paymentRequestQueue)
            .to(paymentRequestExchange)
            .with(properties.exchangeRequestName());
    }

    /**
     * Настраивает шаблон RabbitTemplate с JSON-конвертером.
     *
     * @param connectionFactory фабрика соединений
     * @return настроенный RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonConverter());
        return rabbitTemplate;
    }

    /**
     * Создает фабрику слушателей с JSON-конвертером.
     *
     * @param connectionFactory фабрика соединений
     * @return фабрика слушателей
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonConverter());
        factory.setDefaultRequeueRejected(false);
        factory.setAutoStartup(true);
        return factory;
    }

    /**
     * Создает JSON-конвертер сообщений с маппингом классов.
     *
     * @return JSON-конвертер
     */
    @Bean
    public JacksonJsonMessageConverter jsonConverter() {
        JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter();
        converter.setClassMapper(classMapper());
        converter.getJavaTypeMapper().addTrustedPackages("com.iprody", "java");
        return converter;
    }

    /**
     * Создает маппер классов для JSON-конвертера.
     *
     * @return маппер классов
     */
    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();

        idClassMapping.put("reserve-request", PaymentRequest.class);
        idClassMapping.put("reserve-response", PaymentResponseMessage.class);

        classMapper.setIdClassMapping(idClassMapping);
        return classMapper;
    }
}
