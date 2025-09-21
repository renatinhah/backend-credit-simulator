package com.github.renatinhah.backend_credit_simulator.service;

import com.github.renatinhah.backend_credit_simulator.dto.LoanSimulationRequest;
import com.github.renatinhah.backend_credit_simulator.dto.LoanSimulationResponse;
import com.github.renatinhah.backend_credit_simulator.exceptions.LoanSimulationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class LoanSimulationServiceTest {

    @InjectMocks
    private LoanSimulationService loanSimulationService;

    private static final LocalDate TODAY = LocalDate.of(2025, 1, 1);
    private static final BigDecimal STANDARD_LOAN_AMOUNT = new BigDecimal("10000.00");
    private static final int STANDARD_PAYMENT_TERM = 12;

    // Dados de teste para diferentes idades e taxas esperadas
    private static final TestData[] AGE_RATE_TEST_DATA = {
            new TestData(0, 0.05, "856.07", "272.84", "10272.84"),
            new TestData(25, 0.05, "856.07", "272.84", "10272.84"),
            new TestData(26, 0.03, "846.94", "163.28", "10163.28"),
            new TestData(40, 0.03, "846.94", "163.28", "10163.28"),
            new TestData(41, 0.02, "842.39", "108.68", "10108.68"),
            new TestData(60, 0.02, "842.39", "108.68", "10108.68"),
            new TestData(61, 0.04, "851.50", "218.00", "10218.00"),
            new TestData(100, 0.04, "851.50", "218.00", "10218.00")
    };

    @ParameterizedTest(name = "Should simulate loan for age {0} with rate {1}")
    @MethodSource("ageRateTestData")
    @DisplayName("Should calculate loan simulation correctly for different age ranges")
    void shouldSimulateLoanCorrectlyForDifferentAges(TestData testData) throws LoanSimulationException {
        // Given
        LoanSimulationRequest request = createStandardRequest();
        request.setBirthDate(TODAY.minusYears(testData.age()));

        // When
        LoanSimulationResponse response = loanSimulationService.simulate(request);

        // Then
        assertLoanSimulationResponse(response, testData);
    }

    @Test
    @DisplayName("Should simulate loan with variable interest rate when provided")
    void shouldSimulateLoanWithVariableInterestRate() throws LoanSimulationException {
        // Given
        LoanSimulationRequest request = createStandardRequest();
        request.setBirthDate(LocalDate.of(2000, 1, 1));
        request.setVariableInterestRate(new BigDecimal("0.035"));

        BigDecimal expectedMonthlyInstallment = new BigDecimal("849.22");
        BigDecimal expectedTotalAmount = new BigDecimal("10190.64");
        BigDecimal expectedTotalInterest = new BigDecimal("190.64");

        // When
        LoanSimulationResponse response = loanSimulationService.simulate(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getMonthlyInstallment()).isEqualByComparingTo(expectedMonthlyInstallment);
        assertThat(response.getTotalAmountToPay()).isEqualByComparingTo(expectedTotalAmount);
        assertThat(response.getTotalInterest()).isEqualByComparingTo(expectedTotalInterest);
    }

    @ParameterizedTest(name = "Should handle reasonable loan amounts: {0}")
    @ValueSource(strings = {"100.00", "1000.00", "50000.00", "100000.00"})
    @DisplayName("Should handle reasonable loan amounts correctly")
    void shouldHandleReasonableLoanAmounts(String loanAmountStr) throws LoanSimulationException {
        // Given
        LoanSimulationRequest request = createStandardRequest();
        request.setLoanAmount(new BigDecimal(loanAmountStr));

        // When
        LoanSimulationResponse response = loanSimulationService.simulate(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getMonthlyInstallment()).isPositive();
        assertThat(response.getTotalAmountToPay()).isPositive();
        assertThat(response.getTotalInterest()).isGreaterThanOrEqualTo(BigDecimal.ZERO);
    }

    @ParameterizedTest(name = "Should handle reasonable payment terms: {0} months")
    @ValueSource(ints = {1, 6, 12, 24, 36, 48, 60})
    @DisplayName("Should handle reasonable payment terms correctly")
    void shouldHandleReasonablePaymentTerms(int paymentTerm) throws LoanSimulationException {
        // Given
        LoanSimulationRequest request = createStandardRequest();
        request.setPaymentTermInMonths(paymentTerm);

        // When
        LoanSimulationResponse response = loanSimulationService.simulate(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getMonthlyInstallment()).isPositive();

        // Verificar se o total calculado está correto
        BigDecimal calculatedTotal = response.getMonthlyInstallment()
                .multiply(new BigDecimal(paymentTerm))
                .setScale(2, RoundingMode.HALF_UP);

        assertThat(response.getTotalAmountToPay()).isEqualByComparingTo(calculatedTotal);
    }

    @Test
    @DisplayName("Should maintain precision in financial calculations")
    void shouldMaintainPrecisionInFinancialCalculations() throws LoanSimulationException {
        // Given
        LoanSimulationRequest request = createStandardRequest();
        request.setLoanAmount(new BigDecimal("10000.333"));
        request.setBirthDate(TODAY.minusYears(30));

        // When
        LoanSimulationResponse response = loanSimulationService.simulate(request);

        // Then
        assertThat(response.getMonthlyInstallment().scale()).isEqualTo(2);
        assertThat(response.getTotalAmountToPay().scale()).isEqualTo(2);
        assertThat(response.getTotalInterest().scale()).isEqualTo(2);

        // Verificar consistência dos cálculos
        BigDecimal calculatedTotal = response.getMonthlyInstallment()
                .multiply(new BigDecimal(STANDARD_PAYMENT_TERM))
                .setScale(2, RoundingMode.HALF_UP);

        assertThat(response.getTotalAmountToPay()).isEqualByComparingTo(calculatedTotal);

        BigDecimal calculatedInterest = response.getTotalAmountToPay()
                .subtract(request.getLoanAmount())
                .setScale(2, RoundingMode.HALF_UP);

        assertThat(response.getTotalInterest()).isEqualByComparingTo(calculatedInterest);
    }

    private LoanSimulationRequest createStandardRequest() {
        LoanSimulationRequest request = new LoanSimulationRequest();
        request.setLoanAmount(STANDARD_LOAN_AMOUNT);
        request.setPaymentTermInMonths(STANDARD_PAYMENT_TERM);
        request.setBirthDate(TODAY.minusYears(30)); // idade padrão para testes
        return request;
    }

    private void assertLoanSimulationResponse(LoanSimulationResponse response, TestData testData) {
        assertThat(response).isNotNull();
        assertThat(response.getMonthlyInstallment())
                .isEqualByComparingTo(new BigDecimal(testData.expectedMonthlyInstallment()));
        assertThat(response.getTotalInterest())
                .isEqualByComparingTo(new BigDecimal(testData.expectedTotalInterest()));
        assertThat(response.getTotalAmountToPay())
                .isEqualByComparingTo(new BigDecimal(testData.expectedTotalAmount()));
    }

    // Fonte de dados para testes parametrizados
    private static Stream<TestData> ageRateTestData() {
        return Arrays.stream(AGE_RATE_TEST_DATA);
    }

    // Record para encapsular dados de teste
    private record TestData(
            int age,
            double expectedRate,
            String expectedMonthlyInstallment,
            String expectedTotalInterest,
            String expectedTotalAmount
    ) {}
}