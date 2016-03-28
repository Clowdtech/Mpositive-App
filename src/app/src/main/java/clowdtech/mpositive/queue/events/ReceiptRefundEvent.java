package clowdtech.mpositive.queue.events;

public class ReceiptRefundEvent {
    private long id;

    public ReceiptRefundEvent(long id) {

        this.id = id;
    }

    public long getId() {
        return id;
    }
}
