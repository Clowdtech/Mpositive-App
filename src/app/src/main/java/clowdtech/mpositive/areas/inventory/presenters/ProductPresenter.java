package clowdtech.mpositive.areas.inventory.presenters;

import android.graphics.Color;
import android.view.View;

import com.clowdtech.data.entities.Category;
import com.clowdtech.data.entities.IProduct;
import com.clowdtech.data.entities.IProductTile;
import com.clowdtech.data.repository.IProductRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import clowdtech.mpositive.areas.inventory.views.ProductView;
import clowdtech.mpositive.data.InventoryStore;
import clowdtech.mpositive.data.commands.ProductUpsertCommand;
import clowdtech.mpositive.queue.IEventBus;
import clowdtech.mpositive.ui.BasePresenter;

public class ProductPresenter extends BasePresenter<ProductView> {
    // move these 2 into view?
    private final IEventBus eventBus;

    private InventoryStore inventoryStore;
    private final IProductRepository productRepository;

    private List<Category> categories;

    private ArrayList<Long> newProductCategories;
    private ArrayList<Long> oldProductCategories;

    private Long productId;
    private Long tileId;

    @Inject
    public ProductPresenter(IEventBus eventBus, InventoryStore inventoryStore, IProductRepository productRepository) {
        this.eventBus = eventBus;
        this.inventoryStore = inventoryStore;
        this.productRepository = productRepository;
    }

    @Override
    public void bindView(ProductView view) {
        super.bindView(view);

        this.view.bindViews();

        eventBus.register(this);

        this.newProductCategories = new ArrayList<>();
        this.oldProductCategories = new ArrayList<>();

        this.categories = new ArrayList<>();
    }

    @Override
    public void unbindView() {
        eventBus.unregister(this);

        this.view.unbindViews();

        super.unbindView();
    }

    public void setItem(IProduct product) {
        this.productId = product.getId();

        if (product.getId() != null) {
            this.view.showActionItems();
        }

        this.view.setName(product.getName());
        this.view.setPrice(product.getPrice());
        this.view.setVat(product.getVat());
        this.view.setDescription(product.getDescription());

        //TODO: get rid of this database call
        categories = getCategoriesFor(product);

        this.view.setCategories(this.categories);

        setTileInfo(product.getTile());
    }

    private List<Category> getCategoriesFor(IProduct product) {
        Long id = product.getId();

        if (id == null) {
            return new ArrayList<>();
        }

        return inventoryStore.getCategoriesFor(id);
    }

    private void setTileInfo(IProductTile tile) {
        this.tileId = tile.getId();

        int background = Color.parseColor(tile.getBackground());

        this.view.setTileBackground(background);
        this.view.setBackgroundButtonColour(background);

        int foreground = Color.parseColor(tile.getForeground());

        this.view.setTileTextColour(foreground);
        this.view.setForegroundButtonColour(foreground);

        this.view.setVisibleOnHomePage(tile.getVisibleOnHomePage());
        this.view.setVisibleInCategory(tile.getVisibleInCategory());
    }

    public void categoryAdded(long value) {
        Category category = this.inventoryStore.getCategory(value);

        if (this.categories.contains(category)) {
            return;
        }

        if (this.oldProductCategories.contains(value)) {
            this.oldProductCategories.remove(value);
        } else {
            this.newProductCategories.add(value);
        }

        this.categories.add(category);

        Collections.sort(this.categories, new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                return ((Category) o1).getName().compareTo(((Category) o2).getName());
            }
        });

        this.view.refreshCategories(this.categories);
    }

    public void categoryRemoved(long value) {
        Category category = this.inventoryStore.getCategory(value);

        if (this.newProductCategories.contains(value)) {
            this.newProductCategories.remove(value);
        } else {
            this.oldProductCategories.add(value);
        }

        this.categories.remove(category);

        this.view.refreshCategories(this.categories);
    }

    public void tileForegroundColourSelected(int color) {
        this.view.setTileTextColour(color);
        this.view.setForegroundButtonColour(color);
    }

    public void tileBackgroundColourSelected(int color) {
        this.view.setTileBackground(color);
        this.view.setBackgroundButtonColour(color);
    }

    public void nameChanged(String name) {
        this.view.setTileTitle(name);
    }

    public void save() {
        if (viewInvalid()) {
            return;
        }

        ProductUpsertCommand command = new ProductUpsertCommand(productRepository, eventBus);

        command.setProductId(this.productId);
        command.setName(this.view.getName().trim());
        command.setPrice(this.view.getPrice());
        command.setVat(this.view.getVat());
        command.setDescription(this.view.getDescription().trim());

        command.setOldCategories(this.oldProductCategories);
        command.setNewCategories(this.newProductCategories);

        command.setTileId(this.tileId);
        command.setTileBackground(String.format("#%06X", (0xFFFFFF & this.view.getTileBackgroundColor())));
        command.setTileForeground(String.format("#%06X", (0xFFFFFF & this.view.getTileForegroundColor())));
        command.setTileVisibleOnHomePage(this.view.getVisibleOnHomePage());
        command.setTileVisibleInCategory(this.view.getVisibleInCategory());

        command.execute();

        this.view.getContainer().productSaved();
    }

    private boolean viewInvalid() {
        return this.view.getName().trim().isEmpty();
    }

    public void delete() {
        productRepository.delete(this.productId);

        this.view.getContainer().productDeleted();
    }

    public void priceDisplayChanged(String formattedValue) {
        this.view.setTilePrice(formattedValue);
    }

    public void deleteActionItemClick() {
        this.view.confirmDelete();
    }

    public void setMenu(View viewMenu) {
        this.view.getContainer().setViewMenu(viewMenu);
    }

    public void addCategory() {
        List<Category> availableCategories = inventoryStore.getCategories();

        this.view.setAvailableProducts(availableCategories);

        this.view.showCategoriesPicker();
    }
}
