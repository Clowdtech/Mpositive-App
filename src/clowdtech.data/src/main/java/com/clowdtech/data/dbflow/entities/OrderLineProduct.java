package com.clowdtech.data.dbflow.entities;

import com.clowdtech.data.dbflow.ApplicationDatabase;
import com.clowdtech.data.dbflow.TypeConverter.DateTimeConverter;
import com.clowdtech.data.entities.IOrderLineProduct;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.joda.time.DateTime;

@Table(database = ApplicationDatabase.class, name = "OrderLinesProduct")
public class OrderLineProduct extends BaseModel implements IOrderLineProduct {

    @PrimaryKey(autoincrement = true)
    long Id;

    @Column(name = "ProductId")
    long productId;

    @Column(name = "CreatedDate", typeConverter = DateTimeConverter.class)
    DateTime createdDate;

    @Column(name = "Quantity")
    int quantity;

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
    public long getProductId() {
        return productId;
    }

    @Override
    public void setProductId(long productId) {
        this.productId = productId;
    }

    @Override
    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
