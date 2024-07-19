package org.nott.enums;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * @author Nott
 * @date 2024-7-17
 */

public enum PeriodUnit {

    MINUTE("m", ChronoUnit.MINUTES),
    DAY("D", ChronoUnit.DAYS),
    WEEK("W", ChronoUnit.WEEKS),
    MONTH("M", ChronoUnit.MONTHS),
    YEAR("Y", ChronoUnit.YEARS);

    private String name;

    private TemporalUnit value;

    PeriodUnit(String name, TemporalUnit value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public TemporalUnit getValue() {
        return value;
    }

    public static TemporalUnit getByName(String name){
        return PeriodUnit.valueOf(name).getValue();

    }
}
