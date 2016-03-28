package clowdtech.mpositive.areas.till.presenters;

import javax.inject.Inject;

import clowdtech.mpositive.areas.till.views.SaveOrderView;
import clowdtech.mpositive.queue.IEventBus;
import clowdtech.mpositive.queue.events.OrderSaveEvent;
import clowdtech.mpositive.ui.BasePresenter;

public class OrderSaveDialogPresenter extends BasePresenter<SaveOrderView> {
    private IEventBus eventBus;

    @Inject
    public OrderSaveDialogPresenter(IEventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void saveOrder() {
        String reference = this.view.getReference();

        if (reference.trim().equals("")) {
            return;
        }

        eventBus.post(new OrderSaveEvent(reference));

        this.view.dismiss();
    }
}
