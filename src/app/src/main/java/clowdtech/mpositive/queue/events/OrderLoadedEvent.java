package clowdtech.mpositive.queue.events;

import com.clowdtech.data.entities.IOrder;

public class OrderLoadedEvent {
    private IOrder order;

    public OrderLoadedEvent(IOrder order) {
        this.order = order;
    }

    public IOrder getOrder() {
        return order;
    }
}
