package com.clowdtech.data.dbflow.entities;

import com.clowdtech.data.dbflow.ApplicationDatabase;
import com.clowdtech.data.entities.CategoryTile;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.NotNull;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = ApplicationDatabase.class, name = "CategoryTiles")
public class CategoryTileDbf extends BaseModel implements CategoryTile {

    @PrimaryKey(autoincrement = true)
    long Id;

    @Column(name = "ForegroundColour")
    String foreground;

    @Column(name = "BackgroundColour")
    String background;

    @Column(name = "VisibleOnHomePage")
    @NotNull
    boolean visibleOnHomePage;

    @Override
    public Long id() {
        return Id;
    }

    @Override
    public String getForeground() {
        return foreground;
    }

    @Override
    public String getBackground() {
        return background;
    }

    public void setForeground(String foreground) {
        this.foreground = foreground;
    }

    @Override
    public boolean getVisibleOnHomePage() {
        return visibleOnHomePage;
    }

    @Override
    public void setVisibleOnHomePage(boolean visible) {
        visibleOnHomePage = visible;
    }

    public void setBackground(String background) {
        this.background = background;
    }
}
