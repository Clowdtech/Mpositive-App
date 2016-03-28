package clowdtech.mpositive.areas.inventory.presenters;

import javax.inject.Inject;

import clowdtech.mpositive.areas.inventory.views.InventoryView;
import clowdtech.mpositive.queue.IEventBus;
import clowdtech.mpositive.ui.BasePresenter;

public class InventoryPresenter extends BasePresenter<InventoryView> {
    private IEventBus eventBus;

    @Inject
    public InventoryPresenter(IEventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void bindView(InventoryView view) {
        super.bindView(view);

        this.view.bindViews();

        eventBus.register(this);
    }

    @Override
    public void unbindView() {
        view.unbindViews();

        super.unbindView();

        eventBus.unregister(this);
    }
}
