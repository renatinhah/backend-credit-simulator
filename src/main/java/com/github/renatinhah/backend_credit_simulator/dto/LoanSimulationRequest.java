package com.github.renatinhah.backend_credit_simulator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoanSimulationRequest {

    @NotNull(message = "Loan amount is required")
    @DecimalMin(value = "0.01", message = "Loan amount must be greater than zero")
    @Schema(description = "Loan amount", example = "10000")
    private BigDecimal loanAmount;

    @Past(message = "Birth date must be in the past")
    @NotNull(message = "Birth date is required")
    @Schema(description = "Customer birth date", example = "2000-09-01")
    private LocalDate birthDate;

    @Min(value = 1, message = "Payment term must be at least 1 month")
    @Schema(description = "Number of installments", example = "12")
    private int paymentTermInMonths;
}


