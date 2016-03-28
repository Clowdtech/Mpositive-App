package clowdtech.mpositive.areas.till.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import clowdtech.mpositive.App;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.shared.views.NumericEntryPadView;
import clowdtech.mpositive.areas.shared.views.OnEntryChangedListener;
import clowdtech.mpositive.areas.till.presenters.CommandPadPresenter;
import clowdtech.mpositive.lifecycle.PresentedView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class CommandPadView extends LinearLayout implements PresentedView, Presentable {

    @Inject
    protected CommandPadPresenter presenter;

    @Bind(R.id.subTotal)
    protected TextView subTotal;

    @Bind(R.id.commands_sku_name)
    protected EditText skuName;

    @Bind(R.id.numeric_entry)
    protected NumericEntryPadView numericEntry;


    public CommandPadView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) {
            return;
        }

        ((App) context.getApplicationContext()).getApplicationComponent().inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.bind(this);

        numericEntry.setOnEntryChangedListener(new OnEntryChangedListener() {
            @Override
            public void onEntryChanged(BigDecimal entry) {
                presenter.entryValueChanged(entry);
            }
        });
    }

    public void setSubTotal(String text) {
        subTotal.setText(text);
    }

    public String getNote() {
        return skuName.getText().toString();
    }

    public void setNote(String text) {
        skuName.setText(text);
    }

    public void resetNumericEntry() {
        numericEntry.resetValue();
    }

    @OnClick(R.id.command_add)
    public void commitSubTotal() {
        presenter.commitSubTotal();
    }

    @Override
    public void bindView() {
        presenter.bindView(this);
    }

    @Override
    public Presenter getPresenter() {
        return this.presenter;
    }
}
