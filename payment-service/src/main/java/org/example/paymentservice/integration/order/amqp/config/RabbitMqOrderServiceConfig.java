package org.example.paymentservice.integration.order.amqp.config;

import lombok.RequiredArgsConstructor;
import org.example.paymentservice.integration.order.amqp.config.properties.RabbitMqOrderServiceProperties;
import org.example.paymentservice.integration.order.dto.request.PaymentRequestMessage;
import org.example.paymentservice.integration.order.dto.response.PaymentResponseMessage;
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
 * Конфигурационный класс для настройки RabbitMQ для сервиса оплаты заказов.
 */
@Configuration
@RequiredArgsConstructor
public class RabbitMqOrderServiceConfig {

    private final RabbitMqOrderServiceProperties properties;

    /**
     * Создает очередь для ответа о статусе платежа
     */
    @Bean
    public Queue paymentResponseQueue() {
        return QueueBuilder.durable(properties.queueResponseName())
            .build();
    }

    /**
     * Создает прямой обменник для ответа о платеже
     */
    @Bean
    public DirectExchange paymentResponseExchange() {
        return new DirectExchange(properties.queueResponseName());
    }

    /**
     * Создаетя связывание между очередью и обменником
     *
     * @param paymentResponseQueue    - очередь
     * @param paymentResponseExchange обменник
     * @return связывание
     */
    @Bean
    public Binding paymentResponseBinding(Queue paymentResponseQueue, DirectExchange paymentResponseExchange) {
        return BindingBuilder
            .bind(paymentResponseQueue)
            .to(paymentResponseExchange)
            .with(properties.queueResponseName());
    }

    /**
     * Настраивает шаблон RabbitTemplate с JSON-конвертером
     *
     * @param connectionFactory - фабрика соединений
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
        converter.getJavaTypeMapper().addTrustedPackages("org.example.paymentservice", "java");
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

        idClassMapping.put("reserve-request", PaymentRequestMessage.class);
        idClassMapping.put("reserve-response", PaymentResponseMessage.class);

        classMapper.setIdClassMapping(idClassMapping);
        return classMapper;
    }
}
