package com.github.renatinhah.backend_credit_simulator.exceptions;

public class AgeNotSupportedException extends RuntimeException {

    public AgeNotSupportedException(int age) {
        super(String.format("Age range not found for age: %d", age));
    }
}
