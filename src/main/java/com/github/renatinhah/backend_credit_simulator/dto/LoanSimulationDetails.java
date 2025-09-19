package com.github.renatinhah.backend_credit_simulator.dto;

import java.math.BigDecimal;

public class LoanSimulationDetails {
    private BigDecimal totalAmountToPay;
    private BigDecimal totalInterest;

    public LoanSimulationDetails(BigDecimal totalAmountToPay, BigDecimal totalInterest) {
        this.totalAmountToPay = totalAmountToPay;
        this.totalInterest = totalInterest;
    }

    public BigDecimal getTotalAmountToPay() {
        return totalAmountToPay;
    }

    public void setTotalAmountToPay(BigDecimal totalAmountToPay) {
        this.totalAmountToPay = totalAmountToPay;
    }

    public BigDecimal getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(BigDecimal totalInterest) {
        this.totalInterest = totalInterest;
    }
}
