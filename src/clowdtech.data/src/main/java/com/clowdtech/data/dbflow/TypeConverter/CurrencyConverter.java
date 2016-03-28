package com.clowdtech.data.dbflow.TypeConverter;

import com.raizlabs.android.dbflow.converter.TypeConverter;

import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 * This class is used to automatically convert DateTime object to String
 * and backwards during read/write operations to database
 */
@SuppressWarnings("unused")
@com.raizlabs.android.dbflow.annotation.TypeConverter
public final class CurrencyConverter extends TypeConverter<Integer, BigDecimal> {

    @Override
    public Integer getDBValue(BigDecimal model) {
        if (model != null) {
            return model.multiply(new BigDecimal(100)).intValue();
        }

        return null;
    }

    @Override
    public BigDecimal getModelValue(Integer data) {
        if (data != null) {
            return new BigDecimal(data).divide(new BigDecimal(100), 2, BigDecimal.ROUND_CEILING);
        }

        return null;
    }
}
