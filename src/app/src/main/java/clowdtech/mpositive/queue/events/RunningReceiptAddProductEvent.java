package clowdtech.mpositive.queue.events;

import java.math.BigDecimal;

public class RunningReceiptAddProductEvent {
    private final long itemId;
    private final String title;
    private final BigDecimal price;

    public RunningReceiptAddProductEvent(long itemId, String title, BigDecimal price) {
        this.itemId = itemId;
        this.title = title;
        this.price = price;
    }

    public long getItemId() {
        return itemId;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
