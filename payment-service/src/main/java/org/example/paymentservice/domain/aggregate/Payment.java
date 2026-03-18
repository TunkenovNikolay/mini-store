package org.example.paymentservice.domain.aggregate;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.paymentservice.domain.PaymentStatus;
import org.example.paymentservice.domain.vo.InquiryRefId;
import org.example.paymentservice.domain.vo.Money;
import org.example.paymentservice.domain.vo.PaymentId;
import org.example.paymentservice.domain.vo.TransactionRefId;

import java.math.BigDecimal;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Embedded
    @Column(nullable = false, unique = true)
    private PaymentId paymentId = new PaymentId();

    @Embedded
    @Column(nullable = false)
    private Money money = new Money(BigDecimal.ZERO, "USD");
    ;

    @Embedded
    @Column(nullable = false)
    private InquiryRefId inquiryRefId;

    @Embedded
    private TransactionRefId transactionRefId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    public Payment(Money money, InquiryRefId inquiryRefId) {
        this.money = money != null ? money : new Money();
        this.inquiryRefId = inquiryRefId;
    }

}

