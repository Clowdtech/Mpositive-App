package clowdtech.mpositive.queue.events;

import com.clowdtech.data.entities.IOrderLineManual;
import com.clowdtech.data.entities.ITransactionLineManual;

public class RemoveManualEntryFromReceiptEvent {
    private IOrderLineManual line;

    public RemoveManualEntryFromReceiptEvent(IOrderLineManual line) {
        this.line = line;
    }

    public IOrderLineManual getLine() {
        return line;
    }
}
