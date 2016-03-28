package com.clowdtech.data.dbflow.entities;

import com.clowdtech.data.dbflow.ApplicationDatabase;
import com.clowdtech.data.dbflow.TypeConverter.CurrencyConverter;
import com.clowdtech.data.dbflow.TypeConverter.DateTimeConverter;
import com.clowdtech.data.entities.ITransactionLineManual;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.joda.time.DateTime;

import java.math.BigDecimal;

@Table(database = ApplicationDatabase.class, name = "ManualTransactionLines")
public class TransactionLineManual extends BaseModel implements ITransactionLineManual {
    @PrimaryKey(autoincrement = true)
    long Id;

    @Column(name = "ManualName")
    String manualName;

    @Column(name = "ManualPrice", typeConverter = CurrencyConverter.class)
    BigDecimal manualPrice;

    @Column(name = "DateCreated", typeConverter = DateTimeConverter.class)
    DateTime dateCreated;


    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "TransactionId", columnType = long.class, foreignKeyColumnName = "Id")}, saveForeignKeyModel = false)
    Transaction transactionForeignKeyContainer;

    public void associateTransaction(Transaction transaction) {
        transactionForeignKeyContainer = transaction;
    }





    @Override
    public DateTime getDateCreated() {
        return dateCreated;
    }

    @Override
    public String getName() {
        return manualName;
    }

    @Override
    public BigDecimal getPrice() {
        return manualPrice;
    }

    public void setName(String name) {
        this.manualName = name;
    }

    public void setPrice(BigDecimal price) {
        this.manualPrice = price;
    }

    public void setDateCreated(DateTime dateCreated) {
        this.dateCreated = dateCreated;
    }
}
