package com.github.renatinhah.backend_credit_simulator.handlers;

import com.github.renatinhah.backend_credit_simulator.exceptions.AgeNotSupportedException;
import com.github.renatinhah.backend_credit_simulator.exceptions.LoanSimulationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AgeNotSupportedException.class)
    public ResponseEntity<String> handleAgeNotSupported(AgeNotSupportedException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(LoanSimulationException.class)
    public ResponseEntity<String> handleLoanSimulationException(LoanSimulationException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + ex.getMessage());
    }
}
