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
import clowdtech.mpositive.areas.till.views.CheckoutSingleView;
import clowdtech.mpositive.areas.till.views.PaymentCompleteSingleView;
import clowdtech.mpositive.areas.till.views.SavedOrdersViewImpl;
import clowdtech.mpositive.areas.till.views.TenderViewSingle;
import clowdtech.mpositive.lifecycle.PresentedView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class SinglePaneContainer extends RelativeLayout implements Container, TillView, Presentable {

    @Inject
    protected TillPresenter presenter;

    @Bind(R.id.checkout_view)
    protected CheckoutSingleView checkoutView;

    @Bind(R.id.view_container)
    protected ViewGroup viewContainer;

    private TenderViewSingle tenderView;

    private PaymentCompleteSingleView completeView;

    private SavedOrdersViewImpl savedOrdersView;


    public SinglePaneContainer(Context context, AttributeSet attrs) {
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
    public boolean isPaymentChoiceInView() {
        return tenderAttached() && !this.tenderView.isBackHandled();
    }

    @Override
    public boolean isCheckoutBackHandled() {
        return checkoutAttached() && checkoutView.isBackHandled();
    }

    @Override
    public void displayCheckout() {
        if (!checkoutAttached()) {
            injectView(R.layout.till_checkout_single);
        }

        checkoutView = (CheckoutSingleView) getContentView();
    }

    @Override
    public void displayPaymentChoice() {
        if (!isPaymentChoiceInView()) {
            injectView(R.layout.till_tender_single);
        }

        tenderView = (TenderViewSingle) getContentView();
    }

    @Override
    public void displayPaymentComplete(long receiptId) {
        if (!completeAttached()) {
            injectView(R.layout.till_complete_single);
        }

        completeView = (PaymentCompleteSingleView) getContentView();

        completeView.initialise(receiptId);
    }

    @Override
    public void displaySavedOrders() {
        if (!savedOrdersAttached()) {
            injectView(R.layout.till_saved_orders);
        }

        savedOrdersView = (SavedOrdersViewImpl) getContentView();
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
        return checkoutView.isBackHandled();
    }

    @Override
    public void clearReceipt() {
        checkoutView.reset();
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
    public void bindView() {
        presenter.bindView(this);
    }

    private boolean checkoutAttached() {
        return checkoutView != null && checkoutView.getParent() != null;
    }

    private boolean tenderAttached() {
        return tenderView != null && tenderView.getParent() != null;
    }

    private boolean completeAttached() {
        return completeView != null && completeView.getParent() != null;
    }

    private boolean savedOrdersAttached() {
        return savedOrdersView != null && savedOrdersView.getParent() != null;
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