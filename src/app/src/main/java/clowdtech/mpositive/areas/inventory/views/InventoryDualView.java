package clowdtech.mpositive.areas.inventory.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.clowdtech.data.entities.IProduct;
import com.clowdtech.data.entities.Category;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import clowdtech.mpositive.App;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.inventory.Container;
import clowdtech.mpositive.areas.inventory.activities.ProductManagement;
import clowdtech.mpositive.areas.inventory.presenters.InventoryPresenter;
import clowdtech.mpositive.areas.shared.ViewPage;
import clowdtech.mpositive.areas.shared.ViewPageAdapter;
import clowdtech.mpositive.lifecycle.PresentedView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class InventoryDualView extends LinearLayout implements InventoryView, PresentedView, Presentable {

    @Inject
    protected InventoryPresenter presenter;

    private ProductsView productsView;

    private CategoriesView categoriesView;

    @Bind(R.id.viewPager)
    protected ViewPager viewPager;

    @BindString(R.string.captions_products)
    String captionProducts;

    @BindString(R.string.captions_categories)
    String captionCategories;

    public InventoryDualView(Context context, AttributeSet attrs) {
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

        if (isInEditMode()) {
            return;
        }

        productsView = (ProductsView) View.inflate(getContext(), R.layout.inventory_products, null);
        categoriesView = (CategoriesView) View.inflate(getContext(), R.layout.inventory_categories, null);

        initialiseTwoPagePager(viewPager);
    }

    private void initialiseTwoPagePager(ViewPager pager) {
        List<ViewPage> viewPages = new ArrayList<>();

        viewPages.add(new ViewPage(categoriesView, captionCategories));
        viewPages.add(new ViewPage(productsView, captionProducts));

        initialisePager(pager, viewPages);
    }

    private void initialisePager(ViewPager pager, List<ViewPage> viewPages) {
        ViewPageAdapter pageAdapter = new ViewPageAdapter(viewPages);



        pager.setAdapter(pageAdapter);

        pager.setCurrentItem(1);
    }

    @Override
    public Container getContainer() {
        return ((ProductManagement) getContext()).getContainer();
    }

    @Override
    public void setProducts(List<IProduct> data) {
        productsView.setProducts(data);
    }

    @Override
    public void setCategories(List<Category> data) {
        categoriesView.setCategories(data);
    }

    @Override
    public void showCategories() {
        viewPager.setCurrentItem(0);
    }

    @Override
    public void showProducts() {
        viewPager.setCurrentItem(1);
    }

    @Override
    public void unbindViews() {
        this.categoriesView.getPresenter().unbindView();
        this.productsView.getPresenter().unbindView();
    }

    @Override
    public void bindViews() {
        this.categoriesView.bindView();
        this.productsView.bindView();
    }

    @Override
    public void bindView() {
        presenter.bindView(this);
    }

    @Override
    public Presenter getPresenter() {
        return this.presenter;
    }
}