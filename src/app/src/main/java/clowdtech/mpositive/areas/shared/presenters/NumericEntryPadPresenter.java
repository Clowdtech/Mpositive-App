package clowdtech.mpositive.areas.shared.presenters;

import java.math.BigDecimal;

import clowdtech.mpositive.areas.shared.views.NumericEntryPadView;

public class NumericEntryPadPresenter {
    private BigDecimal currentValue;

    private NumericEntryPadView view;

    public NumericEntryPadPresenter(NumericEntryPadView view) {
        this.view = view;

        currentValue = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_UNNECESSARY);
    }

    public void doubleZeroEntry() {
        currentValue = currentValue.multiply(BigDecimal.valueOf(100));

        this.view.onEntryChanged(currentValue);
    }

    public void changeEntry(int value) { // could move the big decimals behind each button to cut down on creating new objects
        BigDecimal original = new BigDecimal(value).setScale(2, BigDecimal.ROUND_UNNECESSARY);

        BigDecimal addition = original.divide(new BigDecimal(100.00), BigDecimal.ROUND_UNNECESSARY);

        currentValue = currentValue.multiply(BigDecimal.TEN).add(addition);

        this.view.onEntryChanged(currentValue);
    }

    public void clearEntry() {
        currentValue = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_UNNECESSARY);

        this.view.onEntryChanged(new BigDecimal(0.00));
    }
}
