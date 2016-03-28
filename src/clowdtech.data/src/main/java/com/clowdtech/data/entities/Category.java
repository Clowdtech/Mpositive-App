package com.clowdtech.data.entities;

public interface Category {
    String getName();

    CategoryTile getTile();

    void setTile(CategoryTile tile);

    Long getId();

    void setName(String name);
}
