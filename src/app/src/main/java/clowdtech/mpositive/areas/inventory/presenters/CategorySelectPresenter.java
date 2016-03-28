package clowdtech.mpositive.areas.inventory.presenters;

import com.clowdtech.data.entities.Category;
import com.clowdtech.data.entities.CategoryTile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import clowdtech.mpositive.areas.inventory.viewModels.CategoryViewModel;
import clowdtech.mpositive.areas.inventory.views.CategorySelectView;
import clowdtech.mpositive.ui.BasePresenter;

public class CategorySelectPresenter extends BasePresenter<CategorySelectView> {
    private ArrayList<CategoryViewModel> viewModels;

    @Inject
    public CategorySelectPresenter() {
        this.viewModels = new ArrayList<>();
    }

    @Override
    public void bindView(CategorySelectView view) {
        super.bindView(view);

        if (viewModels != null) {
            this.view.initialiseView(viewModels);
        }
    }

    public void setCategories(List<Category> categories) {
        this.viewModels = buildViewModels(categories);

        if (view != null) {
            this.view.initialiseView(viewModels);
        }
    }

    public void filter(String query) {
        this.view.filterGrid(query);
    }

    public void resetData(List<Category> dataSource) {
        viewModels.clear();

        viewModels.addAll(buildViewModels(dataSource));

        this.view.notifyDataChanged();
    }

    private ArrayList<CategoryViewModel> buildViewModels(List<Category> categories) {
        ArrayList<CategoryViewModel> models = new ArrayList<>();

        for (Category category : categories) {
            CategoryTile tile = category.getTile();


            // check if tile is null!!!

            CategoryViewModel viewModel = new CategoryViewModel(category.getName(), category.getId(), tile);

            models.add(viewModel);
        }

        Collections.sort(models, new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                return ((CategoryViewModel) o1).getName().compareTo(((CategoryViewModel) o2).getName());
            }
        });

        return models;
    }
}
