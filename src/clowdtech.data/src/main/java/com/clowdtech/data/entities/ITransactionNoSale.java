package com.clowdtech.data.entities;

import org.joda.time.DateTime;

public interface ITransactionNoSale {
    DateTime getCreatedDate();

    void setCreatedDate(DateTime datetime);
}
