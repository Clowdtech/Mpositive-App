package clowdtech.mpositive.areas.till.views;

import android.content.Context;

import java.util.List;

public interface EditableReceiptView {
    void setReceiptLines(List<IReceiptLineView> lineModels);

    void showPrintOrder();

    void hidePrintOrder();

    Context getContext();

    void showOrderSave();

    void bindViews();

    void unbindViews();
}
