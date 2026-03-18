package org.example.orderservice.mapper;

import org.example.orderservice.domain.aggregate.Order;
import org.example.orderservice.domain.dto.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "amount", source = "money.amount")
    @Mapping(target = "currency", source = "money.currency")
    @Mapping(target = "orderId", source = "orderId.orderId")
    @Mapping(target = "customerId", source = "customerId.customerId")
    @Mapping(target = "productId", source = "productId.productId")
    OrderDto toOrderDto(Order order);

}
