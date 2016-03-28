package com.clowdtech.data.repository;

import com.clowdtech.data.DateRange;
import com.clowdtech.data.entities.ITransactionLineManual;
import com.clowdtech.data.entities.ITransactionLineProduct;
import com.clowdtech.data.entities.ITransaction;
import com.clowdtech.data.entities.ITransactionNoSale;
import com.clowdtech.data.entities.PaymentTypes;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public interface ITransactionsRepository {
    long addTransaction(
            List<ITransactionLineManual> manualEntries,
            Collection<ITransactionLineProduct> products,
            BigDecimal paymentAmount,
            PaymentTypes paymentType);

    ITransaction getTransaction(long id);

    List<ITransaction> getTransactionsBetween(long startTime, long endTime);

    List<ITransactionNoSale> getNoSalesBetween(long startTime, long endTime);

    Boolean transactionsOlderThan(long time);

    void registerNoSale();

    DateRange getLastTenDateRange();

    void refundTransaction(Long id);
}
