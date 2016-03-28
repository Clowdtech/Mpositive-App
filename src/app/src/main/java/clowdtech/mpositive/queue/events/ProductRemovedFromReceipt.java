package clowdtech.mpositive.queue.events;

import clowdtech.mpositive.areas.till.contracts.IReceiptLineProduct;

public class ProductRemovedFromReceipt {
    private IReceiptLineProduct line;

    public ProductRemovedFromReceipt(IReceiptLineProduct line) {
        this.line = line;
    }

    public IReceiptLineProduct getLine() {
        return line;
    }
}
