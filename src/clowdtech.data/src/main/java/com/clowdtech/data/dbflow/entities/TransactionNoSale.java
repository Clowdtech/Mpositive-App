package com.clowdtech.data.dbflow.entities;

import com.clowdtech.data.dbflow.ApplicationDatabase;
import com.clowdtech.data.dbflow.TypeConverter.DateTimeConverter;
import com.clowdtech.data.entities.ITransactionNoSale;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.joda.time.DateTime;

@Table(database = ApplicationDatabase.class, name = "TransactionNoSales")
public class TransactionNoSale extends BaseModel implements ITransactionNoSale {

    @PrimaryKey(autoincrement = true)
    long Id;

    @Column(name = "CreatedDate", typeConverter = DateTimeConverter.class)
    public DateTime createdDate;

    @Override
    public DateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(DateTime datetime) {
        this.createdDate = datetime;
    }
}
