package com.clowdtech.data.entities;

public interface IProductTile {
    boolean getVisibleOnHomePage();

    void setVisibleOnHomePage(boolean visible);

    boolean getVisibleInCategory();

    void setVisibleInCategory(boolean visible);

    String getForeground();

    String getBackground();

    void setBackground(String format);

    void setForeground(String format);

    Long getId();
}
