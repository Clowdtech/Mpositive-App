package clowdtech.mpositive.queue.events;

import java.math.BigDecimal;

public class OrderValueChangeEvent {
    private BigDecimal total;

    public OrderValueChangeEvent(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTotal() {
        return total;
    }
}
