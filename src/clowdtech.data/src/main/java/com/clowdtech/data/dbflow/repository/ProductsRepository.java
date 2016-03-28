package com.clowdtech.data.dbflow.repository;

import com.clowdtech.data.dbflow.entities.CategoryDbf;
import com.clowdtech.data.dbflow.entities.CategoryDbf_Table;
import com.clowdtech.data.dbflow.entities.Product;
import com.clowdtech.data.dbflow.entities.ProductCategory;
import com.clowdtech.data.dbflow.entities.ProductCategory_Table;
import com.clowdtech.data.dbflow.entities.ProductTile;
import com.clowdtech.data.dbflow.entities.ProductTile_Table;
import com.clowdtech.data.dbflow.entities.Product_Table;
import com.clowdtech.data.entities.IProduct;
import com.clowdtech.data.entities.IProductCategory;
import com.clowdtech.data.entities.IProductTile;
import com.clowdtech.data.repository.IProductRepository;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductsRepository implements IProductRepository {
    @Override
    public List<IProduct> getProducts() {
        List<Product> orders = SQLite.select()
                .from(Product.class)
                .where(Product_Table.Deleted.eq(false))
                .queryList();

        List<IProduct> returnOrders = new ArrayList<>();

        for (Product order :
                orders) {
            returnOrders.add(order);
        }

        return returnOrders;
    }

    @Override
    public Product getProduct(long id) {
        return SQLite.select()
                .from(Product.class)
                .where(Product_Table.Id.eq(id))
                    .and(Product_Table.Deleted.eq(false))
                .querySingle();
    }

    @Override
    public List<IProduct> getProductsFor(long category) {
        ArrayList<IProduct> cats = new ArrayList<>();

        List<ProductCategory> prodCats = SQLite.select()
                .from(ProductCategory.class)
                .where(ProductCategory_Table.category.eq(category))
                .queryList();

        for (ProductCategory line : prodCats) {
            if (line.getProduct() != null && !line.getProduct().getDeleted())
                cats.add(line.getProduct());
        }

        return cats;
    }

    @Override
    public List<IProductCategory> getProductCategories() {
        List<ProductCategory> queryList = SQLite.select()
                .from(ProductCategory.class)
                .queryList();

        ArrayList<IProductCategory> returnList = new ArrayList<>();

        for (ProductCategory line : queryList) {
            returnList.add(line);
        }

        return returnList;
    }

    @Override
    public void delete(Long productId) {
        Product product = SQLite.select()
                .from(Product.class)
                .where(Product_Table.Id.eq(productId))
                .querySingle();

        product.setDeleted(true);

        product.save();
    }

    @Override
    public IProductTile getProductTile() {
        return new ProductTile();
    }

    @Override
    public IProduct getProduct() {
        return new Product();
    }

    @Override
    public List<IProduct> getNewProducts() {
        List<Product> newProducts = SQLite.select()
                .from(Product.class)
                .where(Product_Table.RemoteId.eq(0))
                .queryList();

        List<IProduct> returnProducts = new ArrayList<>();

        for (Product product : newProducts) {
            returnProducts.add(product);
        }

        return returnProducts;
    }

    @Override
    public List<IProduct> getProductsToUpdate(long minDateStamp, long maxDateStamp) {
        List<Product> newProducts = SQLite.select()
                .from(Product.class)
                .where(Product_Table.LastUpdatedDate.greaterThan(new DateTime().withMillis(minDateStamp)))
                    .and(Product_Table.LastUpdatedDate.lessThanOrEq(new DateTime().withMillis(maxDateStamp)))
                    .and(Product_Table.RemoteId.notEq(0))
                .queryList();

        List<IProduct> returnProducts = new ArrayList<>();

        for (Product product : newProducts) {
            returnProducts.add(product);
        }

        return returnProducts;
    }

    @Override
    public void removeFromCategories(long product, ArrayList<Long> categories) {
        for (long cat : categories) {
            SQLite.delete()
                    .from(ProductCategory.class)
                    .where(ProductCategory_Table.product.eq(product))
                        .and(ProductCategory_Table.category.eq(cat))
                    .query();
        }
    }

    @Override
    public void addToCategories(long productId, ArrayList<Long> categories) {

            Product product = SQLite.select()
                    .from(Product.class)
                    .where(Product_Table.Id.eq(productId))
                    .querySingle();

            for (long categoryId : categories) {
                CategoryDbf category = SQLite.select()
                        .from(CategoryDbf.class)
                        .where(CategoryDbf_Table.Id.eq(categoryId))
                        .querySingle();

                ProductCategory link = new ProductCategory(product, category);

                link.save();
            }
    }

    @Override
    public long saveTile(long id, String foreground, String background, boolean tileVisibleOnHomePage, boolean tileVisibleInCategory) {
        ProductTile tile;

        if (id == 0) {
            tile = new ProductTile();
        } else {
            tile = SQLite.select()
                    .from(ProductTile.class)
                    .where(ProductTile_Table.Id.eq(id))
                    .querySingle();
        }

        tile.setForeground(foreground);
        tile.setBackground(background);
        tile.setVisibleOnHomePage(tileVisibleOnHomePage);
        tile.setVisibleInCategory(tileVisibleInCategory);

        tile.save();

        return tile.getId();
    }

    @Override
    public long saveProduct(long id, String name, String description, BigDecimal price, double vat) {
        Product product;

        if (id == 0) {
            product = new Product();
        } else {
            //TODO: change this and the tile above to an update
            product = SQLite.select()
                    .from(Product.class)
                    .where(Product_Table.Id.eq(id))
                    .querySingle();
        }

        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setVat(vat);

        product.save();

        return product.getId();
    }

    @Override
    public void saveTileForProduct(long productId, long tileId) {
        Product product = SQLite.select()
                .from(Product.class)
                .where(Product_Table.Id.eq(productId))
                .querySingle();

        ProductTile tile = SQLite.select()
                .from(ProductTile.class)
                .where(ProductTile_Table.Id.eq(tileId))
                .querySingle();

        product.setTile(tile);

        product.save();
    }

}
