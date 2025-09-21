package com.github.renatinhah.backend_credit_simulator.model.enums;

import com.github.renatinhah.backend_credit_simulator.exceptions.AgeNotSupportedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InterestRateEnumTest {
    @Test
    void shouldReturnCorrectRateForAgeUpTo25Years() {
        // Given
        int age = 20;

        // When
        InterestRateEnum result = InterestRateEnum.getRateByAge(age);

        // Then
        assertEquals(InterestRateEnum.UP_TO_25_YEARS, result);
        assertEquals(0.05, result.getRate());
    }

    @Test
    void shouldReturnCorrectRateForAgeBetween26And40Years() {
        // Given
        int age = 30;

        // When
        InterestRateEnum result = InterestRateEnum.getRateByAge(age);

        // Then
        assertEquals(InterestRateEnum.FROM_26_TO_40_YEARS, result);
        assertEquals(0.03, result.getRate());
    }

    @Test
    void shouldReturnCorrectRateForAgeBetween41And60Years() {
        // Given
        int age = 50;

        // When
        InterestRateEnum result = InterestRateEnum.getRateByAge(age);

        // Then
        assertEquals(InterestRateEnum.FROM_41_TO_60_YEARS, result);
        assertEquals(0.02, result.getRate());
    }

    @Test
    void shouldReturnCorrectRateForAgeAbove60Years() {
        // Given
        int age = 65;

        // When
        InterestRateEnum result = InterestRateEnum.getRateByAge(age);

        // Then
        assertEquals(InterestRateEnum.ABOVE_60_YEARS, result);
        assertEquals(0.04, result.getRate());
    }

    @Test
    void shouldThrowExceptionForAgeBelowSupportedRange() {
        // Given
        int age = -1;

        // When & Then
        AgeNotSupportedException exception = assertThrows(AgeNotSupportedException.class, () -> {
            InterestRateEnum.getRateByAge(age);
        });

        assertEquals("Age range not found for age: -1", exception.getMessage());
    }

    @Test
    @DisplayName("should have correct properties for UP_TO_25_YEARS")
    void shouldHaveCorrectPropertiesForUpTo25Years() {
        // Given
        InterestRateEnum enumValue = InterestRateEnum.UP_TO_25_YEARS;

        // Then
        assertEquals(0, enumValue.ageStart);
        assertEquals(25, enumValue.ageEnd);
        assertEquals(0.05, enumValue.rate);
    }

}