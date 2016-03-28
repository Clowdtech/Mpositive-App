package clowdtech.mpositive.areas.till.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import clowdtech.mpositive.App;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.shared.views.ReadOnlyReceiptView;
import clowdtech.mpositive.areas.till.activities.TillActivity;
import clowdtech.mpositive.areas.till.presenters.PaymentCompletePresenter;
import clowdtech.mpositive.ioc.components.CheckoutComponent;
import clowdtech.mpositive.lifecycle.PresentedView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class PaymentCompleteDualView extends RelativeLayout implements IPaymentCompleteView, PresentedView, Presentable {

    @Bind(R.id.tender_payment_container)
    protected TransactionCompleteViewImpl completeView;

    @Bind(R.id.till_receipt_read_only)
    protected ReadOnlyReceiptView roReceiptView;

    @Bind(R.id.tender_print_container)
    protected View printReceipt;

    @Bind(R.id.receipt_total)
    protected TextView totalView;

    @Inject
    protected PaymentCompletePresenter presenter;

    public PaymentCompleteDualView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) {
            return;
        }

        CheckoutComponent checkoutComponent = ((App) context.getApplicationContext()).getCheckoutComponent();

        checkoutComponent.inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.bind(this);
    }

    public void initialise(long receiptId) {
        this.presenter.setItem(receiptId);
    }

    public boolean isBackHandled() {
        return this.presenter.isBackHandled();
    }

    public void setReadOnlyReceiptItems(ArrayList<IReceiptLineView> receiptLineViewModels) {
        this.roReceiptView.setData(receiptLineViewModels);
    }

    @Override
    public void completeCheckout() {
        ((TillActivity)getContext()).getContainer().navigateToCheckout();
    }

    @Override
    public void unbindViews() {
        roReceiptView.getPresenter().unbindView();
        completeView.getPresenter().unbindView();
    }

    @Override
    public void bindViews() {
        roReceiptView.bindView();
        completeView.bindView();
    }

    @Override
    public void showPrintOption() {
        printReceipt.setVisibility(VISIBLE);
    }

    @Override
    public void hidePrintOption() {
        printReceipt.setVisibility(GONE);
    }

    @Override
    public void setPaymentValue(String total) {
        totalView.setText(total);
    }

    @OnClick(R.id.tender_print_receipt)
    public void printReceipt() {
        this.presenter.printReceipt();
    }

    @OnClick(R.id.tender_no_receipt)
    public void noReceiptClick() {
        this.presenter.noReceipt();
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
