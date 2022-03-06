package com.omf.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderMyFoodException.class)
    public ResponseEntity<Object> handleRestaurantBusinessException(OrderMyFoodException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getOrderMyFoodErrorList(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
