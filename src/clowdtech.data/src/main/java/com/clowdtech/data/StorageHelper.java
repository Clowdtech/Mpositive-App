package com.clowdtech.data;

import java.math.BigDecimal;

public class StorageHelper {
    public static BigDecimal getBigDecimalFromInt(long value) {
        BigDecimal boxed = new BigDecimal(value);

        BigDecimal division = new BigDecimal(100);

        return boxed.divide(division, 2, BigDecimal.ROUND_CEILING);
    }
}
