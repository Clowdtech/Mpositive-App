package clowdtech.mpositive.areas.inventory.presenters;

import android.graphics.Color;

import com.clowdtech.data.entities.IProduct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import clowdtech.mpositive.areas.inventory.viewModels.ProductViewModel;
import clowdtech.mpositive.areas.inventory.views.ProductSelectView;
import clowdtech.mpositive.areas.shared.InventoryItem;
import clowdtech.mpositive.ui.BasePresenter;

public class ProductSelectPresenter extends BasePresenter<ProductSelectView> {
    private ArrayList<InventoryItem> models;

    @Inject
    public ProductSelectPresenter() {
    }

    public void bindView(ProductSelectView view) {
        super.bindView(view);

        if (models != null) {
            view.initialiseView(models);

            view.setLoaded();
        } else {
            this.view.setLoading();
        }
    }

    public void setProducts(List<IProduct> products) {
        buildViewModels(products);
    }

    public void filter(String query) {
        this.view.filterGrid(query);
    }

    public void resetData(List<IProduct> products) {
        buildViewModels(products);
    }

    private void buildViewModels(final List<IProduct> products) {
        models = new ArrayList<>();

        for (IProduct product : products) {
            int foreground = Color.parseColor(product.getTile().getForeground());
            int background = Color.parseColor(product.getTile().getBackground());

            ProductViewModel viewModel = new ProductViewModel(product.getName(), product.getId(), product.getPrice(), foreground, background);

            models.add(viewModel);
        }

        Collections.sort(models, new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                return ((ProductViewModel) o1).getTitle().compareTo(((ProductViewModel) o2).getTitle());
            }
        });

        if (view != null) {
            view.initialiseView(models);

            view.setLoaded();
        }
    }
}
