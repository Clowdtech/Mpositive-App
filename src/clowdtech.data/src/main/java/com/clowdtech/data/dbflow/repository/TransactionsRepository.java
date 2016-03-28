package com.clowdtech.data.dbflow.repository;

import com.clowdtech.data.DateRange;
import com.clowdtech.data.dbflow.entities.TransactionLineManual;
import com.clowdtech.data.dbflow.entities.Payment;
import com.clowdtech.data.dbflow.entities.TransactionLineProduct;
import com.clowdtech.data.dbflow.entities.Transaction;
import com.clowdtech.data.dbflow.entities.TransactionNoSale;
import com.clowdtech.data.dbflow.entities.TransactionNoSale_Table;
import com.clowdtech.data.dbflow.entities.Transaction_Table;
import com.clowdtech.data.entities.ITransactionLineManual;
import com.clowdtech.data.entities.ITransactionLineProduct;
import com.clowdtech.data.entities.ITransaction;
import com.clowdtech.data.entities.ITransactionNoSale;
import com.clowdtech.data.entities.PaymentTypes;
import com.clowdtech.data.repository.ITransactionsRepository;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Where;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TransactionsRepository implements ITransactionsRepository {
    @Override
    public long addTransaction(
            List<ITransactionLineManual> manualEntries,
            Collection<ITransactionLineProduct> products,
            BigDecimal paymentAmount,
            PaymentTypes paymentType) {

        Transaction transaction = new Transaction();

        transaction.setCreatedDate(DateTime.now());

        transaction.save();

        for (ITransactionLineManual manualLine : manualEntries) {
            TransactionLineManual mtl = new TransactionLineManual();
            mtl.setName(manualLine.getName());
            mtl.setPrice(manualLine.getPrice());
            mtl.setDateCreated(manualLine.getDateCreated());
            mtl.associateTransaction(transaction);
            mtl.save();
        }

        for (ITransactionLineProduct productLine : products) {
            TransactionLineProduct newPtl = new TransactionLineProduct();
            newPtl.setQuantity(productLine.getQuantity());
            newPtl.setProductId(productLine.getProductId());
            newPtl.setName(productLine.getName());
            newPtl.setPrice(productLine.getUnitPrice());
            newPtl.setDateCreated(productLine.getDateCreated());
            newPtl.associateTransaction(transaction);
            newPtl.save();
        }

        Payment payment = new Payment();

        payment.setAmount(paymentAmount);
        payment.setPaymentType(paymentType);
        payment.setPaymentDate(transaction.getCreatedDate()); // doing this for now so they are consistent
        payment.associateTransaction(transaction);
        payment.save();

        return transaction.getId();
    }

    @Override
    public void registerNoSale() {
        TransactionNoSale noSale = new TransactionNoSale();

        noSale.setCreatedDate(DateTime.now());

        noSale.insert();
    }

    @Override
    public DateRange getLastTenDateRange() {
        List<Transaction> lastTenTransactions = SQLite.select()
                .from(Transaction.class)
                .orderBy(Transaction_Table.CreatedDate, false)
                .limit(10)
                .queryList();

        long nowMillis = DateTime.now().getMillis();

        if (lastTenTransactions.size() > 0) {
            return new DateRange(lastTenTransactions.get(lastTenTransactions.size() - 1).getCreatedDate().getMillis(), nowMillis);
        } else {
            return new DateRange(nowMillis, nowMillis);
        }
    }

    @Override
    public void refundTransaction(Long id) {
        Where<Transaction> update = SQLite.update(Transaction.class)
                .set(Transaction_Table.Refunded.eq(true))
                .where(Transaction_Table.Id.is(id));

        update.queryClose();
    }

    @Override
    public ITransaction getTransaction(long id) {
        return SQLite.select()
                .from(Transaction.class)
                .where(Transaction_Table.Id.eq(id))
                .querySingle();
    }

    @Override
    public List<ITransaction> getTransactionsBetween(long startTime, long endTime) {
        List<Transaction> transactions = SQLite.select()
                .from(Transaction.class)
                .where(Transaction_Table.CreatedDate.greaterThanOrEq(new DateTime().withMillis(startTime)))
                .and(Transaction_Table.CreatedDate.lessThanOrEq(new DateTime().withMillis(endTime)))
                .orderBy(Transaction_Table.CreatedDate, false)
                .queryList();

        List<ITransaction> returnTransactions = new ArrayList<>();

        for (Transaction transaction :
                transactions) {
            returnTransactions.add(transaction);
        }

        return returnTransactions;
    }

    @Override
    public List<ITransactionNoSale> getNoSalesBetween(long startTime, long endTime) {
        List<TransactionNoSale> transactions = SQLite.select()
                .from(TransactionNoSale.class)
                .where(TransactionNoSale_Table.CreatedDate.greaterThanOrEq(new DateTime().withMillis(startTime)))
                .and(TransactionNoSale_Table.CreatedDate.lessThanOrEq(new DateTime().withMillis(endTime)))
                .orderBy(TransactionNoSale_Table.CreatedDate, false)
                .queryList();

        List<ITransactionNoSale> returnTransactions = new ArrayList<>();

        for (TransactionNoSale transaction :
                transactions) {
            returnTransactions.add(transaction);
        }

        return returnTransactions;
    }

    @Override
    public Boolean transactionsOlderThan(long time) {
        return SQLite.select()
                .from(Transaction.class)
                .where(Transaction_Table.CreatedDate.lessThanOrEq(new DateTime().withMillis(time)))
                .orderBy(Transaction_Table.CreatedDate, false)
                .hasData();
    }
}
