package com.maptracker.issuemap.domain.comment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(MyException.class)
    public ResponseEntity<String> handleMyException(MyException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErrorCode().getMessage());
    }
}