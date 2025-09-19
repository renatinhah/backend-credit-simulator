package com.github.renatinhah.backend_credit_simulator.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LoanSimulationRequest {
    private BigDecimal loanAmount;
    private LocalDate birthDate;
    private int paymentTermInMonths;

    public LoanSimulationRequest(BigDecimal bigDecimal, LocalDate of, int i) {
        this.loanAmount = bigDecimal;
        this.birthDate = of;
        this.paymentTermInMonths = i;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public int getPaymentTermInMonths() {
        return paymentTermInMonths;
    }

    public void setPaymentTermInMonths(int paymentTermInMonths) {
        this.paymentTermInMonths = paymentTermInMonths;
    }
}


