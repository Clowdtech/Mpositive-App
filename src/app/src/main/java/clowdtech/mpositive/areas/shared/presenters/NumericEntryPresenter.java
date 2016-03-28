package clowdtech.mpositive.areas.shared.presenters;

import java.math.BigDecimal;
import java.text.NumberFormat;

import clowdtech.mpositive.areas.shared.views.INumericEntryView;

public class NumericEntryPresenter {
    private final NumberFormat numberFormat;
    private INumericEntryView view;

    private BigDecimal currentValue;

    public NumericEntryPresenter(INumericEntryView view) {
        this.view = view;

        this.numberFormat = NumberFormat.getCurrencyInstance();

        this.currentValue = new BigDecimal("0.00");

        this.view.setCurrentValueView(numberFormat.format(currentValue));
    }

    public void valueEntryChanged(BigDecimal value) {
        this.currentValue = value;

        this.view.setCurrentValueView(numberFormat.format(value));
    }

    public BigDecimal getValue() {
        return currentValue;
    }

    public void setValue(BigDecimal value) {
        this.currentValue = value;

        this.view.setCurrentValueView(numberFormat.format(value));
    }

    public void resetValue() {
        this.view.setCurrentValueView(numberFormat.format(0.00));

        this.view.resetNumericValue();
    }
}
