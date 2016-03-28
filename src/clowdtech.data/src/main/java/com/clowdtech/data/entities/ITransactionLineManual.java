package com.clowdtech.data.entities;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public interface ITransactionLineManual {
    DateTime getDateCreated();

    String getName();

    BigDecimal getPrice();
}
