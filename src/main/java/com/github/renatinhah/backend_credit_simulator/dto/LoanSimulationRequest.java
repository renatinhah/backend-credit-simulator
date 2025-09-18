package com.github.renatinhah.backend_credit_simulator.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoanSimulationRequest {
    private BigDecimal loanAmount;
    private LocalDate birthDate;
    private int paymentTermInMonths;
}


