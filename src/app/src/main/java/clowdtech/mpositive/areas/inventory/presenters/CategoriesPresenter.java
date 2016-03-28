package clowdtech.mpositive.areas.inventory.presenters;

import com.clowdtech.data.entities.Category;
import com.squareup.otto.Subscribe;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import clowdtech.mpositive.areas.inventory.views.ICategoriesView;
import clowdtech.mpositive.data.InventoryStore;
import clowdtech.mpositive.ui.BasePresenter;
import clowdtech.mpositive.queue.events.CategoriesLoadedEvent;
import clowdtech.mpositive.queue.IEventBus;

public class CategoriesPresenter extends BasePresenter<ICategoriesView> {
    private IEventBus eventBus;
    private InventoryStore inventoryStore;

    private List<Category> categories;

    @Inject
    public CategoriesPresenter(IEventBus eventBus, InventoryStore inventoryStore) {
        this.eventBus = eventBus;
        this.inventoryStore = inventoryStore;
    }

    public void addProduct() {
        this.view.getContainer().categoryEdit(inventoryStore.getCategory());
    }

    @Override
    public void bindView(ICategoriesView view) {
        super.bindView(view);

        eventBus.register(this);

        if (categories == null) {
            view.setLoading();
        }
    }

    @Override
    public void unbindView() {
        super.unbindView();

        eventBus.unregister(this);
    }

    @Subscribe // fires twice from category screen as previous presenter (none singleton is hanging around)
    public void subscribeCategories(CategoriesLoadedEvent event) {
        categories = event.getData();

        if (view != null) {
            view.setLoading();
        }

        if (categories == null || view == null) {
            return;
        }

        sortItems(categories);

        view.setCategories(categories);

        view.setLoaded();
    }

    private void sortItems(List<Category> categories) {
        Collections.sort(categories, new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                return ((Category) o1).getName().compareTo(((Category) o2).getName());
            }
        });
    }

    public void categorySelected(int position) {
        this.view.getContainer().categoryEdit(categories.get(position));
    }
}
