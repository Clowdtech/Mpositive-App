package clowdtech.mpositive.queue.events;

import java.math.BigDecimal;

public class RunningReceiptAddManualEntryEvent {
    private final String note;
    private final BigDecimal currentValue;

    public RunningReceiptAddManualEntryEvent(String note, BigDecimal currentValue) {
        this.note = note;
        this.currentValue = currentValue;
    }

    public BigDecimal getValue() {
        return currentValue;
    }

    public String getNote() {
        return note;
    }
}
