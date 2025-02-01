package gt.com.megatech.persistence.entity.enums;

import java.time.Month;

public enum MonthEnum {
    JANUARY,
    FEBRUARY,
    MARCH,
    APRIL,
    MAY,
    JUNE,
    JULY,
    AUGUST,
    SEPTEMBER,
    OCTOBER,
    NOVEMBER,
    DECEMBER;

    public static MonthEnum fromMonthValue(
            int monthValue
    ) {
        if (monthValue < 1 || monthValue > 12) {
            throw new IllegalArgumentException(
                    "Month value must be between 1 and 12"
            );
        }
        return MonthEnum.values()[monthValue - 1];
    }

    public static MonthEnum fromJavaMonth(
            Month month
    ) {
        return MonthEnum.values()[month.getValue() - 1];
    }
}
