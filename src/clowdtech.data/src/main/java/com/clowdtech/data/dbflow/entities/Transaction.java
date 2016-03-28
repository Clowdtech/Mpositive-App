package com.clowdtech.data.dbflow.entities;

import com.clowdtech.data.dbflow.ApplicationDatabase;
import com.clowdtech.data.dbflow.TypeConverter.DateTimeConverter;
import com.clowdtech.data.entities.ITransactionLineManual;
import com.clowdtech.data.entities.IPayment;
import com.clowdtech.data.entities.ITransactionLineProduct;
import com.clowdtech.data.entities.ITransaction;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

@ModelContainer
@Table(database = ApplicationDatabase.class, name = "Transactions")
public class Transaction extends BaseModel implements ITransaction {

    @PrimaryKey(autoincrement = true)
    long Id;

    @Column(typeConverter = DateTimeConverter.class)
    DateTime CreatedDate;

    @Column(defaultValue = "0")
    boolean Refunded;

    // needs to be accessible for DELETE
    List<ITransactionLineProduct> productTransactionLines;

    public List<ITransactionLineProduct> productLines() {
        if (productTransactionLines == null || productTransactionLines.isEmpty()) {
            List<TransactionLineProduct> lines = SQLite.select()
                    .from(TransactionLineProduct.class)
                    .where(TransactionLineProduct_Table.TransactionId.eq(Id))
                    .queryList();

            this.productTransactionLines = new ArrayList<>();

            for (TransactionLineProduct line :
                    lines) {
                this.productTransactionLines.add(line);
            }
        }

        return productTransactionLines;
    }

    List<ITransactionLineManual> manualTransactionLines;

    public List<ITransactionLineManual> manualLines() {
        if (manualTransactionLines == null || manualTransactionLines.isEmpty()) {
            List<TransactionLineManual> lines = SQLite.select()
                    .from(TransactionLineManual.class)
                    .where(TransactionLineManual_Table.TransactionId.eq(Id))
                    .queryList();

            this.manualTransactionLines = new ArrayList<>();

            for (TransactionLineManual line :
                    lines) {
                this.manualTransactionLines.add(line);
            }
        }

        return manualTransactionLines;
    }



    @Override
    public Long getId() {
        return Id;
    }

    @Override
    public DateTime getCreatedDate() {
        return CreatedDate;
    }

    @Override
    public IPayment getPayment() {
        return SQLite.select()
                .from(Payment.class)
                .where(Payment_Table.TransactionId.eq(Id))
                .querySingle();
    }

    @Override
    public Boolean getRefunded() {
        return Refunded;
    }

    @Override
    public void setRefunded() {
        Refunded = true;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.CreatedDate = createdDate;
    }
}
