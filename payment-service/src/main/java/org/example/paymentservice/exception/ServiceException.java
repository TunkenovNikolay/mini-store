package org.example.paymentservice.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ServiceException extends RuntimeException {

    private HttpStatus status;
    private String message;

    public ServiceException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    private String getMessage(String value) {
        if (this.message == null || !this.message.contains("%")) {
            return this.message != null ? this.message : "";
        }
        try {
            return String.format(this.message, value);
        } catch (Exception e) {
            return this.message.replace("%", "%%");
        }
    }

    public ServiceException(ErrorMessage error, String value) {
        this(error.getStatus(), error.getMessage());
        this.message = getMessage(value);
    }

}
