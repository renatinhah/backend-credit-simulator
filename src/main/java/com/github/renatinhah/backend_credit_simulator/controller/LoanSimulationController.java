package com.github.renatinhah.backend_credit_simulator.controller;

import com.github.renatinhah.backend_credit_simulator.dto.LoanSimulationRequest;
import com.github.renatinhah.backend_credit_simulator.dto.LoanSimulationResponse;
import com.github.renatinhah.backend_credit_simulator.exceptions.LoanSimulationException;
import com.github.renatinhah.backend_credit_simulator.service.LoanSimulationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/simulations")
@RequiredArgsConstructor
public class LoanSimulationController {

    private final LoanSimulationService loanSimulationService;

    @PostMapping
    public ResponseEntity<LoanSimulationResponse> simulateLoan(@Valid @RequestBody LoanSimulationRequest request) throws LoanSimulationException {
        LoanSimulationResponse response = loanSimulationService.simulate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<LoanSimulationResponse>> simulateBulk(@Valid @RequestBody List<LoanSimulationRequest> requests) {
        List<LoanSimulationResponse> responses = loanSimulationService.simulateBulk(requests);
        return ResponseEntity.ok(responses);
    }

}
