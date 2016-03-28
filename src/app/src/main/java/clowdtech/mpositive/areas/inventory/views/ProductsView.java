package clowdtech.mpositive.areas.inventory.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.clowdtech.data.entities.IProduct;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import clowdtech.mpositive.App;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.inventory.Container;
import clowdtech.mpositive.areas.inventory.activities.ProductManagement;
import clowdtech.mpositive.areas.inventory.adapters.ProductsViewAdapter;
import clowdtech.mpositive.areas.inventory.presenters.ProductsPresenter;
import clowdtech.mpositive.lifecycle.PresentedView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class ProductsView extends RelativeLayout implements IProductsView, PresentedView, Presentable {
    @Inject
    protected ProductsPresenter presenter;

    @Bind(R.id.gridView)
    protected GridView gridView;

    @Bind(R.id.progress)
    protected LinearLayout loader;

    public ProductsView(Context context, AttributeSet attrs) {
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

    @OnClick(R.id.add_item)
    protected void onAddItemClick() {
        presenter.addProduct();
    }

    @OnItemClick(R.id.gridView)
    protected void onProductSelected(int position) {
        presenter.productSelected(position);
    }

    @Override
    public void setProducts(List<IProduct> products) {
        ProductsViewAdapter adapter = new ProductsViewAdapter(getContext(), products);

        gridView.setAdapter(adapter);
    }

    @Override
    public Container getContainer() {
        return ((ProductManagement)getContext()).getContainer();
    }

    @Override
    public void setLoaded() {
        loader.setVisibility(GONE);
        gridView.setVisibility(VISIBLE);
    }

    @Override
    public void setLoading() {
        gridView.setVisibility(GONE);
        loader.setVisibility(VISIBLE);
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
