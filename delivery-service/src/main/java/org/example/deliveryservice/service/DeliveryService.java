package org.example.deliveryservice.service;

import org.example.deliveryservice.domain.dto.CreateDeliveryRequest;
import org.example.deliveryservice.domain.dto.DeliveryDto;

import java.util.UUID;

public interface DeliveryService {

    /**
     * Получить Delivery по deliveryId
     */
    DeliveryDto getDeliveryByDeliveryId(String deliveryId);

    /**
     * Получить Delivery по orderRefId
     */
    DeliveryDto getDeliveryByOrderRefId(UUID orderRefId);

    /**
     * Создать Delivery
     */
    DeliveryDto createDelivery(CreateDeliveryRequest request);

    /**
     * Обновить Delivery по deliveryId
     */
    DeliveryDto updateDelivery(String deliveryId, DeliveryDto deliveryDto);

    /**
     * Удалить Delivery по deliveryId
     */
    void deleteDelivery(String deliveryId);

}
