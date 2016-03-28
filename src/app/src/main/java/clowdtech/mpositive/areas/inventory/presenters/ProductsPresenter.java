package clowdtech.mpositive.areas.inventory.presenters;

import com.clowdtech.data.entities.IProduct;
import com.squareup.otto.Subscribe;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import clowdtech.mpositive.areas.inventory.views.IProductsView;
import clowdtech.mpositive.data.InventoryStore;
import clowdtech.mpositive.ui.BasePresenter;
import clowdtech.mpositive.queue.IEventBus;
import clowdtech.mpositive.queue.events.ProductsLoadedEvent;

public class ProductsPresenter extends BasePresenter<IProductsView> {
    private IEventBus eventBus;
    private InventoryStore inventoryStore;

    private List<IProduct> products;

    @Inject
    public ProductsPresenter(IEventBus eventBus, InventoryStore inventoryStore) {
        this.eventBus = eventBus;
        this.inventoryStore = inventoryStore;
    }

    public void productSelected(int position) {
        this.view.getContainer().productSelected(products.get(position));
    }

    public void addProduct() {
        this.view.getContainer().productSelected(inventoryStore.getProduct());
    }

    @Override
    public void bindView(IProductsView view) {
        super.bindView(view);

        eventBus.register(this);

        if (products == null) {
            view.setLoading();
        }
    }

    @Override
    public void unbindView() {
        super.unbindView();

        eventBus.unregister(this);
    }

    @Subscribe
    public void subscribeProducts(ProductsLoadedEvent loadedEvent) {
        products = loadedEvent.getData();

        if (view != null) {
            view.setLoading();
        }

        if (products == null || view == null) {
            return;
        }

        sortItems(products);

        view.setProducts(products);

        view.setLoaded();
    }

    private void sortItems(List<IProduct> productViewModels) {
        Collections.sort(productViewModels, new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                return ((IProduct) o1).getName().compareTo(((IProduct) o2).getName());
            }
        });
    }
}
