package com.clowdtech.data.entities;

public interface CategoryTile {
    Long id();

    String getForeground();

    String getBackground();

    void setBackground(String background);

    void setForeground(String foreground);

    boolean getVisibleOnHomePage();

    void setVisibleOnHomePage(boolean visible);
}
