package clowdtech.mpositive.areas.inventory.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.clowdtech.data.entities.Category;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import clowdtech.mpositive.App;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.inventory.adapters.CategoriesAdapter;
import clowdtech.mpositive.areas.inventory.presenters.CategorySelectPresenter;
import clowdtech.mpositive.areas.inventory.viewModels.CategoryViewModel;
import clowdtech.mpositive.areas.shared.views.ExpandedHeightGridView;
import clowdtech.mpositive.lifecycle.PresentedView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class CategorySelectView extends LinearLayout implements ICategorySelection, PresentedView, Presentable {
    @Inject
    protected CategorySelectPresenter presenter;

    private CategoriesAdapter adapter;

    @Bind(R.id.gridView)
    protected ExpandedHeightGridView gridView;

    @Bind(R.id.add_item)
    protected Button addNewItem;

    @Bind(R.id.searchInput)
    protected EditText searchInput;

    private OnItemSelectionListener<CategoryViewModel> categorySelectionListener;
    private OnClickListener newCategoryListener;

    private boolean newItemAvailable = true;
    private boolean searchAvailable = true;

    private int itemTemplateId;

    public CategorySelectView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setStyleAttrs(attrs);

        inflate(getContext(), R.layout.inventory_categories_select, this);

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
    }

    private void setStyleAttrs(AttributeSet attrs) {
        TypedArray styleAttrs = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.CategorySelectView);

        newItemAvailable = styleAttrs.getBoolean(
                R.styleable.CategorySelectView_newItemAvailable, true);

        searchAvailable = styleAttrs.getBoolean(
                R.styleable.CategorySelectView_searchAvailable, true);

        itemTemplateId = styleAttrs.getResourceId(
                R.styleable.CategorySelectView_itemLayout, R.layout.template_category_select);

        styleAttrs.recycle();
    }

    public void initialiseView(ArrayList<CategoryViewModel> dataSource) {
        adapter = new CategoriesAdapter(getContext(), dataSource, itemTemplateId, this);

        gridView.setAdapter(adapter);
    }

    public void refreshView(List<Category> dataSource) {
        presenter.resetData(dataSource);
    }

    public void filterGrid(String query) {
        adapter.getFilter().filter(query);
    }

    public void setCategories(List<Category> categories) {
        presenter.setCategories(categories);
    }

    // timing issues around these as calling these post render will only cause issues on re-render..
    public void hideNewItemOption() {
        this.addNewItem.setVisibility(GONE);
    }

    public void setOnCategorySelect(OnItemSelectionListener<CategoryViewModel> onClickListener) {
        this.categorySelectionListener = onClickListener;
    }

    public void setOnCategoryCreate(OnClickListener onClickListener) {
        this.newCategoryListener = onClickListener;
    }

    public void notifyDataChanged() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void categorySelected(CategoryViewModel category) {
        if (categorySelectionListener != null) {
            categorySelectionListener.onItemSelected(category);
        }
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
