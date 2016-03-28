package com.clowdtech.data.dbflow.entities;

import com.clowdtech.data.dbflow.ApplicationDatabase;
import com.clowdtech.data.entities.Category;
import com.clowdtech.data.entities.CategoryTile;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = ApplicationDatabase.class, name = "Categories")
public class CategoryDbf extends BaseModel implements Category {

    @PrimaryKey(autoincrement = true)
    long Id;

    @Column(name = "Name")
    String name;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "Tile", columnType = long.class, foreignKeyColumnName = "Id")}, saveForeignKeyModel = false)
    CategoryTileDbf tileForeignKeyContainer;

    public CategoryDbf() {
        super();
    }

    public CategoryDbf(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public CategoryTileDbf getTile() {
        return this.tileForeignKeyContainer;
    }

    @Override
    public void setTile(CategoryTile tile) {
        this.tileForeignKeyContainer = (CategoryTileDbf)tile;
    }

    @Override
    public Long getId() {
        return Id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setTile(CategoryTileDbf tile) {
        this.tileForeignKeyContainer = tile;
    }

    public void associateTile(CategoryTileDbf tile) {
        tileForeignKeyContainer = tile;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof CategoryDbf))return false;

        CategoryDbf compareTo = (CategoryDbf) other;

        return this.Id == compareTo.Id;
    }
}
