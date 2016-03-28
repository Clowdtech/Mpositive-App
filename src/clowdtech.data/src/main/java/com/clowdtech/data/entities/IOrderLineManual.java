package com.clowdtech.data.entities;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public interface IOrderLineManual {
    DateTime getCreatedDate();

    void setCreatedDate(DateTime dateCreated);

    BigDecimal getPrice();

    void setPrice(BigDecimal manualPrice);

    String getName();

    void setName(String name);
}
