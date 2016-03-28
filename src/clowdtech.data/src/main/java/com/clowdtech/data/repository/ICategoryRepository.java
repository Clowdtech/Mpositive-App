package com.clowdtech.data.repository;

import com.clowdtech.data.entities.Category;
import com.clowdtech.data.entities.CategoryTile;

import java.util.ArrayList;
import java.util.List;

public interface ICategoryRepository {
    // read
    List<Category> getCategories();

    Category getCategory();

    Category getCategory(long id);

    List<Category> getCategoriesFor(long id);

    CategoryTile getCategoryTile();


    // write
    long saveTile(long id, String foreground, String background, boolean visibility);

    void saveTileForCategory(long tile, long id);

    void removeProducts(long categoryId, ArrayList<Long> products);

    void addProducts(long categoryId, ArrayList<Long> products);

    long saveCategory(long categoryId, String name);
}
