package clowdtech.mpositive.areas.shared.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;

import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.shared.presenters.NumericEntryPadPresenter;

public class NumericEntryPadView extends LinearLayout {
    private NumericEntryPadPresenter presenter;

    private OnEntryChangedListener onEntryChangedListener;
    private OnEntryCommitListener onEntryCommitListener;

    private final TextView doubleZero;

    public NumericEntryPadView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray styleAttrs = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.NumericEntryPadView);

        styleAttrs.recycle();

        inflate(getContext(), R.layout.shared_numeric_entry_pad, this);

        setupNumPad(R.id.command_num_0, 0);
        setupNumPad(R.id.command_num_1, 1);
        setupNumPad(R.id.command_num_2, 2);
        setupNumPad(R.id.command_num_3, 3);
        setupNumPad(R.id.command_num_4, 4);
        setupNumPad(R.id.command_num_5, 5);
        setupNumPad(R.id.command_num_6, 6);
        setupNumPad(R.id.command_num_7, 7);
        setupNumPad(R.id.command_num_8, 8);
        setupNumPad(R.id.command_num_9, 9);

        findViewById(R.id.command_clear).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.clearEntry();
            }
        });

        doubleZero = (TextView) findViewById(R.id.command_num_00);

        doubleZero.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.doubleZeroEntry();
            }
        });

        if (isInEditMode()) {
            return;
        }

        presenter = new NumericEntryPadPresenter(this);
    }

    private void setupNumPad(int resId, final int value) {
        findViewById(resId).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.changeEntry(value);
            }
        });
    }

    public void setOnEntryChangedListener(OnEntryChangedListener listener) {
        this.onEntryChangedListener = listener;
    }

    public void onEntryChanged(BigDecimal text) {
        if (onEntryChangedListener != null) {
            onEntryChangedListener.onEntryChanged(text);
        }
    }

    public void setOnEntryCommitListener(OnEntryCommitListener listener) {
        this.onEntryCommitListener = listener;
    }

    public void resetValue() {
        presenter.clearEntry();
    }
}
