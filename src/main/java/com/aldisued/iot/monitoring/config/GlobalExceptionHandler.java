package com.aldisued.iot.monitoring.config;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Unique constraint's name can be changed in the entity class for added customization and clarity
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException hce) {
            if ("sensors_name_key".equalsIgnoreCase(hce.getConstraintName())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Unique constraint violated: " + hce.getConstraintName());
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
    }
}
