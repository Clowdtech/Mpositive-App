package clowdtech.mpositive.areas.shared.views;

import android.text.TextWatcher;

import java.math.BigDecimal;

public interface INumericEntryView {
    void setCurrentValueView(String format);

    void resetNumericValue();

    void collapse();

    void addTextChangedListener(TextWatcher textWatcher);

    void setInitialValue(BigDecimal value);
}
