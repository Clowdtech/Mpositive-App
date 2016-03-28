package com.clowdtech.data.entities;

import org.joda.time.DateTime;

import java.util.List;

public interface IOrder {
    long getOrderId();

    List<IOrderLineProduct> productLines();

    List<IOrderLineManual> manualLines();

    String getReference();

    void setReference(String reference);

    DateTime getCreatedDate();

    void setCreatedDate(DateTime createdDate);
}
