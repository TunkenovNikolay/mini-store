package org.example.deliveryservice.mapper;

import org.example.deliveryservice.domain.aggregate.Delivery;
import org.example.deliveryservice.domain.dto.CreateDeliveryRequest;
import org.example.deliveryservice.domain.dto.DeliveryDto;
import org.example.deliveryservice.integration.order.dto.DeliveryRequestMessage;
import org.example.deliveryservice.integration.order.dto.DeliveryResponseMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    @Mapping(target = "deliveryId", source = "deliveryId.deliveryId")
    @Mapping(target = "orderRefId", source = "orderRefId.orderRefId")
    @Mapping(target = "paymentRefId", source = "paymentRefId.paymentRefId")
    @Mapping(target = "street", source = "address.street")
    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "postalCode", source = "address.postalCode")
    DeliveryDto toDeliveryDto(Delivery delivery);

    @Mapping(target = "orderRefId", source = "orderRefId")
    @Mapping(target = "paymentRefId", source = "paymentRefId")
    @Mapping(target = "street", source = "street")
    @Mapping(target = "city", source = "city")
    @Mapping(target = "postalCode", source = "postalCode")
    CreateDeliveryRequest toDelivery(DeliveryRequestMessage deliveryRequestMessage);

    @Mapping(target = "deliveryId", source = "deliveryId.deliveryId")
    @Mapping(target = "orderRefId", source = "orderRefId.orderRefId")
    @Mapping(target = "paymentRefId", source = "paymentRefId.paymentRefId")
    @Mapping(target = "street", source = "address.street")
    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "postalCode", source = "address.postalCode")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "trackingNumber", source = "trackingNumber")
    DeliveryResponseMessage toDeliveryResponseMessage(Delivery delivery);

}
