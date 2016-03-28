package clowdtech.mpositive.areas.till;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import clowdtech.mpositive.App;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.till.views.CheckoutDualView;
import clowdtech.mpositive.areas.till.views.PaymentCompleteDualView;
import clowdtech.mpositive.areas.till.views.TenderViewDual;
import clowdtech.mpositive.lifecycle.PresentedView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class DualPaneContainer extends RelativeLayout implements Container, TillView, Presentable {
    @Inject
    protected TillPresenter presenter;

    @Bind(R.id.checkout_view)
    protected CheckoutDualView checkoutView;

    @Bind(R.id.view_container)
    protected ViewGroup viewContainer;

    private TenderViewDual tenderView;

    private PaymentCompleteDualView completeView;

    public DualPaneContainer(Context context, AttributeSet attrs) {
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
    public boolean onBackPressed() {
        return this.presenter.isBackHandled();
    }

    @Override
    public void bindView() {
        presenter.bindView(this);
    }

    @Override
    public void bindViews() {
        ((PresentedView)getContentView()).bindView();
    }

    @Override
    public void unbindViews() {
        ((Presentable)getContentView()).getPresenter().unbindView();
    }

    @Override
    public boolean isPaymentChoiceInView() {
        return tenderAttached();
    }

    @Override
    public boolean isCheckoutBackHandled() {
        return checkoutView.isBackHandled();
    }

    @Override
    public void displayCheckout() {
        if (!checkoutAttached()) {
            injectView(R.layout.till_checkout_dual);
        }

        checkoutView = (CheckoutDualView) getContentView();
    }

    public void displayPaymentChoice() {
        if (!tenderAttached()) {
            injectView(R.layout.till_tender_dual);
        }

        tenderView = (TenderViewDual) getContentView();
    }

    public void displayPaymentComplete(long receiptId) {
        if (!completeAttached()) {
            injectView(R.layout.till_complete_dual);
        }

        completeView = (PaymentCompleteDualView) getContentView();

        completeView.initialise(receiptId);
    }

    @Override
    public void displaySavedOrders() {

    }

    @Override
    public void navigateToCheckout() {
        this.presenter.navigateToCheckout();
    }

    @Override
    public void navigateToPaymentChoice() {
        this.presenter.navigateToPaymentChoice();
    }

    @Override
    public void navigateToPaymentComplete(long receiptId) {
        this.presenter.navigateToPaymentComplete(receiptId);
    }

    @Override
    public boolean isBackHandled() {
        if (completeAttached()) {
            return true;
        }

        if (tenderAttached()) {
            displayCheckout();

            return true;
        }

        return this.checkoutView.isBackHandled();
    }

    @Override
    public void clearReceipt() {
        checkoutView.reset();
    }


    public boolean checkoutAttached() {
        return checkoutView != null && checkoutView.getParent() != null;
    }

    public boolean tenderAttached() {
        return tenderView != null && tenderView.getParent() != null;
    }

    public boolean completeAttached() {
        return completeView != null && completeView.getParent() != null;
    }


    @Override
    public Presenter getPresenter() {
        return presenter;
    }

    private View getContentView() {
        return viewContainer.getChildAt(0);
    }

    private void removeContentView() {
        ((Presentable)getContentView()).getPresenter().unbindView();

        viewContainer.removeViewAt(0);
    }

    public void injectView(int view) {
        removeContentView();

        inflate(getContext(), view, viewContainer);

        ((PresentedView)getContentView()).bindView();
    }
}

