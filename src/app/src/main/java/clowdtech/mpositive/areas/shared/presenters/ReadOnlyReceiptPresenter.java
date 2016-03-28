package clowdtech.mpositive.areas.shared.presenters;

import java.util.ArrayList;

import javax.inject.Inject;

import clowdtech.mpositive.areas.shared.views.IReadOnlyReceiptView;
import clowdtech.mpositive.areas.till.views.IReceiptLineView;
import clowdtech.mpositive.ui.BasePresenter;

public class ReadOnlyReceiptPresenter extends BasePresenter<IReadOnlyReceiptView> {
    @Inject
    public ReadOnlyReceiptPresenter(){}

    public void setData(ArrayList<IReceiptLineView> receiptLineViewModels) {
        this.view.setItems(receiptLineViewModels);
    }
}
