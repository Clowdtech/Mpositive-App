package clowdtech.mpositive.receipt;

import clowdtech.mpositive.TaskListener;
import clowdtech.mpositive.data.transactions.entities.Receipt;

public interface IReceiptExporter {
    void printReceipt(Receipt receipt, TaskListener listener);
}

