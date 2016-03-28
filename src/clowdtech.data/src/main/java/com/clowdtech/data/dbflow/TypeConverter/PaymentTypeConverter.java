package com.clowdtech.data.dbflow.TypeConverter;

import com.clowdtech.data.entities.PaymentTypes;
import com.raizlabs.android.dbflow.converter.TypeConverter;

@SuppressWarnings("unused")
@com.raizlabs.android.dbflow.annotation.TypeConverter
public final class PaymentTypeConverter extends TypeConverter<Integer, PaymentTypes> {

    @Override
    public Integer getDBValue(PaymentTypes model) {
        if (model != null) {
            return model.getValue();
        }

        return null;
    }

    @Override
    public PaymentTypes getModelValue(Integer data) {
        if (data != null) {
            switch (data) {
                case 0:
                    return PaymentTypes.Cash;
                case 1:
                    return PaymentTypes.Card;
                case 2:
                    return PaymentTypes.Other;
            }
        }

        return null;
    }
}
