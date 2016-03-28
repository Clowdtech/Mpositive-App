package clowdtech.mpositive.areas.inventory.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.clowdtech.data.entities.IProduct;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import clowdtech.mpositive.App;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.inventory.adapters.ProductsAdapter;
import clowdtech.mpositive.areas.inventory.presenters.ProductSelectPresenter;
import clowdtech.mpositive.areas.shared.InventoryItem;
import clowdtech.mpositive.areas.shared.views.ExpandedHeightGridView;
import clowdtech.mpositive.lifecycle.PresentedView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class ProductSelectView extends LinearLayout implements IProductSelection, PresentedView, Presentable {
    @Inject
    protected ProductSelectPresenter presenter;

    private ProductsAdapter adapter;

    @Bind(R.id.gridView)
    protected ExpandedHeightGridView gridView;

    @Bind(R.id.add_item)
    protected Button addNewItem;

    @Bind(R.id.progress)
    protected LinearLayout progress;

    @Bind(R.id.view_container)
    protected LinearLayout viewContainer;

    @Bind(R.id.searchInput)
    protected EditText searchInput;

    private OnItemSelectionListener<InventoryItem> itemSelectionListener;
    private OnClickListener newCategoryListener;

    private boolean newItemAvailable = true;
    private boolean searchAvailable = true;

    private int itemTemplateId;

    public ProductSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setStyleAttrs(attrs);

        inflate(getContext(), R.layout.inventory_products_select, this);

        ButterKnife.bind(this);

        if (!searchAvailable) {
            searchInput.setVisibility(GONE);
        }

        if (newItemAvailable) {
            addNewItem.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (newCategoryListener != null) {
                        newCategoryListener.onClick(v);
                    }
                }
            });
        } else {
            addNewItem.setVisibility(GONE);
        }

        if (isInEditMode()) {
            return;
        }

        ((App) context.getApplicationContext()).getApplicationComponent().inject(this);

//        presenter.bindView(this);
    }

    private void setStyleAttrs(AttributeSet attrs) {
        TypedArray styleAttrs = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.ProductSelectView);

        newItemAvailable = styleAttrs.getBoolean(
                R.styleable.ProductSelectView_newItemAvailable, true);

        searchAvailable = styleAttrs.getBoolean(
                R.styleable.ProductSelectView_searchAvailable, true);

        itemTemplateId = styleAttrs.getResourceId(
                R.styleable.ProductSelectView_itemLayout, R.layout.template_category_select);

        styleAttrs.recycle();
    }

    public void initialiseView(ArrayList<InventoryItem> dataSource) {
        adapter = new ProductsAdapter(getContext(), dataSource, itemTemplateId, this);

        gridView.setAdapter(adapter);
    }

    public void refreshView(List<IProduct> dataSource) {
        presenter.resetData(dataSource);
    }

    public void filterGrid(String query) {
        adapter.getFilter().filter(query);
    }

    public void setItems(List<IProduct> products) {
        presenter.setProducts(products);
    }

    public void hideNewItemOption() {
        this.addNewItem.setVisibility(GONE);
    }

    public void setOnProductSelect(OnItemSelectionListener<InventoryItem> onClickListener) {
        this.itemSelectionListener = onClickListener;
    }

    public void setOnProductCreate(OnClickListener onClickListener) {
        this.newCategoryListener = onClickListener;
    }

    public void notifyDataChanged() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void productSelected(InventoryItem product) {
        if (itemSelectionListener != null) {
            itemSelectionListener.onItemSelected(product);
        }
    }

    @Override
    public void setLoading() {
        this.viewContainer.setVisibility(INVISIBLE);
        this.progress.setVisibility(VISIBLE);
    }

    @Override
    public void setLoaded() {
        this.progress.setVisibility(GONE);
        this.viewContainer.setVisibility(VISIBLE);
    }

    @Override
    public void bindView() {
        presenter.bindView(this);
    }

    @OnTextChanged(R.id.searchInput)
    public void onSearchTextChanged(CharSequence text) {
        adapter.getFilter().filter(text.toString());
    }

    @Override
    public Presenter getPresenter() {
        return this.presenter;
    }
}
