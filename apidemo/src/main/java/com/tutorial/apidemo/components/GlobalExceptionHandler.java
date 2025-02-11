package com.tutorial.apidemo.components;

import com.tutorial.apidemo.exceptions.CustomException;
import com.tutorial.apidemo.exceptions.NotFoundException;
import com.tutorial.apidemo.exceptions.UnauthorizedException;
import com.tutorial.apidemo.models.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseObject> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put("message", error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject("failed", errors.get("message"), null)
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseObject> handleGeneralExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ResponseObject("failed", ex.getMessage(), null)
        );
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseObject> handleCustomExceptions(CustomException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject("failed", ex.getMessage(), null)
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ResponseObject> handleAuthenticationException(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ResponseObject("failed", ex.getMessage(), null)
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseObject> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", ex.getMessage(), null)
        );
    }
}
