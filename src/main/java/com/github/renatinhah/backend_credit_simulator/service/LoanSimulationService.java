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
import java.util.Objects;

@Service
public class LoanSimulationService {
    public static final int MONTHS_IN_YEAR = 12;
    private static final MathContext MATH_CONTEXT = new MathContext(34, RoundingMode.HALF_UP);

    public LoanSimulationResponse simulate(LoanSimulationRequest request) throws LoanSimulationException {
        try {
            BigDecimal annualRate;
            if (Objects.isNull(request.getVariableInterestRate())) {
                int age = calculateAge(request.getBirthDate());
                annualRate = new BigDecimal(InterestRateEnum.getRateByAge(age).getRate());
            } else {
                annualRate = request.getVariableInterestRate();
            }

            BigDecimal monthlyPayment = calculateInstallment(request, annualRate);

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
        BigDecimal monthRate = rate.divide(new BigDecimal(MONTHS_IN_YEAR), MATH_CONTEXT);

        BigDecimal loanAmount = request.getLoanAmount();
        int numberOfPayments = request.getPaymentTermInMonths();
        BigDecimal sumRateOne = BigDecimal.ONE.add(monthRate, MATH_CONTEXT); // (1 + r)

        // (1 + r)^(-n)   ->     1 / (1 + r)^n
        BigDecimal inversePower = BigDecimal.ONE.divide(
                sumRateOne.pow(numberOfPayments, MATH_CONTEXT),
                MATH_CONTEXT
        );
        BigDecimal divisor = BigDecimal.ONE.subtract(inversePower, MATH_CONTEXT); // 1 - (1 + r)^(-n)
        BigDecimal dividend = loanAmount.multiply(monthRate, MATH_CONTEXT); // PV * r

        return dividend.divide(divisor, 2, RoundingMode.HALF_UP);
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
