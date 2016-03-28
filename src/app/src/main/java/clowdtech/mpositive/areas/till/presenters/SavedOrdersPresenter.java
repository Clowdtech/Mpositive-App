package clowdtech.mpositive.areas.till.presenters;

import com.clowdtech.data.entities.IOrder;
import com.clowdtech.data.repository.IOrderRepository;

import java.util.List;

import javax.inject.Inject;

import clowdtech.mpositive.areas.till.views.SavedOrdersView;
import clowdtech.mpositive.data.RunningOrder;
import clowdtech.mpositive.queue.IEventBus;
import clowdtech.mpositive.queue.events.OrderLoadEvent;
import clowdtech.mpositive.ui.BasePresenter;

public class SavedOrdersPresenter extends BasePresenter<SavedOrdersView> {
    private IOrderRepository orderRepository;
    private IEventBus eventBus;
    private RunningOrder runningOrder;

    private List<IOrder> orders;

    private IOrder order;

    @Inject
    public SavedOrdersPresenter(IOrderRepository orderRepository, IEventBus eventBus, RunningOrder runningOrder) {
        this.orderRepository = orderRepository;
        this.eventBus = eventBus;
        this.runningOrder = runningOrder;
    }

    @Override
    public void bindView(SavedOrdersView view) {
        super.bindView(view);

        orders = orderRepository.getOrders(); // db call move out of view render (loader pattern?)

        if (orders.size() > 0) {
            this.view.hideNoOrdersIndicator();
            this.view.showOrders(orders);
        } else {
            this.view.hideOrders();
            this.view.showNoOrdersIndicator();
        }
    }

    public void orderSelected(int position) {
        order = orders.get(position);

        if (runningOrder.hasCurrentOrder()) {
            this.view.confirmDiscardRunningOrder();

            return;
        }

        loadOrder();
    }

    public void loadOrder() {
        // set loading

        eventBus.post(new OrderLoadEvent(order));
    }
}
