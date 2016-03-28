package clowdtech.mpositive.queue.events;

import com.clowdtech.data.entities.IOrderLineManual;
import com.clowdtech.data.entities.ITransactionLineManual;

public class ManualEntryRemovedFromReceipt {
    private IOrderLineManual line;

    public ManualEntryRemovedFromReceipt(IOrderLineManual line) {
        this.line = line;
    }

    public IOrderLineManual getLine() {
        return line;
    }
}
