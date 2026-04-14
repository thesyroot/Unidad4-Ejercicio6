package com.programacion4.unidad4ej6.config.exceptions;

import java.util.List;

import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomException {

    public NotFoundException(String message) {
        super(message, HttpStatus.BAD_REQUEST, List.of(message));
    }

    public NotFoundException(String message, HttpStatus status, List<String> errors) {
        super(message, status, errors);
    }
}
