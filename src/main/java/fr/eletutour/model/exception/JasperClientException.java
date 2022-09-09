package fr.eletutour.model.exception;

import org.springframework.http.HttpStatus;

public class JasperClientException extends RuntimeException {
    private final HttpStatus status;

    public JasperClientException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
