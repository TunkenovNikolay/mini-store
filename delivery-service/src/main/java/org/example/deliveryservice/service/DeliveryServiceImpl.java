package org.example.deliveryservice.service;

import lombok.RequiredArgsConstructor;
import org.example.deliveryservice.domain.aggregate.Delivery;
import org.example.deliveryservice.domain.dto.CreateDeliveryRequest;
import org.example.deliveryservice.domain.dto.DeliveryDto;
import org.example.deliveryservice.domain.vo.DeliveryAddress;
import org.example.deliveryservice.domain.vo.OrderRefId;
import org.example.deliveryservice.domain.vo.PaymentRefId;
import org.example.deliveryservice.exception.ErrorMessage;
import org.example.deliveryservice.exception.ServiceException;
import org.example.deliveryservice.mapper.DeliveryMapper;
import org.example.deliveryservice.repository.DeliveryRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;

    @Override
    public DeliveryDto getDeliveryByDeliveryId(String deliveryId) {
        return deliveryRepository.findByDeliveryId_deliveryId(deliveryId)
            .map(deliveryMapper::toDeliveryDto)
            .orElseThrow(() -> new ServiceException(ErrorMessage.DELIVERY_NOT_EXIST, deliveryId));
    }

    @Override
    public DeliveryDto getDeliveryByOrderRefId(UUID orderRefId) {
        return deliveryRepository.findByOrderRefId_orderRefId(orderRefId)
            .map(deliveryMapper::toDeliveryDto)
            .orElseThrow(() -> new ServiceException(ErrorMessage.DELIVERY_NOT_EXIST, orderRefId));
    }

    @Override
    public DeliveryDto createDelivery(CreateDeliveryRequest request) {
        OrderRefId orderRefId = new OrderRefId(request.getOrderRefId());
        PaymentRefId paymentRefId = new PaymentRefId(request.getPaymentRefId());
        DeliveryAddress deliveryAddress = new DeliveryAddress(request.getStreet(), request.getCity(),
            request.getPostalCode());

        Delivery delivery = new Delivery(orderRefId, paymentRefId, deliveryAddress);

        Delivery savedDelivery = deliveryRepository.save(delivery);

        return deliveryMapper.toDeliveryDto(savedDelivery);
    }

    @Override
    public DeliveryDto updateDelivery(String deliveryId, DeliveryDto deliveryDto) {
        Delivery existingDelivery = deliveryRepository.findByDeliveryId_deliveryId(deliveryId)
            .orElseThrow(() -> new ServiceException(ErrorMessage.DELIVERY_NOT_EXIST, deliveryId));

        DeliveryAddress deliveryAddress = new DeliveryAddress(deliveryDto.getStreet(), deliveryDto.getCity(),
            deliveryDto.getPostalCode());

        existingDelivery.setAddress(deliveryAddress);

        return deliveryMapper.toDeliveryDto(deliveryRepository.save(existingDelivery));
    }

    @Override
    public void deleteDelivery(String deliveryId) {
        Delivery existingDelivery = deliveryRepository.findByDeliveryId_deliveryId(deliveryId)
            .orElseThrow(() -> new ServiceException(ErrorMessage.DELIVERY_NOT_EXIST, deliveryId));

        deliveryRepository.delete(existingDelivery);
    }
}
