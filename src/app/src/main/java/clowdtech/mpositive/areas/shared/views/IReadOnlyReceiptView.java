package clowdtech.mpositive.areas.shared.views;

import java.util.ArrayList;

import clowdtech.mpositive.areas.till.views.IReceiptLineView;

public interface IReadOnlyReceiptView {
    void setItems(ArrayList<IReceiptLineView> lines);
}
