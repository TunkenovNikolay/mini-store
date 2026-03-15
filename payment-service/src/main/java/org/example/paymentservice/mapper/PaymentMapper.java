package org.example.paymentservice.mapper;

import org.example.paymentservice.domain.dto.PaymentDto;
import org.example.paymentservice.domain.aggregate.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "paymentId", source = "paymentId.paymentId")
    @Mapping(target = "amount", source = "money.amount")
    @Mapping(target = "currency", source = "money.currency")
    @Mapping(target = "inquiryRefId", source = "inquiryRefId.inquiryRefId")
    @Mapping(target = "transactionRefId", source = "transactionRefId.transactionRefId")
    PaymentDto toPaymentDto(Payment payment);

}
