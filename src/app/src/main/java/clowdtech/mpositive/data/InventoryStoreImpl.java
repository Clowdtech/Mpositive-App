package clowdtech.mpositive.data;

import android.content.Context;
import android.content.res.Resources;

import com.clowdtech.data.entities.IProduct;
import com.clowdtech.data.entities.IProductTile;
import com.clowdtech.data.entities.Category;
import com.clowdtech.data.repository.ICategoryRepository;
import com.clowdtech.data.entities.CategoryTile;
import com.clowdtech.data.repository.IProductRepository;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Singleton;

import clowdtech.mpositive.R;
import clowdtech.mpositive.ioc.qualifiers.QualifierApplication;
import clowdtech.mpositive.queue.events.CategoriesLoadedEvent;
import clowdtech.mpositive.queue.events.CategoriesLoadingEvent;
import clowdtech.mpositive.queue.events.CategorySavedEvent;
import clowdtech.mpositive.queue.IEventBus;
import clowdtech.mpositive.queue.events.ProductSavedEvent;
import clowdtech.mpositive.queue.events.ProductsLoadedEvent;
import clowdtech.mpositive.queue.events.ProductsLoadingEvent;

// translate between data storage and business model
// this is doing a lot, currently dumping all point into this

@Singleton
public class InventoryStoreImpl implements InventoryStore, IProductsLoadHandler, ICategoriesLoadHandler {
    private final IProductRepository productsRepo;
    private final ICategoryRepository categoriesRepo;
    private IEventBus eventBus;

    private List<Category> categories;
    private List<IProduct> products;

    private final int defaultCategoryBackground;
    private final int defaultCategoryForeground;

    private final int defaultProductBackground;
    private final int defaultProductForeground;

    public InventoryStoreImpl(IProductRepository productsRepo, ICategoryRepository categoriesRepo, IEventBus eventBus, @QualifierApplication Context context) {
        this.productsRepo = productsRepo;
        this.categoriesRepo = categoriesRepo;
        this.eventBus = eventBus;

        this.eventBus.register(this);

        Resources resources = context.getResources();

        this.defaultCategoryBackground = resources.getColor(R.color.tile_category_background_default);
        this.defaultCategoryForeground = resources.getColor(R.color.tile_category_foreground_default);

        this.defaultProductBackground = resources.getColor(R.color.tile_product_background_default);
        this.defaultProductForeground = resources.getColor(R.color.tile_product_foreground_default);
    }

    @Override
    public void initialise() {
        categories = null;
        products = null;

        CategoriesLoadAsyncTask cats = new CategoriesLoadAsyncTask(categoriesRepo, this);

        cats.execute();

        ProductsLoadAsyncTask prods = new ProductsLoadAsyncTask(productsRepo, this);

        prods.execute();
    }

    @Override
    public void refresh() {

    }

    @Override
    public IProduct getProduct() {
        IProduct product = productsRepo.getProduct();

        initialiseProductTile(product);

        return product;
    }

    @Override
    public void categoriesLoaded(List<Category> inventoryItems) {
        for (Category category :
                inventoryItems) {
            initialiseCategory(category);
        }

        this.categories = inventoryItems;

        eventBus.post(new CategoriesLoadedEvent(categories));
    }

    @Override
    public void productsLoaded(List<IProduct> inventoryItems) {
        for (IProduct product :
                inventoryItems) {
            initialiseProductTile(product);
        }

        this.products = inventoryItems;

        eventBus.post(new ProductsLoadedEvent(inventoryItems));
    }

    @Produce
    public CategoriesLoadedEvent produceCategories() {
        return new CategoriesLoadedEvent(categories);
    }

    @Produce
    public ProductsLoadedEvent produceProducts() {
        return new ProductsLoadedEvent(products);
    }

    @Subscribe
    public void subscribeCategorySaved(CategorySavedEvent event) {
        eventBus.post(new CategoriesLoadingEvent());

        Category cat = getCategory(event.getCategoryId());

        int indexOf = categories.indexOf(cat);

        if (indexOf == -1) {
            categories.add(cat);
        } else {
            categories.remove(indexOf);
            categories.add(indexOf, cat);
        }

        eventBus.post(new CategoriesLoadedEvent(categories));
    }

    @Subscribe
    public void subscribeProductSaved(ProductSavedEvent event) {
        eventBus.post(new ProductsLoadingEvent());

        IProduct product = getProduct(event.getProductId());

        int indexOf = products.indexOf(product);

        if (indexOf == -1) {
            products.add(product);
        } else {
            products.remove(indexOf);
            products.add(indexOf, product);
        }

        eventBus.post(new ProductsLoadedEvent(products));
    }








    // TODO: the following should really be wrapped in a domain object
    @Override
    public List<IProduct> getProductsFor(Long id) {
        List<IProduct> products = productsRepo.getProductsFor(id);

        for (IProduct product :
                products) {
            initialiseProductTile(product);
        }

        return products;
    }

    @Override
    public IProduct getProduct(long value) {
        IProduct product = productsRepo.getProduct(value);

        initialiseProductTile(product);

        return product;
    }

    private void initialiseProductTile(IProduct product) {
        if (product.getTile() == null) {
            product.setTile(getNewProductTile());
        } else {
            defaultProductTileProperties(product.getTile());
        }
    }

    private IProductTile getNewProductTile() {
        IProductTile tile = productsRepo.getProductTile();

        tile.setVisibleInCategory(true);
        tile.setVisibleOnHomePage(true);

        defaultProductTileProperties(tile);

        return tile;
    }

    private void defaultProductTileProperties(IProductTile tile) {
        if (tile.getBackground() == null)
            tile.setBackground(String.format("#%06X", (0xFFFFFF & this.defaultProductBackground)));

        if (tile.getForeground() == null)
            tile.setForeground(String.format("#%06X", (0xFFFFFF & this.defaultProductForeground)));
    }



    @Override
    public Category getCategory() {
        Category category = categoriesRepo.getCategory();

        initialiseCategory(category);

        return category;
    }

    @Override
    public Category getCategory(long id) {
        Category category = categoriesRepo.getCategory(id);

        initialiseCategory(category);

        return category;
    }

    @Override
    public List<Category> getCategoriesFor(long id) {
        List<Category> categories = categoriesRepo.getCategoriesFor(id);

        for (Category category :
                categories) {
            initialiseCategory(category);
        }

        return categories;
    }

    @Override
    public List<Category> getCategories() {
        List<Category> categories = categoriesRepo.getCategories();

        for (Category category :
                categories) {
            initialiseCategory(category);
        }

        return categories;
    }

    private void initialiseCategory(Category category) {
        if (category.getTile() == null) {
            category.setTile(getNewCategoryTile());
        } else {
            defaultCategoryTileProperties(category.getTile());
        }
    }

    private CategoryTile getNewCategoryTile() {
        CategoryTile tile = categoriesRepo.getCategoryTile();

        defaultCategoryTileProperties(tile);

        return tile;
    }

    private void defaultCategoryTileProperties(CategoryTile tile) {
        if (tile.getBackground() == null)
            tile.setBackground(String.format("#%06X", (0xFFFFFF & this.defaultCategoryBackground)));

        if (tile.getForeground() == null)
            tile.setForeground(String.format("#%06X", (0xFFFFFF & this.defaultCategoryForeground)));

        tile.setVisibleOnHomePage(true);
    }
}

