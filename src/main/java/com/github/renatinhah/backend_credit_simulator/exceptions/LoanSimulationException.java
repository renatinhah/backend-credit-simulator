package com.github.renatinhah.backend_credit_simulator.exceptions;

import java.math.BigDecimal;

public class LoanSimulationException extends Exception {

    public LoanSimulationException(BigDecimal loanAmount, int getPaymentTermInMonths) {
        super(String.format("Error an simulation for Loan Amount: %s, Payment Term: %d months", loanAmount, getPaymentTermInMonths));
    }
}
