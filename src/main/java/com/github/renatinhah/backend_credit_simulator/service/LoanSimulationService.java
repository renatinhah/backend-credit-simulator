package com.github.renatinhah.backend_credit_simulator.service;

import com.github.renatinhah.backend_credit_simulator.dto.LoanSimulationDetails;
import com.github.renatinhah.backend_credit_simulator.dto.LoanSimulationRequest;
import com.github.renatinhah.backend_credit_simulator.dto.LoanSimulationResponse;
import com.github.renatinhah.backend_credit_simulator.exceptions.LoanSimulationException;
import com.github.renatinhah.backend_credit_simulator.model.enums.InterestRateEnum;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;

@Service
public class LoanSimulationService {
    public static final int MONTHS_IN_YEAR = 12;

    public LoanSimulationResponse simulate(LoanSimulationRequest request) throws LoanSimulationException {
        try {
            int age = calculateAge(request.getBirthDate());
            InterestRateEnum rate = InterestRateEnum.getRateByAge(age);

            BigDecimal monthlyPayment = calculateInstallment(request, new BigDecimal(rate.rate));

            LoanSimulationDetails details = calculateSimulationDetails(request, monthlyPayment);

            return createLoanSimulationResponse(monthlyPayment, details);
        } catch (Exception e) {
            throw new LoanSimulationException(request.getLoanAmount(), request.getPaymentTermInMonths());
        }
    }

    private int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    private BigDecimal calculateInstallment(LoanSimulationRequest request, BigDecimal rate) {
        BigDecimal monthRate = rate.divide(new BigDecimal(MONTHS_IN_YEAR), RoundingMode.HALF_UP);

        BigDecimal loanAmount = request.getLoanAmount();
        int numberOfPayments = request.getPaymentTermInMonths();

        BigDecimal sumRateOne = new BigDecimal(1).add(monthRate); // (1 + r)
        BigDecimal potencia = sumRateOne.pow(-numberOfPayments, MathContext.DECIMAL128); // (1 + r)^(-n)
        BigDecimal divisor = new BigDecimal(1).subtract(potencia); // 1 - (1 + r)^(-n)
        BigDecimal dividend = loanAmount.multiply(monthRate); // (PV * r)

        return dividend.divide(divisor, MathContext.DECIMAL128); // (PV * r) / (1 - (1 + r)^(-n))
    }

    private LoanSimulationDetails calculateSimulationDetails(LoanSimulationRequest request, BigDecimal monthlyPayment) {
        BigDecimal totalAmountToPay = monthlyPayment.multiply(new BigDecimal(request.getPaymentTermInMonths()));
        totalAmountToPay = totalAmountToPay.setScale(2, RoundingMode.HALF_UP);

        BigDecimal totalInterest = totalAmountToPay.subtract(request.getLoanAmount());
        totalInterest = totalInterest.setScale(2, RoundingMode.HALF_UP);

        return LoanSimulationDetails.builder()
                .totalAmountToPay(totalAmountToPay)
                .totalInterest(totalInterest)
                .build();
    }

    private LoanSimulationResponse createLoanSimulationResponse(BigDecimal monthlyPayment,LoanSimulationDetails details) {
        return LoanSimulationResponse.builder()
                .totalAmountToPay(details.getTotalAmountToPay())
                .monthlyInstallment(monthlyPayment.setScale(2, RoundingMode.HALF_UP))
                .totalInterest(details.getTotalInterest())
                .build();
    }

}
