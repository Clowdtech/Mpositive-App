package clowdtech.mpositive.areas.till.presenters;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;

import com.clowdtech.data.entities.IProduct;
import com.clowdtech.data.entities.Category;
import com.clowdtech.data.entities.IProductTile;
import com.clowdtech.data.repository.IProductRepository;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import clowdtech.mpositive.ITracker;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.inventory.viewModels.ProductViewModel;
import clowdtech.mpositive.areas.shared.InventoryItem;
import clowdtech.mpositive.areas.till.viewModels.CategoryTileViewModel;
import clowdtech.mpositive.areas.till.views.InventoryView;
import clowdtech.mpositive.queue.events.RunningReceiptAddProductEvent;
import clowdtech.mpositive.queue.events.CategoriesLoadedEvent;
import clowdtech.mpositive.queue.IEventBus;
import clowdtech.mpositive.queue.events.ProductsLoadedEvent;
import clowdtech.mpositive.ui.BasePresenter;

public class InventoryPresenter extends BasePresenter<InventoryView> {
    private final ITracker tracker;

    private final IEventBus eventBus;
    private final IProductRepository productRepo;

    private boolean categoryChildrenInView;

    private List<InventoryItem> inventory;
    private List<InventoryItem> categoryViewModels;
    private List<InventoryItem> productViewModels;

    // move higher
    private final int defaultProductBackground;
    private final int defaultProductForeground;
    private final int defaultCategoryForeground;
    private final int defaultCategoryBackground;

    @Inject
    public InventoryPresenter(ITracker tracker, Resources resources, IEventBus eventBus, IProductRepository productRepo) {
        this.tracker = tracker;

        this.eventBus = eventBus;
        this.productRepo = productRepo;

        defaultProductBackground = resources.getColor(R.color.tile_product_background_default);
        defaultProductForeground = resources.getColor(R.color.tile_product_foreground_default);
        defaultCategoryForeground = resources.getColor(R.color.tile_category_foreground_default);
        defaultCategoryBackground = resources.getColor(R.color.tile_category_background_default);
    }

    @Override
    public void bindView(InventoryView view) {
        super.bindView(view);

        eventBus.register(this);

        loadInventoryInView();
    }

    @Override
    public void unbindView() {
        eventBus.unregister(this);

        super.unbindView();
    }

    private void categorySelected(final CategoryTileViewModel category) {
        view.setLoading();

        Runnable runner = new Runnable() {
            @Override
            public void run() {
                inventory = new ArrayList<>();

                List<IProduct> prods = productRepo.getProductsFor(category.getItemId());

                List<InventoryItem> children = new ArrayList<>();

                for (int i = 0; i < prods.size(); i++) {
                    IProduct product = prods.get(i);

                    if (product.getTile().getVisibleInCategory()) {
                        ProductViewModel vm = getProductViewModel(product);

                        children.add(vm);
                    }
                }

                sortItems(children);

                inventory.addAll(children);

                inventory.add(0, getBackTile());

                view.setInventoryItems(inventory);

                categoryChildrenInView = true;

                view.setLoaded();
            }
        };

        runner.run();
    }

    @NonNull
    private ProductViewModel getBackTile() {
        IProductTile tile = productRepo.getProductTile();

        tile.setVisibleOnHomePage(true);

        return new ProductViewModel("...", defaultProductForeground, defaultProductBackground);
    }

    public boolean onBackPressed() {
        if (this.categoryChildrenInView) {
            loadInventoryInView();

            return true;
        }

        return false;
    }

    private void productSelected(ProductViewModel product) {
        eventBus.post(new RunningReceiptAddProductEvent(product.getItemId(), product.getTitle(), product.getPrice()));
    }

    @Subscribe
    public void subscribeCategories(CategoriesLoadedEvent loadedEvent) {
        List<Category> categories = loadedEvent.getData();

        if (categories == null) {
            return;
        }

        categoryViewModels = new ArrayList<>();

        for (Category category : categories) {
            if (category.getTile() != null && !category.getTile().getVisibleOnHomePage()) {
                continue;
            }

            int foreground;
            int background;

            if (category.getTile() == null) {
                foreground = defaultCategoryForeground;
                background = defaultCategoryBackground;
            } else {
                foreground = Color.parseColor(category.getTile().getForeground());
                background = Color.parseColor(category.getTile().getBackground());
            }

            CategoryTileViewModel cvm = new CategoryTileViewModel(category.getId(), category.getName(), foreground, background);

            categoryViewModels.add(cvm);
        }

        sortItems(categoryViewModels);

        if (productViewModels != null) {
            loadInventoryInView();
        }
    }

    @Subscribe
    public void subscribeProducts(ProductsLoadedEvent loadedEvent) {
        List<IProduct> products = loadedEvent.getData();

        if (products == null) {
            return;
        }

        productViewModels = new ArrayList<>();

        for (IProduct product : products) {
            if (product.getTile() != null && !product.getTile().getVisibleOnHomePage()) {
                continue;
            }

            ProductViewModel vm = getProductViewModel(product);

            productViewModels.add(vm);
        }

        sortItems(productViewModels);

        if (categoryViewModels != null) {
            loadInventoryInView();
        }
    }

    @NonNull
    private ProductViewModel getProductViewModel(IProduct product) {
        int foreground;
        int background;

        if (product.getTile() == null) {
            foreground = defaultProductForeground;
            background = defaultProductBackground;
        } else {
            foreground = product.getTile().getForeground() == null ? defaultProductForeground : Color.parseColor(product.getTile().getForeground());
            background = product.getTile().getBackground() == null ? defaultProductBackground : Color.parseColor(product.getTile().getBackground());
        }

        return new ProductViewModel(product.getName(), product.getId(), product.getPrice(), foreground, background);
    }

    private void loadInventoryInView() {
        if (this.view != null && productViewModels != null && categoryViewModels != null) {
            inventory = new ArrayList<>(categoryViewModels);
            inventory.addAll(productViewModels);

            view.setInventoryItems(inventory);

            categoryChildrenInView = false;

            this.view.setLoaded();
        }
    }

    private void sortItems(List<InventoryItem> productViewModels) {
        Collections.sort(productViewModels, new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                boolean itemOneCategory = o1 instanceof CategoryTileViewModel;
                boolean itemTwoCategory = o2 instanceof CategoryTileViewModel;

                if (itemOneCategory && !itemTwoCategory) {
                    return -1;
                }

                if (itemTwoCategory && !itemOneCategory) {
                    return 1;
                }

                return ((InventoryItem) o1).getTitle().compareTo(((InventoryItem) o2).getTitle());
            }
        });
    }

    public void itemSelected(int position) {
        if (categoryChildrenInView && position == 0) {
            loadInventoryInView();
        } else if (!categoryChildrenInView && position < categoryViewModels.size()) {
            categorySelected((CategoryTileViewModel) inventory.get(position));
        } else {
            productSelected((ProductViewModel) inventory.get(position));
        }
    }
}