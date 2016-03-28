package clowdtech.mpositive.areas.reporting.transaction.presenters;

import clowdtech.mpositive.areas.reporting.transaction.views.IReceiptView;
import clowdtech.mpositive.ui.Presenter;

public interface IReceiptPresenter extends Presenter<IReceiptView> {
    void setData(long receiptId);

    void refundReceipt();

    void printReceipt();
}
