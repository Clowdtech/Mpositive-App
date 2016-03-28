package com.clowdtech.data.entities;

import org.joda.time.DateTime;

import java.util.List;

public interface ITransaction {
    List<ITransactionLineProduct> productLines();

    List<ITransactionLineManual> manualLines();

    Long getId();

    DateTime getCreatedDate();

    IPayment getPayment();

    Boolean getRefunded();

    void setRefunded();
}
