package org.example.paymentservice.mapper;

import org.example.paymentservice.domain.aggregate.Payment;
import org.example.paymentservice.domain.dto.CreatePaymentRequest;
import org.example.paymentservice.domain.dto.PaymentDto;
import org.example.paymentservice.integration.order.dto.request.PaymentRequestMessage;
import org.example.paymentservice.integration.order.dto.response.PaymentResponseMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "paymentId", source = "paymentId.paymentId")
    @Mapping(target = "amount", source = "money.amount")
    @Mapping(target = "currency", source = "money.currency")
    @Mapping(target = "inquiryRefId", source = "inquiryRefId.inquiryRefId")
    @Mapping(target = "transactionRefId", source = "transactionRefId.transactionRefId")
    PaymentDto toCreatePaymentDto(Payment payment);

    CreatePaymentRequest toCreatePaymentRequest(PaymentRequestMessage paymentRequestMessage);

    @Mapping(target = "orderId", source = "orderId")
    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "amount", source = "money.amount")
    @Mapping(target = "currency", source = "money.currency")
    @Mapping(target = "paymentStatus", source = "status")
    @Mapping(target = "paymentId", source = "paymentId.paymentId")
    PaymentResponseMessage toPaymentResponseMessage(Payment payment);

}
