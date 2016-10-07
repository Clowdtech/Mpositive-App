package clowdtech.mpositive.areas.till.views;

import android.content.Context;

import java.util.ArrayList;

public interface IPaymentCompleteView {
    void setReadOnlyReceiptItems(ArrayList<IReceiptLineView> lineModels);

    void completeCheckout();

    Context getContext();

    void showPrintOption();

    void hidePrintOption();

    void setPaymentValue(String total);

    void unbindViews();

    void bindViews();
}
