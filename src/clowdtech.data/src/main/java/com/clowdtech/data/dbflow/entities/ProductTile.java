package com.clowdtech.data.dbflow.entities;

import com.clowdtech.data.dbflow.ApplicationDatabase;
import com.clowdtech.data.entities.IProductTile;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.NotNull;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = ApplicationDatabase.class, name = "ProductTiles")
public class ProductTile extends BaseModel implements IProductTile {

    @PrimaryKey(autoincrement = true)
    long Id;

    @Column(name = "VisibleOnHomePage")
    @NotNull
    boolean visibleOnHomePage;

    @Column(name = "VisibleInCategory")
    @NotNull
    boolean visibleInCategory;

    @Column(name = "ForegroundColour")
    String foreground;

    @Column(name = "BackgroundColour")
    String background;

    public ProductTile() {
        super();

        this.visibleOnHomePage = true;
        this.visibleInCategory = true;
    }

    public long id() {
        return getId();
    }

    public String getForeground() {
        return foreground;
    }

    public String getBackground() {
        return background;
    }

    public void setForeground(String foreground) {
        this.foreground = foreground;
    }

    @Override
    public Long getId() {
        return Id;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    @Override
    public boolean getVisibleOnHomePage() {
        return visibleOnHomePage;
    }

    @Override
    public void setVisibleOnHomePage(boolean visible) {
        this.visibleOnHomePage = visible;
    }

    @Override
    public boolean getVisibleInCategory() {
        return visibleInCategory;
    }

    @Override
    public void setVisibleInCategory(boolean visible) {
        this.visibleInCategory = visible;
    }
}
