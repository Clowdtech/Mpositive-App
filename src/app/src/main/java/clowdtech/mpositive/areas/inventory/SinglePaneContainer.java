package clowdtech.mpositive.areas.inventory;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.clowdtech.data.entities.IProduct;
import com.clowdtech.data.entities.Category;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import clowdtech.mpositive.App;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.inventory.views.AreaSelectView;
import clowdtech.mpositive.areas.inventory.views.CategoriesView;
import clowdtech.mpositive.areas.inventory.views.CategoryViewImpl;
import clowdtech.mpositive.areas.inventory.views.ProductViewImpl;
import clowdtech.mpositive.areas.inventory.views.ProductsView;
import clowdtech.mpositive.areas.shared.views.ViewMenuView;
import clowdtech.mpositive.lifecycle.PresentedView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class SinglePaneContainer extends RelativeLayout implements ProductManagementView, Container, Presentable {
    private ProductViewImpl productView;

    private CategoryViewImpl categoryView;

    private ProductsView productsView;

    private CategoriesView categoriesView;

    @Inject
    ProductManagementPresenter presenter;

    @Bind(R.id.view_container)
    protected ViewGroup viewContainer;

    @Bind(R.id.area_view)
    protected AreaSelectView areaSelectView;

    private ViewMenuView viewMenu;

    public SinglePaneContainer(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) {
            return;
        }

        ((App) context.getApplicationContext()).getApplicationComponent().inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.bind(this);

        areaSelectView.bindView();
    }


    @Override
    public void initialise(ViewMenuView customActionBar) {
        this.viewMenu = customActionBar;
    }


    @Override
    public void productSelected(IProduct product) {
        showProductEditView(product);
    }

    @Override
    public void productSaved() {
        showProductsView();
    }

    @Override
    public void productDeleted() {
        showProductsView();
    }

    @Override
    public void navToHomeView() {
        showAreaSelectView();
    }

    @Override
    public void setViewMenu(View view) {
        this.viewMenu.setViewMenu(view);
    }

    @Override
    public void clearViewMenu() {
        this.viewMenu.clearViewMenu();
    }

    @Override
    public void productViewSelected() {
        showProductsView();
    }

    @Override
    public void categoryViewSelected() {
        showCategoriesView();
    }

    @Override
    public void bindView() {
        this.presenter.bindView(this);
    }

    @Override
    public void unbindViews() {
        ((Presentable) getContentView()).getPresenter().unbindView();
    }

    @Override
    public void bindViews() {
        ((PresentedView) getContentView()).bindView();
    }

    @Override
    public void categoryEdit(Category category) {
        if (!categoryViewAttached()) {
            injectView(R.layout.inventory_category);
        }

        categoryView = (CategoryViewImpl) getContentView();

        categoryView.setItem(category);
    }

    @Override
    public void categorySaved() {
        showCategoriesView();
    }

    @Override
    public boolean onBackPressed() {
        if (productViewAttached()) {
            showProductsView();

            return true;
        }

        if (categoryViewAttached()) {
            showCategoriesView();

            return true;
        }

        if (categoriesViewAttached() || productsViewAttached()) {
            showAreaSelectView();

            return true;
        }

        return false;
    }

    private void showAreaSelectView() {
        if (!areaSelectAttached()) {
            injectView(R.layout.inventory_area_select_view);
        }

        areaSelectView = (AreaSelectView) getContentView();
    }

    private void showProductsView() {
        if (!productsViewAttached()) {
            injectView(R.layout.inventory_products);
        }

        productsView = (ProductsView) getContentView();
    }

    private void showCategoriesView() {
        if (!categoriesViewAttached()) {
            injectView(R.layout.inventory_categories);
        }

        categoriesView = (CategoriesView) getContentView();
    }

    private void showProductAddView() {
        if (!productViewAttached()) {
            injectView(R.layout.inventory_product);
        }

        productView = (ProductViewImpl) getContentView();
    }

    private void showProductEditView(IProduct product) {
        showProductAddView();

        productView.setItem(product);
    }

    public void injectView(int view) {
        this.viewMenu.clearViewMenu();

        removeContentView();

        inflate(getContext(), view, viewContainer);

        ((PresentedView)getContentView()).bindView();
    }

    private boolean areaSelectAttached() {
        return areaSelectView != null && areaSelectView.getParent() != null;
    }

    private boolean productsViewAttached() {
        return productsView != null && productsView.getParent() != null;
    }

    private boolean categoriesViewAttached() {
        return categoriesView != null && categoriesView.getParent() != null;
    }

    private boolean productViewAttached() {
        return productView != null && productView.getParent() != null;
    }

    private boolean categoryViewAttached() {
        return categoryView != null && categoryView.getParent() != null;
    }

    private View getContentView() {
        return viewContainer.getChildAt(0);
    }

    private void removeContentView() {
        ((Presentable) getContentView()).getPresenter().unbindView();

        viewContainer.removeViewAt(0);
    }

    @Override
    public Presenter getPresenter() {
        return this.presenter;
    }
}
