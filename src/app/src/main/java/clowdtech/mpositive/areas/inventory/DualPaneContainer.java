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
import clowdtech.mpositive.areas.inventory.views.CategoryViewImpl;
import clowdtech.mpositive.areas.inventory.views.InventoryDualView;
import clowdtech.mpositive.areas.inventory.views.ProductViewImpl;
import clowdtech.mpositive.areas.shared.views.ViewMenuView;
import clowdtech.mpositive.lifecycle.PresentedView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class DualPaneContainer extends RelativeLayout implements ProductManagementView, Container, Presentable {

    @Inject
    ProductManagementPresenter presenter;

    @Bind(R.id.inventory_view)
    protected InventoryDualView inventoryView;

    @Bind(R.id.view_container)
    protected ViewGroup viewContainer;

    private ProductViewImpl productView;

    private CategoryViewImpl categoryView;

    private ViewMenuView viewMenu;

    public DualPaneContainer(Context context, AttributeSet attrs) {
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
        showInventoryView();

        inventoryView.showProducts();
    }

    @Override
    public void productDeleted() {
        showInventoryView();

        inventoryView.showProducts();
    }

    @Override
    public void categoryEdit(Category category) {
        showCategoryEdit(category);
    }

    @Override
    public void categorySaved() {
        showInventoryView();

        inventoryView.showCategories();
    }

    @Override
    public boolean onBackPressed() {
        if (productViewAttached()) {
            showInventoryView();

            inventoryView.showProducts();

            return true;
        }

        if (categoryViewAttached()) {
            showInventoryView();

            inventoryView.showCategories();

            return true;
        }

        return false;
    }

    @Override
    public void navToHomeView() {
        if (productViewAttached() || categoryViewAttached()) {
            showInventoryView();
        }
    }

    @Override
    public void setViewMenu(View view) {
        viewMenu.setViewMenu(view);
    }

    @Override
    public void clearViewMenu() {
        this.viewMenu.clearViewMenu();
    }

    @Override
    public void productViewSelected() {
    }

    @Override
    public void categoryViewSelected() {
    }

    @Override
    public void bindView() {
        this.presenter.bindView(this);
    }

    @Override
    public void unbindViews() {
        ((Presentable)getContentView()).getPresenter().unbindView();
    }

    @Override
    public void bindViews() {
        ((PresentedView)getContentView()).bindView();
    }

    private void showInventoryView() {
        this.viewMenu.clearViewMenu();

        if (!inventoryViewAttached()) {
            injectView(R.layout.inventory_home_dual);
        }

        inventoryView = (InventoryDualView) getContentView();
    }

    private void showProductAddView() {
        this.viewMenu.clearViewMenu();

        if (!productViewAttached()) {
            injectView(R.layout.inventory_product);
        }

        productView = (ProductViewImpl) getContentView();
    }

    private void showProductEditView(IProduct product) {
        showProductAddView();

        productView.setItem(product);
    }

    private void showCategoryEdit(Category cat) {
        this.viewMenu.clearViewMenu();

        if (!categoryViewAttached()) {
            injectView(R.layout.inventory_category);
        }

        categoryView = (CategoryViewImpl) getContentView();

        categoryView.setItem(cat);
    }

    private void injectView(int view) {
        removeContentView();

        inflate(getContext(), view, viewContainer);

        ((PresentedView)getContentView()).bindView();
    }

    private View getContentView() {
        return viewContainer.getChildAt(0);
    }

    private void removeContentView() {
        ((Presentable) getContentView()).getPresenter().unbindView();

        viewContainer.removeViewAt(0);
    }

    private boolean productViewAttached() {
        return productView != null && productView.getParent() != null;
    }

    private boolean categoryViewAttached() {
        return categoryView != null && categoryView.getParent() != null;
    }

    private boolean inventoryViewAttached() {
        return inventoryView.getParent() != null;
    }

    @Override
    public Presenter getPresenter() {
        return this.presenter;
    }
}