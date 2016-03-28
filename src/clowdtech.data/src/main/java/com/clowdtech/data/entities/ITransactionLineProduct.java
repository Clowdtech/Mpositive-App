package com.clowdtech.data.entities;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public interface ITransactionLineProduct {
    long getProductId();

    DateTime getDateCreated();

    String getName();

    BigDecimal getUnitPrice();

    int getQuantity();
}
