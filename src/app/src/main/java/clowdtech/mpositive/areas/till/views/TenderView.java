package clowdtech.mpositive.areas.till.views;

import java.math.BigDecimal;
import java.util.ArrayList;

import clowdtech.mpositive.data.transactions.entities.ReceiptPayment;

public interface TenderView {
    boolean isPaymentChoiceInview();

    void setPaymentValue(BigDecimal paymentDefault);

    ReceiptPayment getPaymentValue();

    void setReadOnlyReceiptItems(ArrayList<IReceiptLineView> receiptLineViewModels);

    void displayPaymentComplete(long receiptId);

    void setTotal(String total);

    void unbindViews();

    void bindViews();
}
