package com.github.renatinhah.backend_credit_simulator.service;

import com.github.renatinhah.backend_credit_simulator.dto.LoanSimulationRequest;
import com.github.renatinhah.backend_credit_simulator.dto.LoanSimulationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class LoanSimulationPerformanceTest {

    @InjectMocks
    private LoanSimulationService loanSimulationService;

    @Test
    @DisplayName("Should handle 10k bulk simulations under 2 seconds")
    void shouldHandleHighVolume() {
        List<LoanSimulationRequest> requests = IntStream.range(0, 10_000)
                .mapToObj(i -> {
                    LoanSimulationRequest req = new LoanSimulationRequest();
                    req.setLoanAmount(new BigDecimal("10000"));
                    req.setPaymentTermInMonths(12);
                    req.setBirthDate(LocalDate.of(1990, 1, 1));
                    return req;
                })
                .toList();

        long start = System.currentTimeMillis();
        List<LoanSimulationResponse> responses = loanSimulationService.simulateBulk(requests);
        long duration = System.currentTimeMillis() - start;

        assertThat(responses).hasSize(10_000);
        assertThat(duration).isLessThan(2000); // 2 segundos
    }
}

