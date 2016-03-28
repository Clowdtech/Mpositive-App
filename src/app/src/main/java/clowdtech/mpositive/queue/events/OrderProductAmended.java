package clowdtech.mpositive.queue.events;

import clowdtech.mpositive.areas.till.contracts.IReceiptLineProduct;

public class OrderProductAmended {
    private IReceiptLineProduct line;

    public OrderProductAmended(IReceiptLineProduct line) {
        this.line = line;
    }

    public IReceiptLineProduct getLine() {
        return line;
    }
}
