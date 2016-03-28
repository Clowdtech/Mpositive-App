package clowdtech.mpositive.queue.events;

public class OrderSaveEvent {
    private String reference;

    public OrderSaveEvent(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }
}
