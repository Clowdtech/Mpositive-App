package com.clowdtech.data.dbflow.entities;


import com.clowdtech.data.dbflow.ApplicationDatabase;
import com.clowdtech.data.dbflow.TypeConverter.CurrencyConverter;
import com.clowdtech.data.dbflow.TypeConverter.DateTimeConverter;
import com.clowdtech.data.dbflow.TypeConverter.PaymentTypeConverter;
import com.clowdtech.data.entities.IPayment;
import com.clowdtech.data.entities.PaymentTypes;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;

import org.joda.time.DateTime;

import java.math.BigDecimal;

@ModelContainer
@Table(database = ApplicationDatabase.class, name = "Payments")
public class Payment extends BaseModel implements IPayment {
    @PrimaryKey(autoincrement = true)
    long Id;

    @Column(typeConverter = DateTimeConverter.class)
    DateTime PaymentDate;

    @Column(typeConverter = PaymentTypeConverter.class)
    PaymentTypes PaymentType;

    @Column(typeConverter = CurrencyConverter.class)
    BigDecimal Amount;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "TransactionId", columnType = long.class, foreignKeyColumnName = "Id")}, saveForeignKeyModel = false)
    Transaction transactionForeignKeyContainer;

    @Override
    public BigDecimal getAmount() {
        return Amount;
    }

    @Override
    public PaymentTypes getPaymentType() {
        return PaymentType;
    }

    public void setPaymentDate(DateTime paymentDate) {
        this.PaymentDate = paymentDate;
    }

    public void setAmount(BigDecimal amount) {
        this.Amount = amount;
    }

    public void setPaymentType(PaymentTypes paymentType) {
        this.PaymentType = paymentType;
    }

    public void associateTransaction(Transaction transaction) {
        transactionForeignKeyContainer = transaction;
    }
}

