package com.clowdtech.data.dbflow.entities;

import com.clowdtech.data.dbflow.ApplicationDatabase;
import com.clowdtech.data.dbflow.TypeConverter.CurrencyConverter;
import com.clowdtech.data.dbflow.TypeConverter.DateTimeConverter;
import com.clowdtech.data.entities.ITransactionLineProduct;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.joda.time.DateTime;

import java.math.BigDecimal;

@Table(database = ApplicationDatabase.class, name = "ProductTransactionLines")
public class TransactionLineProduct extends BaseModel implements ITransactionLineProduct {
    @PrimaryKey(autoincrement = true)
    long Id;

    @Column(name = "ProductId")
    long ProductId;

    @Column(name = "ProductName")
    String ProductName;

    @Column(name = "ProductPrice", typeConverter = CurrencyConverter.class)
    BigDecimal ProductPrice;

    @Column(name = "DateCreated", typeConverter = DateTimeConverter.class)
    DateTime DateCreated;

    @Column(name = "Quantity")
    int Quantity;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "TransactionId", columnType = long.class, foreignKeyColumnName = "Id")}, saveForeignKeyModel = false)
    Transaction transactionForeignKeyContainer;

    public void associateTransaction(Transaction transaction) {
        transactionForeignKeyContainer = transaction;
    }





    @Override
    public DateTime getDateCreated() {
        return DateCreated;
    }

    @Override
    public long getProductId() {
        return ProductId;
    }

    @Override
    public String getName() {
        return ProductName;
    }

    @Override
    public BigDecimal getUnitPrice() {
        return ProductPrice;
    }

    @Override
    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        this.Quantity = quantity;
    }

    public void setProductId(long productId) {
        this.ProductId = productId;
    }

    public void setName(String name) {
        this.ProductName = name;
    }

    public void setPrice(BigDecimal price) {
        this.ProductPrice = price;
    }

    public void setDateCreated(DateTime dateCreated) {
        this.DateCreated = dateCreated;
    }
}
