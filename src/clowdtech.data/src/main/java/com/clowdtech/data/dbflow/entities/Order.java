package com.clowdtech.data.dbflow.entities;

import com.clowdtech.data.dbflow.ApplicationDatabase;
import com.clowdtech.data.dbflow.TypeConverter.DateTimeConverter;
import com.clowdtech.data.entities.IOrder;
import com.clowdtech.data.entities.IOrderLineManual;
import com.clowdtech.data.entities.IOrderLineProduct;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

@Table(database = ApplicationDatabase.class, name = "Orders")
public class Order extends BaseModel implements IOrder {

    @PrimaryKey(autoincrement = true)
    long Id;

    @Column(name = "Reference")
    String reference;

    @Column(name = "CreatedDate", typeConverter = DateTimeConverter.class)
    DateTime createdDate;


    public long getId() {
        return Id;
    }

    List<IOrderLineProduct> productLines;

    @Override
    public long getOrderId() {
        return Id;
    }

    @Override
    public List<IOrderLineProduct> productLines() {
        if (productLines == null || productLines.isEmpty()) {
            List<OrderLineProduct> lines = SQLite.select()
                    .from(OrderLineProduct.class)
                    .where(OrderLineProduct_Table.OrderId.eq(Id))
                    .queryList();

            this.productLines = new ArrayList<>();

            for (OrderLineProduct line :
                    lines) {
                this.productLines.add(line);
            }
        }

        return productLines;
    }

    List<IOrderLineManual> manualLines;

    @Override
    public List<IOrderLineManual> manualLines() {
        if (manualLines == null || manualLines.isEmpty()) {
            List<OrderLineManual> lines = SQLite.select()
                    .from(OrderLineManual.class)
                    .where(OrderLineManual_Table.OrderId.eq(Id))
                    .queryList();

            this.manualLines = new ArrayList<>();

            for (OrderLineManual line :
                    lines) {
                this.manualLines.add(line);
            }
        }

        return manualLines;
    }

    @Override
    public String getReference() {
        return reference;
    }

    @Override
    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public DateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }
}
