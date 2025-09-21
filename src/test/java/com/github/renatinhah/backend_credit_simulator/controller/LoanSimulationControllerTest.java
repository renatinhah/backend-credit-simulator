package com.github.renatinhah.backend_credit_simulator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.renatinhah.backend_credit_simulator.dto.LoanSimulationRequest;
import com.github.renatinhah.backend_credit_simulator.dto.LoanSimulationResponse;
import com.github.renatinhah.backend_credit_simulator.handlers.GlobalExceptionHandler;
import com.github.renatinhah.backend_credit_simulator.service.LoanSimulationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoanSimulationControllerSimpleTest {

    private MockMvc mockMvc;

    @Mock
    private LoanSimulationService loanSimulationService;

    @InjectMocks
    private LoanSimulationController loanSimulationController;

    private ObjectMapper objectMapper;

    private static final String ENDPOINT = "/api/v1/simulations";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loanSimulationController)
                .setControllerAdvice(new GlobalExceptionHandler()) // Adicionar seu exception handler
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Para LocalDate
    }

    @Test
    @DisplayName("Should return 200 when request is valid")
    void shouldReturn200WhenRequestIsValid() throws Exception {
        // Given
        LoanSimulationRequest validRequest = createValidRequest();
        LoanSimulationResponse expectedResponse = createMockResponse();

        when(loanSimulationService.simulate(any(LoanSimulationRequest.class)))
                .thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monthlyInstallment").value(856.07))
                .andExpect(jsonPath("$.totalInterest").value(272.84))
                .andExpect(jsonPath("$.totalAmountToPay").value(10272.84));
    }

    @ParameterizedTest(name = "Should return 400 for invalid request: {0}")
    @MethodSource("invalidRequestData")
    @DisplayName("Should return 400 Bad Request for invalid input data")
    void shouldReturn400ForInvalidRequestData(String testName, String requestJson) throws Exception {
        // When & Then
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        // Verify service was never called due to validation failure
        verify(loanSimulationService, never()).simulate(any());
    }

    // Helper methods
    private LoanSimulationRequest createValidRequest() {
        LoanSimulationRequest request = new LoanSimulationRequest();
        request.setLoanAmount(new BigDecimal("10000.00"));
        request.setBirthDate(LocalDate.of(2000, 1, 1));
        request.setPaymentTermInMonths(12);
        return request;
    }

    private LoanSimulationResponse createMockResponse() {
        return LoanSimulationResponse.builder()
                .monthlyInstallment(new BigDecimal("856.07"))
                .totalInterest(new BigDecimal("272.84"))
                .totalAmountToPay(new BigDecimal("10272.84"))
                .build();
    }

    // Data source for invalid request tests
    private static Stream<Arguments> invalidRequestData() {
        return Stream.of(
                Arguments.of(
                        "Null loan amount",
                        "{\"birthDate\":\"2000-01-01\",\"paymentTermInMonths\":12}"
                ),
                Arguments.of(
                        "Zero loan amount",
                        "{\"loanAmount\":0,\"birthDate\":\"2000-01-01\",\"paymentTermInMonths\":12}"
                ),
                Arguments.of(
                        "Negative loan amount",
                        "{\"loanAmount\":-100,\"birthDate\":\"2000-01-01\",\"paymentTermInMonths\":12}"
                ),
                Arguments.of(
                        "Null birth date",
                        "{\"loanAmount\":10000,\"paymentTermInMonths\":12}"
                ),
                Arguments.of(
                        "Future birth date",
                        "{\"loanAmount\":10000,\"birthDate\":\"2030-01-01\",\"paymentTermInMonths\":12}"
                ),
                Arguments.of(
                        "Zero payment term",
                        "{\"loanAmount\":10000,\"birthDate\":\"2000-01-01\",\"paymentTermInMonths\":0}"
                )
        );
    }
}