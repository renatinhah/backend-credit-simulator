package com.github.renatinhah.backend_credit_simulator.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanSimulationResponse {
    private BigDecimal totalAmountToPay;
    private BigDecimal monthlyInstallment;
    private BigDecimal totalInterest;
}
