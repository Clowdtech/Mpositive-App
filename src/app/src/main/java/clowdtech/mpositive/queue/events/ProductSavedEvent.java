package clowdtech.mpositive.queue.events;

public class ProductSavedEvent {
    private long productId;

    public ProductSavedEvent(long productId) {
        this.productId = productId;
    }

    public long getProductId() {
        return productId;
    }
}
