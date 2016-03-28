package clowdtech.mpositive.queue.events;

public class RemoveProductFromReceiptEvent {
    private long productId;

    public RemoveProductFromReceiptEvent(long productId) {
        this.productId = productId;
    }

    public long getProductId() {
        return productId;
    }
}
