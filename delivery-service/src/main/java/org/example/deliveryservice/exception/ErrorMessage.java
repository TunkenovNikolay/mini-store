package org.example.deliveryservice.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

    // Common
    NULL_ID(HttpStatus.BAD_REQUEST, "ID cannot be null"),
    NEGATIVE_ID(HttpStatus.BAD_REQUEST, "ID cannot be 0 or negative"),

    // Delivery
    DELIVERY_NOT_EXIST(HttpStatus.NOT_FOUND, "Delivery deliveryId=%s does not exist"),

    //Message
    UNEXPECTED_ERROR_PROCESSING_MESSAGE(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error processing message: %");

    private final HttpStatus status;
    private final String message;

}
