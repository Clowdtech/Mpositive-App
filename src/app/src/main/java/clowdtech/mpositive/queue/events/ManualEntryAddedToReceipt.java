package clowdtech.mpositive.queue.events;

import com.clowdtech.data.entities.IOrderLineManual;

import clowdtech.mpositive.data.lines.EntryLineManual;

public class ManualEntryAddedToReceipt {
    private IOrderLineManual line;

    public ManualEntryAddedToReceipt(IOrderLineManual line) {
        this.line = line;
    }

    public IOrderLineManual getLine() {
        return line;
    }
}
