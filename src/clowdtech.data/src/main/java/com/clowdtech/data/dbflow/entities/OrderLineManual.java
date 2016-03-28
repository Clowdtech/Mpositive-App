package com.clowdtech.data.dbflow.entities;

import com.clowdtech.data.dbflow.ApplicationDatabase;
import com.clowdtech.data.dbflow.TypeConverter.CurrencyConverter;
import com.clowdtech.data.dbflow.TypeConverter.DateTimeConverter;
import com.clowdtech.data.entities.IOrderLineManual;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.joda.time.DateTime;

import java.math.BigDecimal;

@Table(database = ApplicationDatabase.class, name = "OrderLinesManual")
public class OrderLineManual extends BaseModel implements IOrderLineManual {

    @PrimaryKey(autoincrement = true)
    long Id;

    @Column(name = "CreatedDate", typeConverter = DateTimeConverter.class)
    DateTime createdDate;

    @Column(name = "Name")
    String name;

    @Column(name = "Price", typeConverter = CurrencyConverter.class)
    BigDecimal price;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "OrderId", columnType = long.class, foreignKeyColumnName = "Id")}, saveForeignKeyModel = false)
    Order orderForeignKeyContainer;

    public void associateOrder(Order order) {
        orderForeignKeyContainer = order;
    }

    @Override
    public DateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public void setPrice(BigDecimal manualPrice) {
        this.price = manualPrice;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
