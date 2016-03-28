package clowdtech.mpositive.areas.till.views;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clowdtech.data.entities.IOrder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import clowdtech.mpositive.App;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.till.Container;
import clowdtech.mpositive.areas.till.activities.TillActivity;
import clowdtech.mpositive.areas.till.presenters.SavedOrdersPresenter;
import clowdtech.mpositive.lifecycle.PresentedView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class SavedOrdersViewImpl extends RelativeLayout implements SavedOrdersView, Presentable, PresentedView {

    @Inject
    SavedOrdersPresenter presenter;

    @Bind(R.id.saved_orders)
    protected ListView listView;

    @Bind(R.id.noOrdersIndicator)
    protected TextView noOrdersIndicator;

    private OrderDiscardDialogImpl orderDiscardDialog;

    public SavedOrdersViewImpl(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) {
            return;
        }

        orderDiscardDialog = new OrderDiscardDialogImpl(getContext());

        ((App)context.getApplicationContext()).getCheckoutComponent().inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (isInEditMode()) {
            return;
        }

        orderDiscardDialog.setPositiveClick(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.loadOrder();
            }
        });

        ButterKnife.bind(this);
    }

    @Override
    public void showOrders(List<IOrder> orders) {
        List<String> items = new ArrayList<>();

        for (IOrder order :
                orders) {
            items.add(order.getReference());
        }

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, items);

        listView.setAdapter(itemsAdapter);
    }

    @Override
    public Container getContainer() {
        return ((TillActivity)getContext()).getContainer();
    }

    @Override
    public void confirmDiscardRunningOrder() {
        orderDiscardDialog.show();
    }

    @Override
    public void showNoOrdersIndicator() {
        noOrdersIndicator.setVisibility(VISIBLE);
    }

    @Override
    public void hideNoOrdersIndicator() {
        noOrdersIndicator.setVisibility(GONE);
    }

    @Override
    public void hideOrders() {
        listView.setVisibility(GONE);
    }

    @OnItemClick(R.id.saved_orders)
    public void onOrderSelected(int position) {
        this.presenter.orderSelected(position);
    }

    @Override
    public Presenter getPresenter() {
        return this.presenter;
    }

    @Override
    public void bindView() {
        presenter.bindView(this);
    }
}
