package com.github.renatinhah.backend_credit_simulator.model.enums;

import com.github.renatinhah.backend_credit_simulator.exceptions.AgeNotSupportedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class InterestRateEnumTest {

    private static final AgeRateTestCase[] CRITICAL_AGE_TEST_CASES = {
            new AgeRateTestCase(0, InterestRateEnum.UP_TO_25_YEARS, "Lower bound first range"),
            new AgeRateTestCase(25, InterestRateEnum.UP_TO_25_YEARS, "Upper bound first range"),
            new AgeRateTestCase(26, InterestRateEnum.FROM_26_TO_40_YEARS, "Lower bound second range"),
            new AgeRateTestCase(40, InterestRateEnum.FROM_26_TO_40_YEARS, "Upper bound second range"),
            new AgeRateTestCase(41, InterestRateEnum.FROM_41_TO_60_YEARS, "Lower bound third range"),
            new AgeRateTestCase(60, InterestRateEnum.FROM_41_TO_60_YEARS, "Upper bound third range"),
            new AgeRateTestCase(61, InterestRateEnum.ABOVE_60_YEARS, "Lower bound fourth range"),

            // Alguns casos representativos
            new AgeRateTestCase(20, InterestRateEnum.UP_TO_25_YEARS, "Young adult"),
            new AgeRateTestCase(35, InterestRateEnum.FROM_26_TO_40_YEARS, "Mid-career"),
            new AgeRateTestCase(50, InterestRateEnum.FROM_41_TO_60_YEARS, "Pre-retirement"),
            new AgeRateTestCase(100, InterestRateEnum.ABOVE_60_YEARS, "Edge case - centenarian")
    };

    @ParameterizedTest(name = "Age {0} should return {1} ({2})")
    @MethodSource("validAgeTestCases")
    @DisplayName("Should return correct interest rate enum for valid ages")
    void shouldReturnCorrectRateForValidAges(AgeRateTestCase testCase) {
        // When
        InterestRateEnum result = InterestRateEnum.getRateByAge(testCase.age());

        // Then
        assertThat(result).isEqualTo(testCase.expectedEnum());
        assertThat(result.getRate()).isEqualTo(testCase.expectedEnum().getRate());
    }

    @ParameterizedTest(name = "Invalid age: {0}")
    @ValueSource(ints = {-10, -5, -1})
    @DisplayName("Should throw AgeNotSupportedException for invalid ages")
    void shouldThrowExceptionForInvalidAges(int invalidAge) {
        // When & Then
        AgeNotSupportedException exception = assertThrows(
                AgeNotSupportedException.class,
                () -> InterestRateEnum.getRateByAge(invalidAge)
        );

        assertThat(exception.getMessage())
                .isEqualTo(String.format("Age range not found for age: %d", invalidAge));
    }

    @ParameterizedTest(name = "Enum {0} should have correct properties")
    @EnumSource(InterestRateEnum.class)
    @DisplayName("Should have correct properties for all enum values")
    void shouldHaveCorrectPropertiesForAllEnums(InterestRateEnum enumValue) {
        // Then - Verificar propriedades básicas
        assertThat(enumValue.ageStart).isGreaterThanOrEqualTo(0);
        assertThat(enumValue.ageEnd).isGreaterThanOrEqualTo(enumValue.ageStart);
        assertThat(enumValue.getRate()).isPositive();

        // Verificar valores específicos conhecidos
        switch (enumValue) {
            case UP_TO_25_YEARS:
                assertEnumProperties(enumValue, 0, 25, 0.05);
                break;
            case FROM_26_TO_40_YEARS:
                assertEnumProperties(enumValue, 26, 40, 0.03);
                break;
            case FROM_41_TO_60_YEARS:
                assertEnumProperties(enumValue, 41, 60, 0.02);
                break;
            case ABOVE_60_YEARS:
                assertEnumProperties(enumValue, 61, Integer.MAX_VALUE, 0.04);
                break;
        }
    }

    private static Stream<AgeRateTestCase> validAgeTestCases() {
        return Arrays.stream(CRITICAL_AGE_TEST_CASES);
    }

    private void assertEnumProperties(InterestRateEnum enumValue, int expectedAgeStart,
                                      int expectedAgeEnd, double expectedRate) {
        assertThat(enumValue.ageStart).isEqualTo(expectedAgeStart);
        assertThat(enumValue.ageEnd).isEqualTo(expectedAgeEnd);
        assertThat(enumValue.getRate()).isEqualTo(expectedRate);
    }

    // Record para encapsular casos de teste
    private record AgeRateTestCase(
            int age,
            InterestRateEnum expectedEnum,
            String description
    ) {}
}