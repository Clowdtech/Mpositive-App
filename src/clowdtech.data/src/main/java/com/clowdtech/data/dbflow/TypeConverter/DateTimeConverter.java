package com.clowdtech.data.dbflow.TypeConverter;

import com.raizlabs.android.dbflow.converter.TypeConverter;

import org.joda.time.DateTime;

/**
 * This class is used to automatically convert DateTime object to String
 * and backwards during read/write operations to database
 */
@SuppressWarnings("unused")
@com.raizlabs.android.dbflow.annotation.TypeConverter
public final class DateTimeConverter extends TypeConverter<Long, DateTime> {

    @Override
    public Long getDBValue(DateTime model) {
        if (model != null) {
            return model.getMillis();
        }

        return null;
    }

    @Override
    public DateTime getModelValue(Long data) {
        if (data != null) {
            return new DateTime().withMillis(data);
        }

        return null;
    }
}
