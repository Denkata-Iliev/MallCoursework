package com.deni.mallcoursework.infrastructure.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
@Getter
public class ConflictException extends RuntimeException {
    private final String field;

    public ConflictException(String field) {
        super("This " + field + " already exists!");
        this.field = field;
    }
}
