package com.rw.testapp.domain.products;

import com.rw.testapp.common.Problem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class ProductExceptionsHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Problem handleProductNotFound(ProductNotFoundException exception) {
        log.error("An error occurred", exception);
        return Problem.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .detail(exception.getMessage())
                .build();
    }

    @ExceptionHandler(OutOfStockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Problem handleOutOfStock(OutOfStockException exception) {
        log.error("An error occurred", exception);
        return Problem.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .detail(exception.getMessage())
                .build();
    }
}
