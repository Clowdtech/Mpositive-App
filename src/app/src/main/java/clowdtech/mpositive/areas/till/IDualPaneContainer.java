package clowdtech.mpositive.areas.till;

import com.clowdtech.data.entities.ITransactionLineManual;
import com.clowdtech.data.entities.ITransactionLineProduct;

public interface IDualPaneContainer {
    void displayCheckout();

    void addReceiptEntry(ITransactionLineProduct line);

    void addReceiptEntry(ITransactionLineManual line);

    void refreshRunningTotal();

    void displayPaymentChoice();

    void displayPaymentComplete(long receiptId);

    boolean isBackHandled();

    void clearReceipt();
}
