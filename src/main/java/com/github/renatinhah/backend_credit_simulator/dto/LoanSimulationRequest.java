package com.github.renatinhah.backend_credit_simulator.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LoanSimulationRequest {

    @NotNull(message = "Loan amount is required")
    @DecimalMin(value = "0.01", message = "Loan amount must be greater than zero")
    private BigDecimal loanAmount;

    @NotNull(message = "Birth date is required")
    private LocalDate birthDate;

    @Min(value = 1, message = "Payment term must be at least 1 month")
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


