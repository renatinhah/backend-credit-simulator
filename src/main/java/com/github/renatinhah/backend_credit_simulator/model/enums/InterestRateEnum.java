package com.github.renatinhah.backend_credit_simulator.model.enums;

import com.github.renatinhah.backend_credit_simulator.exceptions.AgeNotSupportedException;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum InterestRateEnum {

    UP_TO_25_YEARS(0, 25, 0.05),
    FROM_26_TO_40_YEARS(26, 40, 0.03),
    FROM_41_TO_60_YEARS(41, 60, 0.02),
    ABOVE_60_YEARS(61, Integer.MAX_VALUE, 0.04);

    public final int ageStart;
    public final int ageEnd;
    public final double rate;

    InterestRateEnum(int ageStart, int ageEnd, double rate) {
        this.ageStart = ageStart;
        this.ageEnd = ageEnd;
        this.rate = rate;
    }

    public static InterestRateEnum getRateByAge(int age) {
        for (InterestRateEnum rate : values()) {
            if (age >= rate.ageStart && age <= rate.ageEnd) {
                return rate;
            }
        }
        throw new AgeNotSupportedException(age);
    }


}
