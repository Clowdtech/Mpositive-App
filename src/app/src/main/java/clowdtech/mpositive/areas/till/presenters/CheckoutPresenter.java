package clowdtech.mpositive.areas.till.presenters;

import com.squareup.otto.Subscribe;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.inject.Inject;

import clowdtech.mpositive.areas.till.views.CheckoutView;
import clowdtech.mpositive.queue.IEventBus;
import clowdtech.mpositive.queue.events.OrderLoadedEvent;
import clowdtech.mpositive.queue.events.OrderValueChangeEvent;
import clowdtech.mpositive.ui.BasePresenter;

public class CheckoutPresenter extends BasePresenter<CheckoutView> {
    private final DecimalFormat currencyFormatter;

    private IEventBus eventBus;

    @Inject
    public CheckoutPresenter(IEventBus eventBus) {
        this.eventBus = eventBus;

        this.currencyFormatter = (DecimalFormat) NumberFormat.getCurrencyInstance();
    }

    @Override
    public void bindView(CheckoutView view) {
        super.bindView(view);

        view.bindViews();

        eventBus.register(this);
    }

    @Override
    public void unbindView() {
        view.unbindViews();

        super.unbindView();

        eventBus.unregister(this);
    }

    @Subscribe
    public void subscribeReceiptTotalChanged(OrderValueChangeEvent event) {
        setTotal(event.getTotal());
    }

    @Subscribe
    public void subscribeOrderLoaded(OrderLoadedEvent event) {
        String reference = String.format("Order: %s", event.getOrder().getReference());

        this.view.setOrderReference(reference);

        this.view.displayOrderReference();
    }

    public void chargeRequested() {
        this.view.inventoryHandledBack();
    }

    private void setTotal(BigDecimal total) {
        String formattedTotal = currencyFormatter.format(total);

        if (total.compareTo(BigDecimal.ZERO) == 0) {
            this.view.setChargeText("Charge");
            this.view.setChargeEnabled(false);
        } else {
            this.view.setChargeText(String.format("Charge: %s", formattedTotal));
            this.view.setChargeEnabled(true);
        }
    }

    public boolean isBackHandled() {
        return this.view.inventoryHandledBack();
    }

    public void reset() {
        this.view.resetEditableReceipt();
    }
}
