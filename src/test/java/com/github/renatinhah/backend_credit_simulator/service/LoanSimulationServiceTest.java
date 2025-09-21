package com.github.renatinhah.backend_credit_simulator.service;

import com.github.renatinhah.backend_credit_simulator.dto.LoanSimulationRequest;
import com.github.renatinhah.backend_credit_simulator.dto.LoanSimulationResponse;
import com.github.renatinhah.backend_credit_simulator.exceptions.LoanSimulationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoanSimulationServiceTest {

    @InjectMocks
    private LoanSimulationService loanSimulationService;

    private LoanSimulationRequest validRequest;
    private final LocalDate TODAY = LocalDate.of(2025, 1, 1);

    @BeforeEach
    void setUp() {
        validRequest = new LoanSimulationRequest();
        validRequest.setLoanAmount(new BigDecimal("10000.00"));
        validRequest.setPaymentTermInMonths(12);
    }

    @Test
    @DisplayName("Should simulate loan for person aged 25 years - 5% rate")
    void shouldSimulateLoanSuccessfully_WhenAge25() throws LoanSimulationException {
        validRequest.setBirthDate(TODAY.minusYears(25));

        BigDecimal expectedMonthlyInstallment = new BigDecimal("856.07").setScale(2, RoundingMode.HALF_UP);
        BigDecimal expectedTotalInterest = new BigDecimal("272.90").setScale(2, RoundingMode.HALF_UP);
        BigDecimal expectedTotalAmountToPay = new BigDecimal("10272.90").setScale(2, RoundingMode.HALF_UP);

        LoanSimulationResponse response = loanSimulationService.simulate(validRequest);

        assertEquals(expectedMonthlyInstallment, response.getMonthlyInstallment());
        assertEquals(expectedTotalInterest, response.getTotalInterest());
        assertEquals(expectedTotalAmountToPay, response.getTotalAmountToPay());
    }

    @Test
    @DisplayName("Should simulate loan for person aged 35 years - 3% rate")
    void shouldSimulateLoanSuccessfully_WhenAge35() throws LoanSimulationException {
        validRequest.setBirthDate(TODAY.minusYears(35));

        BigDecimal expectedMonthlyInstallment = new BigDecimal("846.94").setScale(2, RoundingMode.HALF_UP);
        BigDecimal expectedTotalInterest = new BigDecimal("163.24").setScale(2, RoundingMode.HALF_UP);
        BigDecimal expectedTotalAmountToPay = new BigDecimal("10163.24").setScale(2, RoundingMode.HALF_UP);

        LoanSimulationResponse response = loanSimulationService.simulate(validRequest);

        assertEquals(expectedMonthlyInstallment, response.getMonthlyInstallment());
        assertEquals(expectedTotalInterest, response.getTotalInterest());
        assertEquals(expectedTotalAmountToPay, response.getTotalAmountToPay());
    }

    @Test
    @DisplayName("Should simulate loan for person aged 41 years - 2% rate")
    void shouldSimulateLoanSuccessfully_WhenAge41() throws LoanSimulationException {
        validRequest.setBirthDate(TODAY.minusYears(41));

        BigDecimal expectedMonthlyInstallment = new BigDecimal("842.39").setScale(2, RoundingMode.HALF_UP);
        BigDecimal expectedTotalInterest = new BigDecimal("108.66").setScale(2, RoundingMode.HALF_UP);
        BigDecimal expectedTotalAmountToPay = new BigDecimal("10108.66").setScale(2, RoundingMode.HALF_UP);

        LoanSimulationResponse response = loanSimulationService.simulate(validRequest);

        assertEquals(expectedMonthlyInstallment, response.getMonthlyInstallment());
        assertEquals(expectedTotalInterest, response.getTotalInterest());
        assertEquals(expectedTotalAmountToPay, response.getTotalAmountToPay());
    }

    @Test
    @DisplayName("Should simulate loan for person aged 61 years - 4% rate")
    void shouldSimulateLoanSuccessfully_WhenAge61() throws LoanSimulationException {
        validRequest.setBirthDate(TODAY.minusYears(61));

        BigDecimal expectedMonthlyInstallment = new BigDecimal("851.50").setScale(2, RoundingMode.HALF_UP);
        BigDecimal expectedTotalInterest = new BigDecimal("217.99").setScale(2, RoundingMode.HALF_UP);
        BigDecimal expectedTotalAmountToPay = new BigDecimal("10217.99").setScale(2, RoundingMode.HALF_UP);

        LoanSimulationResponse response = loanSimulationService.simulate(validRequest);

        assertEquals(expectedMonthlyInstallment, response.getMonthlyInstallment());
        assertEquals(expectedTotalInterest, response.getTotalInterest());
        assertEquals(expectedTotalAmountToPay, response.getTotalAmountToPay());
    }

    @Test
    @DisplayName("Should throw LoanSimulationException when request is invalid")
    void shouldThrowLoanSimulationExceptionWhenInvalidRequest() {
        validRequest.setLoanAmount(null);
        validRequest.setPaymentTermInMonths(12);
        validRequest.setBirthDate(TODAY.minusYears(35));

        LoanSimulationException exception = assertThrows(
                LoanSimulationException.class,
                () -> loanSimulationService.simulate(validRequest)
        );

        assertTrue(exception.getMessage().contains("Error an simulation"));
    }
}
