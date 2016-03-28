package clowdtech.mpositive.areas.till.presenters;

import java.math.BigDecimal;

import javax.inject.Inject;

import clowdtech.mpositive.areas.till.views.RecordPaymentView;
import clowdtech.mpositive.ui.BasePresenter;

public class RecordPaymentPresenter extends BasePresenter<RecordPaymentView> {
    @Inject
    public RecordPaymentPresenter(){}

    public void payByCard() {
        this.view.showEditCard();
    }

    public void payByCash() {
        this.view.showEditCash();
    }

    public void payByOther() {
        this.view.showEditOther();
    }

    public void setDefaultValue(BigDecimal defaultValue) {
        this.view.setInitialValue(defaultValue);
    }
}
