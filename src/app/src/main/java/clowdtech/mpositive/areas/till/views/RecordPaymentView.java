package clowdtech.mpositive.areas.till.views;

import java.math.BigDecimal;

public interface RecordPaymentView {
    void showEditCard();

    void showEditCash();

    void showEditOther();

    void setInitialValue(BigDecimal defaultValue);
}
