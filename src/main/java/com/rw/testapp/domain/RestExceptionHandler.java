package com.rw.testapp.domain;

import com.rw.testapp.common.Problem;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
class RestExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Problem handleInvalidInput(ConstraintViolationException exception) {
        log.error("An error occurred", exception);
        return Problem.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .detail(exception.getMessage())
                .build();
    }
}
