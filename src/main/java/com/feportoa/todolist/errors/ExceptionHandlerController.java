package com.feportoa.todolist.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {
    
    @ExceptionHandler(HttpMessageNotReadableException.class) // @ExceptionHandler(TipoDaExceptionDoMetodo)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException exc) { // Handle exception(TipoDaException varName)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(exc.getMessage());
    }

}
