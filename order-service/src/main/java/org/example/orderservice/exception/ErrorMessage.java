package org.example.orderservice.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

    // Common
    NULL_ID(HttpStatus.BAD_REQUEST, "ID cannot be null"),
    NEGATIVE_ID(HttpStatus.BAD_REQUEST, "ID cannot be 0 or negative"),

    // Orders
    ORDER_NOT_EXIST(HttpStatus.NOT_FOUND, "Order orderId=%s does not exist"),

    //Message
    UNEXPECTED_ERROR_PROCESSING_MESSAGE(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error processing message: %"),

    // Integration with PaymentService
    PAYMENT_CLIENT_ERROR(HttpStatus.BAD_REQUEST, "Payment service client error: %s"),
    PAYMENT_SERVER_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "Payment service server error: %s");

    private final HttpStatus status;
    private final String message;

}
