package com.deni.mallcoursework.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException() {
        super("Old password doesn't match!");
    }
}
