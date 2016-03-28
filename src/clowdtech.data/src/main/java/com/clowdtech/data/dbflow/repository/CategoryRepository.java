package com.clowdtech.data.dbflow.repository;

import com.clowdtech.data.dbflow.entities.CategoryDbf;
import com.clowdtech.data.dbflow.entities.CategoryDbf_Table;
import com.clowdtech.data.dbflow.entities.CategoryTileDbf;
import com.clowdtech.data.dbflow.entities.CategoryTileDbf_Table;
import com.clowdtech.data.dbflow.entities.Product;
import com.clowdtech.data.dbflow.entities.ProductCategory;
import com.clowdtech.data.dbflow.entities.ProductCategory_Table;
import com.clowdtech.data.dbflow.entities.Product_Table;
import com.clowdtech.data.entities.Category;
import com.clowdtech.data.entities.CategoryTile;
import com.clowdtech.data.repository.ICategoryRepository;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepository implements ICategoryRepository {
    @Override
    public List<Category> getCategories() {
        List<CategoryDbf> categories = SQLite.select()
                .from(CategoryDbf.class)
                .queryList();

        ArrayList<Category> cats = new ArrayList<>();

        for (Category cat : categories) {
            cats.add(cat);
        }

        return cats;
    }

    @Override
    public Category getCategory() {
        return new CategoryDbf();
    }

    @Override
    public Category getCategory(long id) {
        return (Category) SQLite.select()
                .from(CategoryDbf.class)
                .where(CategoryDbf_Table.Id.eq(id))
                .querySingle();
    }

    @Override
    public List<Category> getCategoriesFor(long id) {
        ArrayList<Category> cats = new ArrayList<>();

        List<ProductCategory> prodCats = SQLite.select()
                .from(ProductCategory.class)
                .where(ProductCategory_Table.product.eq(id))
                .queryList();

        for (ProductCategory line : prodCats) {
            if (line.getCategory() != null)
                cats.add(line.getCategory());
        }

        return cats;
    }

    @Override
    public long saveTile(long id, String foreground, String background, boolean visibility) {
        CategoryTileDbf tile;

        if (id == 0) {
            tile = new CategoryTileDbf();
        } else {
            tile = SQLite.select()
                    .from(CategoryTileDbf.class)
                    .where(CategoryTileDbf_Table.Id.eq(id))
                    .querySingle();
        }

        tile.setBackground(background);
        tile.setForeground(foreground);
        tile.setVisibleOnHomePage(visibility);

        tile.save();

        return tile.id();
    }

    @Override
    public void saveTileForCategory(long tileId, long categoryId) {
        CategoryDbf category = SQLite.select()
                .from(CategoryDbf.class)
                .where(CategoryDbf_Table.Id.eq(categoryId))
                .querySingle();

        CategoryTileDbf tile = SQLite.select()
                .from(CategoryTileDbf.class)
                .where(CategoryTileDbf_Table.Id.eq(tileId))
                .querySingle();

        category.setTile(tile);

        category.save();
    }

    @Override
    public void removeProducts(long categoryId, ArrayList<Long> products) {
            for (long product : products) {
                SQLite.delete()
                        .from(ProductCategory.class)
                        .where(ProductCategory_Table.category.eq(categoryId))
                            .and(ProductCategory_Table.product.eq(product))
                        .query();
            }
    }

    @Override
    public void addProducts(long categoryId, ArrayList<Long> products) {
            CategoryDbf category = SQLite.select()
                    .from(CategoryDbf.class)
                    .where(CategoryDbf_Table.Id.eq(categoryId))
                    .querySingle();

            for (long productId : products) {
                Product product = SQLite.select()
                        .from(Product.class)
                        .where(Product_Table.Id.eq(productId))
                        .querySingle();

                ProductCategory link = new ProductCategory(product, category);

                link.save();
            }
    }

    @Override
    public long saveCategory(long categoryId, String name) {
        CategoryDbf category;

        if (categoryId == 0) {
            category = new CategoryDbf();
        } else {
            category = SQLite.select()
                    .from(CategoryDbf.class)
                    .where(CategoryDbf_Table.Id.eq(categoryId))
                    .querySingle();
        }

        category.setName(name);

        category.save();

//        ActiveAndroid.clearCache(); // this was here previously when there was a reliance on previously loaded collections (i think) but now moving away from that

        return category.getId();
    }

    @Override
    public CategoryTile getCategoryTile() {
        return new CategoryTileDbf();
    }
}
