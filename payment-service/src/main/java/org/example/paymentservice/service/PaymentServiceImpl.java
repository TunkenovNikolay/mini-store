package org.example.paymentservice.service;

import lombok.RequiredArgsConstructor;
import org.example.paymentservice.domain.aggregate.Payment;
import org.example.paymentservice.domain.dto.CreatePaymentRequest;
import org.example.paymentservice.domain.dto.PaymentDto;
import org.example.paymentservice.domain.vo.InquiryRefId;
import org.example.paymentservice.domain.vo.Money;
import org.example.paymentservice.exception.ErrorMessage;
import org.example.paymentservice.exception.ServiceException;
import org.example.paymentservice.mapper.PaymentMapper;
import org.example.paymentservice.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public PaymentDto getPayment(String paymentId) {
        return paymentRepository.findByPaymentId_PaymentId(paymentId)
            .map(paymentMapper::toPaymentDto)
            .orElseThrow(() -> new ServiceException(ErrorMessage.PAYMENT_NOT_EXIST, paymentId));
    }

    @Override
    public PaymentDto createPayment(CreatePaymentRequest request) {
        InquiryRefId inquiryRefId = new InquiryRefId(request.getInquiryRefId());
        Money money = new Money(request.getAmount(), request.getCurrency());

        Payment payment = new Payment(money, inquiryRefId);

        Payment savedPayment = paymentRepository.save(payment);

        return paymentMapper.toPaymentDto(savedPayment);
    }

    @Override
    public PaymentDto updatePayment(String paymentId, PaymentDto paymentDto) {
        Payment existingPayment = paymentRepository.findByPaymentId_PaymentId(paymentId)
            .orElseThrow(() -> new ServiceException(ErrorMessage.PAYMENT_NOT_EXIST, paymentId));

        Money money = new Money(paymentDto.getAmount(), paymentDto.getCurrency());

        existingPayment.setMoney(money);

        return paymentMapper.toPaymentDto(paymentRepository.save(existingPayment));
    }

    @Override
    public void deletePayment(String paymentId) {
        Payment existingOrder = paymentRepository.findByPaymentId_PaymentId(paymentId)
            .orElseThrow(() -> new ServiceException(ErrorMessage.PAYMENT_NOT_EXIST, paymentId));

        paymentRepository.delete(existingOrder);
    }
}
