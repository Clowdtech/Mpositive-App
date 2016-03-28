package clowdtech.mpositive.areas.till.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import clowdtech.mpositive.App;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.shared.views.ReadOnlyReceiptView;
import clowdtech.mpositive.areas.till.activities.TillActivity;
import clowdtech.mpositive.areas.till.presenters.TenderPresenter;
import clowdtech.mpositive.data.transactions.entities.ReceiptPayment;
import clowdtech.mpositive.lifecycle.PresentedView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class TenderViewDual extends RelativeLayout implements TenderView, PresentedView, Presentable {

    @Bind(R.id.payment_method)
    protected RecordPaymentViewImpl paymentView;

    @Bind(R.id.receipt_read_only)
    protected ReadOnlyReceiptView roReceiptView;

    @Bind(R.id.receipt_total)
    protected TextView tillTotal;

    @Inject
    protected TenderPresenter presenter;

    public TenderViewDual(Context context, AttributeSet attrs) {
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
    public boolean isPaymentChoiceInview() {
        return false;
    }

    @Override
    public void setPaymentValue(BigDecimal paymentDefault) {
        this.paymentView.setDefaultValue(paymentDefault);
    }

    @Override
    public ReceiptPayment getPaymentValue() {
        return this.paymentView.getPayment();
    }

    @Override
    public void setReadOnlyReceiptItems(ArrayList<IReceiptLineView> receiptLineViewModels) {
        this.roReceiptView.setData(receiptLineViewModels);
    }

    @Override
    public void displayPaymentComplete(long receiptId) {
        ((TillActivity) getContext()).getContainer().navigateToPaymentComplete(receiptId);
    }

    @Override
    public void setTotal(String total) {
        tillTotal.setText(total);
    }

    @Override
    public void unbindViews() {
        paymentView.getPresenter().unbindView();
        roReceiptView.getPresenter().unbindView();
    }

    @Override
    public void bindViews() {
        paymentView.bindView();
        roReceiptView.bindView();
    }

    public boolean isBackHandled() {
        return this.presenter.isBackHandled();
    }

    @OnClick(R.id.completeTransaction)
    public void completePayment() {
        presenter.completePayment();
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
