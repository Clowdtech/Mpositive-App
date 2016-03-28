package clowdtech.mpositive.areas.inventory.presenters;

import android.graphics.Color;

import com.clowdtech.data.entities.IProduct;
import com.clowdtech.data.entities.Category;
import com.clowdtech.data.repository.ICategoryRepository;
import com.clowdtech.data.entities.CategoryTile;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import clowdtech.mpositive.ITracker;
import clowdtech.mpositive.areas.inventory.views.CategoryView;
import clowdtech.mpositive.data.InventoryStore;
import clowdtech.mpositive.data.commands.CategoryUpsertCommand;
import clowdtech.mpositive.queue.IEventBus;
import clowdtech.mpositive.queue.events.ProductsLoadedEvent;
import clowdtech.mpositive.tracking.TrackingCategories;
import clowdtech.mpositive.ui.BasePresenter;

public class CategoryPresenter extends BasePresenter<CategoryView> {
    private final ITracker tracker;
    private final ICategoryRepository categoryRepository;

    private final IEventBus eventBus;

    private InventoryStore inventoryStore;

    private List<IProduct> products;

    private ArrayList<Long> newCategoryProducts;
    private ArrayList<Long> oldCategoryProducts;

    private List<IProduct> productsCache;

    private Long categoryId;
    private Long tileId;

    @Inject
    public CategoryPresenter(ITracker tracker,
                             InventoryStore inventoryStore,
                             ICategoryRepository categoryRepository,
                             IEventBus eventBus) {
        this.inventoryStore = inventoryStore;
        this.tracker = tracker;
        this.categoryRepository = categoryRepository;
        this.eventBus = eventBus;
    }

    @Override
    public void bindView(CategoryView view) {
        super.bindView(view);

        this.view.bindViews();

        eventBus.register(this);

        this.newCategoryProducts = new ArrayList<>();
        this.oldCategoryProducts = new ArrayList<>();

        this.products = new ArrayList<>();
    }

    @Override
    public void unbindView() {
        eventBus.unregister(this);

        this.view.unbindViews();

        super.unbindView();
    }

    public void setItem(Category category) {
        this.categoryId = category.getId(); // this was to differentiate setting up a category or not when its new, but always doing setup now so explicitly set to zero

        this.products = getProductsFor(category);

        this.view.setName(category.getName());
        this.view.setProducts(this.products);

        CategoryTile tile = category.getTile();

        setTileInfo(tile);
    }

    private List<IProduct> getProductsFor(Category category) {
        Long id = category.getId();

        if (id == null) {
            return new ArrayList<>();
        }

        return inventoryStore.getProductsFor(id);
    }

    private void setTileInfo(CategoryTile tile) {
        this.tileId = tile.id();

        int background = Color.parseColor(tile.getBackground());

        this.view.setTileBackground(background);
        this.view.setButtonBackgroundColour(background);

        int foreground = Color.parseColor(tile.getForeground());

        this.view.setTileTextColour(foreground);
        this.view.setButtonForegroundColour(foreground);

        this.view.setVisibleOnHomePage(tile.getVisibleOnHomePage());
    }

    public void productAdded(long value) {
        IProduct product = inventoryStore.getProduct(value);

        if (this.products.contains(product)) {
            return;
        }

        if (this.oldCategoryProducts.contains(product.getId())) {
            this.oldCategoryProducts.remove(product.getId());
        } else {
            this.newCategoryProducts.add(product.getId());
        }

        this.products.add(product);

        Collections.sort(this.products, new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                return ((IProduct) o1).getName().compareTo(((IProduct) o2).getName());
            }
        });

        this.view.refreshCategories(this.products);

        this.tracker.trackEvent(TrackingCategories.InventoryManagement, "Product Added to Category");
    }

    public void productRemoved(long value) {
        IProduct product = inventoryStore.getProduct(value);

        if (this.newCategoryProducts.contains(value)) {
            this.newCategoryProducts.remove(value);
        } else {
            this.oldCategoryProducts.add(value);
        }

        this.products.remove(product);

        this.view.refreshCategories(this.products);

        this.tracker.trackEvent(TrackingCategories.InventoryManagement, "Product Removed from Category");
    }

    public void tileForegroundColourSelected(int color) {
        this.view.setTileTextColour(color);
        this.view.setButtonForegroundColour(color);
    }

    public void tileBackgroundColourSelected(int color) {
        this.view.setTileBackground(color);
        this.view.setButtonBackgroundColour(color);
    }

    public void categoryNameChanged(String name) {
        this.view.setTileText(name);
    }

    public void save() {
        if (viewInvalid()) {
            return;
        }

        CategoryUpsertCommand categoryCommand = new CategoryUpsertCommand(categoryRepository, eventBus);

        categoryCommand.setId(this.categoryId);
        categoryCommand.setName(this.view.getName());
        categoryCommand.setVisibility(this.view.getVisibleOnHomePage());
        categoryCommand.setNewProducts(this.newCategoryProducts);
        categoryCommand.setOldProducts(this.oldCategoryProducts);

        categoryCommand.setTileId(this.tileId);
        categoryCommand.setTileBackground(String.format("#%06X", (0xFFFFFF & this.view.getTileBackgroundColor())));
        categoryCommand.setTileForeground(String.format("#%06X", (0xFFFFFF & this.view.getTileForegroundColor())));

        categoryCommand.execute();

        this.tracker.trackEvent(TrackingCategories.InventoryManagement, "Category Saved");

        this.view.getContainer().categorySaved();
    }

    private boolean viewInvalid() {
        if (this.view.getName().trim().isEmpty()) {
            return true;
        }

        return false;
    }

    public void addProduct() {
        loadPossibleProducts();

        this.view.showProductsPicker();
    }

    @Subscribe
    public void subscribeProducts(ProductsLoadedEvent loadedEvent) {
        this.productsCache = loadedEvent.getData(); // TODO: could be holding lots of products in multiple places, look at this mechanism

        loadPossibleProducts();
    }

    private void loadPossibleProducts() {
        if (viewLoaded() && productsLoaded()) {
            this.view.setAvailableProducts(this.productsCache);

            this.productsCache = null;
        }
    }

    private boolean viewLoaded() {
        return this.view != null;
    }

    private boolean productsLoaded() {
        return this.productsCache != null;
    }
}
