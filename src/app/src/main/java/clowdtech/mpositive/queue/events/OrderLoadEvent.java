package clowdtech.mpositive.queue.events;

import com.clowdtech.data.entities.IOrder;

public class OrderLoadEvent {
    private IOrder orderId;

    public OrderLoadEvent(IOrder orderId) {
        this.orderId = orderId;
    }

    public IOrder getOrder() {
        return orderId;
    }
}
