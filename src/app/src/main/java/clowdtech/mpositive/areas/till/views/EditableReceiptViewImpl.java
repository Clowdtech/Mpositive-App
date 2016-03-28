package clowdtech.mpositive.areas.till.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import clowdtech.mpositive.App;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.shared.adapters.ReceiptAdapter;
import clowdtech.mpositive.areas.till.presenters.EditableReceiptPresenter;
import clowdtech.mpositive.ioc.components.CheckoutComponent;
import clowdtech.mpositive.lifecycle.PresentedView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class EditableReceiptViewImpl extends RelativeLayout implements PresentedView, Presentable, EditableReceiptView {

    @Inject
    protected EditableReceiptPresenter presenter;

    @Bind(R.id.checkout_lines)
    protected ListView listView;

    @Bind(R.id.till_print_order)
    protected Button printOrder;

    private SaveOrderDialogImpl saveDialog;

    public EditableReceiptViewImpl(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) {
            return;
        }

        saveDialog = new SaveOrderDialogImpl(context);

        CheckoutComponent checkoutComponent = ((App) context.getApplicationContext()).getCheckoutComponent();

        checkoutComponent.inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.bind(this);
    }

    public void reset() {
        this.presenter.reset();
    }

    public void setReceiptLines(List<IReceiptLineView> dataSource) {
        ReceiptAdapter adapter = new ReceiptAdapter(getContext(), dataSource);

        listView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.till_print_order)
    public void printReceipt() {
        presenter.printRunningReceipt();
    }

    @OnClick(R.id.till_clear_order)
    public void clearReceipt() {
        presenter.clearRunningReceipt();
    }

    @OnClick(R.id.till_save_order)
    public void saveOrder() {
        presenter.requestSaveOrder();
    }

    @Override
    public void bindView() {
        presenter.bindView(this);
    }

    public void showPrintOrder() {
        printOrder.setVisibility(VISIBLE);
    }

    public void hidePrintOrder() {
        printOrder.setVisibility(GONE);
    }

    @Override
    public void showOrderSave() {
        saveDialog.show();
    }

    @Override
    public void bindViews() {
        saveDialog.bindView();
    }

    @Override
    public void unbindViews() {
        saveDialog.getPresenter().unbindView();
    }

    @Override
    public Presenter getPresenter() {
        return this.presenter;
    }
}

