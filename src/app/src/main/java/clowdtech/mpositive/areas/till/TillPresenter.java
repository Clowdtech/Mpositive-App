package clowdtech.mpositive.areas.till;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.squareup.otto.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.ButterKnife;
import clowdtech.mpositive.data.RunningOrder;
import clowdtech.mpositive.queue.IEventBus;
import clowdtech.mpositive.queue.events.MenuItemVisibilityEvent;
import clowdtech.mpositive.queue.events.MenuSelectedEvent;
import clowdtech.mpositive.queue.events.OrderLoadEvent;
import clowdtech.mpositive.queue.events.OrderLoadedEvent;
import clowdtech.mpositive.ui.BasePresenter;

@Singleton
public class TillPresenter extends BasePresenter<TillView> {
    private ViewNum currentViewNum = ViewNum.Checkout;

    private String viewNumKey = "ViewNum";
    private IEventBus eventBus;
    private RunningOrder runningOrder;

    @Inject
    public TillPresenter(IEventBus eventBus, RunningOrder runningOrder) {
        this.eventBus = eventBus;
        this.runningOrder = runningOrder;
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);

        if (bundle != null && bundle.getSerializable(viewNumKey) != null) {
            ViewNum serializable = (ViewNum) bundle.getSerializable(viewNumKey);

            this.currentViewNum = serializable;

            if (this.view == null) {
                return;
            }

            if (serializable != null)
                setView(serializable);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);

        bundle.putSerializable(viewNumKey, currentViewNum);
    }

    @Override
    public void bindView(TillView view) {
        super.bindView(view);

        this.view.bindViews();

        setView(currentViewNum);

        eventBus.register(this);
    }

    @Override
    public void unbindView() {
        this.view.unbindViews();

        super.unbindView();

        eventBus.unregister(this);
    }

    @Subscribe
    public void subscribeMenuItem(MenuSelectedEvent event) {
        navigateToOrders();
    }

    @Subscribe
    public void subscribeOrderLoaded(OrderLoadedEvent event) {
        navigateToCheckout();
    }

    // should this move towards a state model rather than asking the view for state???
    public boolean isBackHandled() {
        //TODO: shouts state machine
        switch (currentViewNum) {
            case Checkout:
                if (this.view.isCheckoutBackHandled()) {
                    return true;
                }

                break;
            case Tender:
                navigateToCheckout();

                return true;
            case Orders:
                navigateToCheckout();

                return true;
        }

        return false;
    }


    public void navigateToCheckout() {
        this.currentViewNum = ViewNum.Checkout;

        this.view.displayCheckout();

        this.eventBus.post(new MenuItemVisibilityEvent(true));
    }

    public void navigateToPaymentChoice() {
        this.currentViewNum = ViewNum.Tender;

        this.view.displayPaymentChoice();

        this.eventBus.post(new MenuItemVisibilityEvent(false));
    }

    public void navigateToPaymentComplete(long receiptId) {
        this.currentViewNum = ViewNum.Payment;

        this.view.displayPaymentComplete(receiptId);

        this.eventBus.post(new MenuItemVisibilityEvent(false));
    }

    public void navigateToOrders() {
        this.currentViewNum = ViewNum.Orders;

        this.view.displaySavedOrders();

        this.eventBus.post(new MenuItemVisibilityEvent(false));
    }

    private void setView(ViewNum serializable) {
        switch (serializable) {
            case Checkout:
                this.view.displayCheckout();
                break;
            case Tender:
                this.view.displayPaymentChoice();
                break;
            case Payment:
                this.view.displayPaymentComplete(0); // what?
                break;
            case Orders:
                this.view.displaySavedOrders();
                break;
        }
    }
}
