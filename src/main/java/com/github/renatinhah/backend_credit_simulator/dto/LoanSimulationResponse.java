package com.github.renatinhah.backend_credit_simulator.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LoanSimulationResponse {
    private BigDecimal totalAmountToPay;
    private BigDecimal monthlyInstallment;
    private BigDecimal totalInterest;

}
