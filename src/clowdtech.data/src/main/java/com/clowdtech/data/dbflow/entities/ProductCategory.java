package com.clowdtech.data.dbflow.entities;

import com.clowdtech.data.dbflow.ApplicationDatabase;
import com.clowdtech.data.entities.Category;
import com.clowdtech.data.entities.IProduct;
import com.clowdtech.data.entities.IProductCategory;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = ApplicationDatabase.class, name = "ProductCategories")
public class ProductCategory extends BaseModel implements IProductCategory {

    @PrimaryKey(autoincrement = true)
    long Id;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "product", columnType = long.class, foreignKeyColumnName = "Id")}, saveForeignKeyModel = false)
    Product productForeignKeyContainer;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "category", columnType = long.class, foreignKeyColumnName = "Id")}, saveForeignKeyModel = false)
    CategoryDbf categoryForeignKeyContainer;

    public ProductCategory() {
        super();
    }

    public ProductCategory(IProduct product, Category category) {
        this.productForeignKeyContainer = (Product)product;
        this.categoryForeignKeyContainer = (CategoryDbf)category;
    }

    @Override
    public IProduct getProduct() {
        return productForeignKeyContainer;
    }

    @Override
    public Category getCategory() {
        return categoryForeignKeyContainer;
    }
}
