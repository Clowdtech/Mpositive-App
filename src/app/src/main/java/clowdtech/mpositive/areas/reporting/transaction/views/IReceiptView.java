package clowdtech.mpositive.areas.reporting.transaction.views;

import android.content.Context;

import java.util.ArrayList;

import clowdtech.mpositive.areas.till.views.IReceiptLineView;

public interface IReceiptView {
    void resetRefunded();

    void setRefunded();

    void setTotalHeading(String receiptTotal);

    void setLineCount(String lineCount);

    void setTotalInfo(String totalValue);

    void setPaidInfo(String receiptPaid);

    void setPaidLabel(String paidLabel);

    void setChangeInfo(String receiptChange);

    void setPrinting();

    void setPrinted();

    void setLines(ArrayList<IReceiptLineView> lineModels);

    Context getContext();

    void bindViews();

    void unbindViews();
}
