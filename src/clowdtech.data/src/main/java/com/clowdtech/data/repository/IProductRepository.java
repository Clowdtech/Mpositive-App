package com.clowdtech.data.repository;

import com.clowdtech.data.entities.IProduct;
import com.clowdtech.data.entities.IProductCategory;
import com.clowdtech.data.entities.IProductTile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public interface IProductRepository {
    List<IProduct> getProductsFor(long category);

    List<IProductCategory> getProductCategories();

    List<IProduct> getNewProducts();

    List<IProduct> getProductsToUpdate(long minDateStamp, long maxDateStamp);




    List<IProduct> getProducts();

    // this is here as there's not a clear separation between the repo and the app so we use repo types
    IProduct getProduct();

    IProduct getProduct(long id);

    void addToCategories(long product, ArrayList<Long> categories);

    void removeFromCategories(long productId, ArrayList<Long> categories);

    long saveTile(long id, String foreground, String background, boolean tileVisibleOnHomePage, boolean tileVisibleInCategory);

    long saveProduct(long id, String name, String description, BigDecimal price, double vat);

    void saveTileForProduct(long productId, long tileId);

    void delete(Long product);

    IProductTile getProductTile();
}
