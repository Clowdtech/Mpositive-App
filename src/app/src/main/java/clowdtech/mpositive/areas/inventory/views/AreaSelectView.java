package clowdtech.mpositive.areas.inventory.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import clowdtech.mpositive.App;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.inventory.Container;
import clowdtech.mpositive.areas.inventory.activities.ProductManagement;
import clowdtech.mpositive.areas.inventory.presenters.AreaSelectPresenter;
import clowdtech.mpositive.lifecycle.PresentedView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class AreaSelectView extends LinearLayout implements IAreaSelectView, PresentedView, Presentable {
    @Inject
    protected AreaSelectPresenter presenter;

    @Bind(R.id.product_area_select)
    protected ListView areaSelect;

    public AreaSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) {
            return;
        }

        ((App) context.getApplicationContext()).getApplicationComponent().inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        String[] names = new String[]{"Categories","Products"};

        ButterKnife.bind(this);

        areaSelect.setAdapter(new ArrayAdapter<>(getContext(),
                R.layout.list_selection_row,
                R.id.list_row_title, names));

        areaSelect.setItemChecked(1, true);
    }

    @Override
    public Container getContainer() {
        return ((ProductManagement)getContext()).getContainer();
    }

    @OnItemClick(R.id.product_area_select)
    public void onAreaSelect(int position) {
        switch (position) {
            case 1:
                presenter.showProductsView();
                break;
            case 0:
                presenter.showCategoriesView();
                break;
        }
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
