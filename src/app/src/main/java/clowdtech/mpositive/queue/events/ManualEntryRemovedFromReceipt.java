package clowdtech.mpositive.queue.events;

import com.clowdtech.data.entities.IOrderLineManual;

public class ManualEntryRemovedFromReceipt {
    private IOrderLineManual line;

    public ManualEntryRemovedFromReceipt(IOrderLineManual line) {
        this.line = line;
    }

    public IOrderLineManual getLine() {
        return line;
    }
}
