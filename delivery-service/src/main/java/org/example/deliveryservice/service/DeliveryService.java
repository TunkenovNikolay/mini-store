package org.example.deliveryservice.service;

import org.example.deliveryservice.domain.aggregate.Delivery;
import org.example.deliveryservice.domain.dto.CreateDeliveryRequest;
import org.example.deliveryservice.domain.dto.DeliveryDto;

import java.util.UUID;

public interface DeliveryService {

    /**
     * Получить Delivery по deliveryId
     */
    Delivery getDeliveryByDeliveryId(String deliveryId);

    /**
     * Получить Delivery по orderRefId
     */
    Delivery getDeliveryByOrderRefId(UUID orderRefId);

    /**
     * Создать Delivery
     */
    Delivery createDelivery(CreateDeliveryRequest request);

    /**
     * Обновить Delivery по deliveryId
     */
    Delivery updateDelivery(String deliveryId, DeliveryDto deliveryDto);

    /**
     * Удалить Delivery по deliveryId
     */
    void deleteDelivery(String deliveryId);

}
