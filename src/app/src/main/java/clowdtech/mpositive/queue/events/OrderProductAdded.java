package clowdtech.mpositive.queue.events;

import com.clowdtech.data.entities.ITransactionLineProduct;

public class OrderProductAdded {
    private ITransactionLineProduct line;

    public OrderProductAdded(ITransactionLineProduct line) {
        this.line = line;
    }

    public ITransactionLineProduct getLine() {
        return line;
    }
}
