package clowdtech.mpositive.areas.till.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import clowdtech.mpositive.App;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.shared.InventoryItem;
import clowdtech.mpositive.areas.till.adapters.InventoryAdapter;
import clowdtech.mpositive.areas.till.presenters.InventoryPresenter;
import clowdtech.mpositive.lifecycle.PresentedView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class InventoryViewImpl extends RelativeLayout implements InventoryView, PresentedView, Presentable {

    @Inject
    protected InventoryPresenter presenter;

    private InventoryAdapter adapter;

    @Bind(R.id.gridview)
    protected GridView gridView;

    @Bind(R.id.progress)
    protected LinearLayout progress;

    public InventoryViewImpl(Context context, AttributeSet attrs) {
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
    public void setInventoryItems(List<InventoryItem> dataSource) {
        adapter = new InventoryAdapter(getContext(), dataSource);

        gridView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void setLoaded() {
        progress.setVisibility(GONE);
        gridView.setVisibility(VISIBLE);
    }

    @Override
    public void refreshProducts() {
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onBackPressed() {
        return presenter.onBackPressed();
    }

    @Override
    public void setLoading() {
        gridView.setVisibility(GONE);
        progress.setVisibility(VISIBLE);
    }

    @OnItemClick(R.id.gridview)
    public void onItemClick(int position) {
        presenter.itemSelected(position);
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
