package com.github.renatinhah.backend_credit_simulator.controller;

import com.github.renatinhah.backend_credit_simulator.dto.LoanSimulationRequest;
import com.github.renatinhah.backend_credit_simulator.dto.LoanSimulationResponse;
import com.github.renatinhah.backend_credit_simulator.service.LoanSimulationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/simulations")
public class LoanSimulationController {

    private final LoanSimulationService loanSimulationService;

    public LoanSimulationController(LoanSimulationService loanSimulationService) {
        this.loanSimulationService = loanSimulationService;
    }

    @PostMapping
    public ResponseEntity<LoanSimulationResponse> simulateLoan(@RequestBody LoanSimulationRequest request) {
        LoanSimulationResponse response = loanSimulationService.simulate(request);
        return ResponseEntity.ok(response);
    }
}
