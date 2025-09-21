package com.github.renatinhah.backend_credit_simulator;

import java.math.BigDecimal;
import java.math.MathContext;

public class TestPow {
    public static void main(String[] args) {
        BigDecimal x = new BigDecimal("1.01");
        int n = -12;
        BigDecimal y = x.pow(n, MathContext.DECIMAL128); // Deve dar erro
        System.out.println(y);
    }
}

