package clowdtech.mpositive.areas.shared.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;

import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.shared.presenters.NumericEntryPresenter;

public class NumericEntryView extends LinearLayout implements INumericEntryView {
    private NumericEntryPresenter presenter;

    private final TextView valueView;
    private final NumericEntryPadView numericEntry;

    private boolean keypadVisible;

    private TextWatcher textChanged;

    public NumericEntryView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialiseDefaults(attrs);

        inflate(getContext(), R.layout.shared_numeric_entry, this);

        valueView = (TextView) findViewById(R.id.numeric_value_view);

        valueView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                valueView.requestFocus();

                numericEntry.setVisibility(numericEntry.getVisibility() == GONE ? VISIBLE : GONE);
            }
        });

        valueView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(valueView.getWindowToken(), 0);
            }
        });

        numericEntry = (NumericEntryPadView) findViewById(R.id.numeric_entry_pad);

        numericEntry.setVisibility(keypadVisible ? VISIBLE : GONE);

        numericEntry.setOnEntryChangedListener(new OnEntryChangedListener() {
            @Override
            public void onEntryChanged(BigDecimal formattedValue) {
                presenter.valueEntryChanged(formattedValue);
            }
        });

        if (isInEditMode()) {
            return;
        }

        presenter = new NumericEntryPresenter(this);
    }

    private void initialiseDefaults(AttributeSet attrs) {
        TypedArray styleAttrs = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.NumericEntryPadView);

        keypadVisible = styleAttrs.getBoolean(
                R.styleable.NumericEntryPadView_keypadVisible, false);

        styleAttrs.recycle();
    }

    public BigDecimal getValue() {
        return presenter.getValue();
    }

    public void setValue(BigDecimal value) {
        presenter.setValue(value);
    }

    public void resetValue() {
        presenter.resetValue();
    }

    @Override
    public void setCurrentValueView(String formattedValue) {
        this.valueView.setText(formattedValue);

        if (textChanged != null) {
            textChanged.onTextChanged(formattedValue, 0, 0, 0); // complete hack needs a proper value changed
        }
    }

    @Override
    public void resetNumericValue() {
        this.numericEntry.resetValue();
    }

    @Override
    public void collapse() {
        numericEntry.setVisibility(GONE);
    }

    @Override
    public void addTextChangedListener(TextWatcher textWatcher) {
        this.textChanged = textWatcher;
    }

    @Override
    public void setInitialValue(BigDecimal value) {
        presenter.setValue(value);
    }
}
