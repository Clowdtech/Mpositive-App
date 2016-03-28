package com.clowdtech.data.dbflow.entities;

import com.clowdtech.data.StorageHelper;
import com.clowdtech.data.dbflow.ApplicationDatabase;
import com.clowdtech.data.dbflow.TypeConverter.DateTimeConverter;
import com.clowdtech.data.entities.IProductTile;
import com.clowdtech.data.entities.IProduct;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.NotNull;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.joda.time.DateTime;

import java.math.BigDecimal;

@Table(database = ApplicationDatabase.class, name = "Products")
public class Product extends BaseModel implements IProduct {

    @PrimaryKey(autoincrement = true)
    long Id;

    @Column(name = "Name")
    String name;

    @Column(name = "Price")
    int price;

    @Column(name = "Description")
    String description;

    @Column(name = "VAT")
    double vat;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "Tile", columnType = long.class, foreignKeyColumnName = "Id")}, saveForeignKeyModel = false)
    ProductTile tileForeignKeyContainer;

    @Column(name = "Deleted")
    boolean deleted;

    @Column(name = "RemoteId")
    @NotNull
    int remoteId;

    @Column(name = "LastUpdatedDate", typeConverter = DateTimeConverter.class)
    DateTime lastUpdatedDate;

    public static Product newInstance() {
        Product product = new Product();

        product.deleted = false;

        return product;
    }

    @Override
    public BigDecimal getPrice() {
        return StorageHelper.getBigDecimalFromInt(price);
    }

    @Override
    public void setPrice(BigDecimal price) {
        this.price = price.multiply(new BigDecimal(100)).intValue();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Boolean getDeleted() {
        return deleted;
    }

    @Override
    public Long getId() {
        return Id;
    }

    @Override
    public int getRemoteId() {
        return remoteId;
    }

    @Override
    public double getVat() {
        return vat;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setVat(double vat) {
        this.vat = vat;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setLastUpdatedDate(DateTime lastUpdate) {
        lastUpdatedDate = lastUpdate;
    }

    @Override
    public void setRemoteId(int id) {
        remoteId = id;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public IProductTile getTile() {
        return this.tileForeignKeyContainer;
    }

    @Override
    public void setTile(IProductTile tile) {
        this.tileForeignKeyContainer = (ProductTile)tile;
    }


    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Product))return false;

        Product compareTo = (Product) other;

        return this.getId().equals(compareTo.getId());
    }
}
