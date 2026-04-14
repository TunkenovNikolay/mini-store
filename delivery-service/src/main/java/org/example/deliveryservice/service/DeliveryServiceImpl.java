package org.example.deliveryservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.example.deliveryservice.domain.aggregate.Delivery;
import org.example.deliveryservice.domain.dto.CreateDeliveryRequest;
import org.example.deliveryservice.domain.dto.DeliveryDto;
import org.example.deliveryservice.domain.vo.DeliveryAddress;
import org.example.deliveryservice.domain.vo.OrderRefId;
import org.example.deliveryservice.domain.vo.PaymentRefId;
import org.example.deliveryservice.exception.ErrorMessage;
import org.example.deliveryservice.exception.ServiceException;
import org.example.deliveryservice.integration.order.dto.DeliveryResponseMessage;
import org.example.deliveryservice.mapper.DeliveryMapper;
import org.example.deliveryservice.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final KafkaTemplate<String, DeliveryResponseMessage> kafkaTemplate;
    private final DeliveryMapper deliveryMapper;

    @Value("${kafka.service.delivery.delivery-confirmation-topic}")
    private String deliveryConfirmationTopic;

    @Override
    public Delivery getDeliveryByDeliveryId(String deliveryId) {
        return deliveryRepository.findByDeliveryId_deliveryId(deliveryId)
            .orElseThrow(() -> new ServiceException(ErrorMessage.DELIVERY_NOT_EXIST, deliveryId));
    }

    @Override
    public Delivery getDeliveryByOrderRefId(UUID orderRefId) {
        return deliveryRepository.findByOrderRefId_orderRefId(orderRefId)
            .orElseThrow(() -> new ServiceException(ErrorMessage.DELIVERY_NOT_EXIST, orderRefId));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Delivery createDelivery(CreateDeliveryRequest request) {
        OrderRefId orderRefId = new OrderRefId(request.getOrderRefId());
        PaymentRefId paymentRefId = new PaymentRefId(request.getPaymentRefId());
        DeliveryAddress deliveryAddress = new DeliveryAddress(request.getStreet(), request.getCity(),
            request.getPostalCode());

        Delivery delivery = new Delivery(orderRefId, paymentRefId, deliveryAddress);
        Delivery savedDelivery = deliveryRepository.save(delivery);
        log.info("Insurance confirmation created with ID: {}", delivery.getId());

        sendMessage(delivery);
        return savedDelivery;
    }

    private void sendMessage(Delivery delivery) {
        DeliveryResponseMessage responseMessage = deliveryMapper.toDeliveryResponseMessage(delivery);

        // Заголовок с идемпотентным ключем
        RecordHeader header = new RecordHeader("X-Idempotency-Key", UUID.randomUUID().toString().getBytes());

        // Создаем ProducerRecord для полной кастомизации отправки сообщения в Kafka
        var producerRecord = new ProducerRecord<String, DeliveryResponseMessage>(
            deliveryConfirmationTopic, null, null, null, responseMessage, List.of(header));
        kafkaTemplate.send(producerRecord);
        log.info("Sent delivery confirmation creation message to Kafka for ID: {}", delivery.getId());
    }

    @Override
    public Delivery updateDelivery(String deliveryId, DeliveryDto deliveryDto) {
        Delivery existingDelivery = deliveryRepository.findByDeliveryId_deliveryId(deliveryId)
            .orElseThrow(() -> new ServiceException(ErrorMessage.DELIVERY_NOT_EXIST, deliveryId));

        DeliveryAddress deliveryAddress = new DeliveryAddress(deliveryDto.getStreet(), deliveryDto.getCity(),
            deliveryDto.getPostalCode());

        existingDelivery.setAddress(deliveryAddress);

        return deliveryRepository.save(existingDelivery);
    }

    @Override
    public void deleteDelivery(String deliveryId) {
        Delivery existingDelivery = deliveryRepository.findByDeliveryId_deliveryId(deliveryId)
            .orElseThrow(() -> new ServiceException(ErrorMessage.DELIVERY_NOT_EXIST, deliveryId));

        deliveryRepository.delete(existingDelivery);
    }
}
