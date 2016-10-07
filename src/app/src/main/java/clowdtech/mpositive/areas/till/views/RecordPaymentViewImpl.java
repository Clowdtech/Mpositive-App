package clowdtech.mpositive.areas.till.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.clowdtech.data.entities.PaymentTypes;

import java.math.BigDecimal;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import clowdtech.mpositive.App;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.shared.views.NumericEntryView;
import clowdtech.mpositive.areas.till.presenters.RecordPaymentPresenter;
import clowdtech.mpositive.data.transactions.entities.ReceiptPayment;
import clowdtech.mpositive.lifecycle.PresentedView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class RecordPaymentViewImpl extends LinearLayout implements RecordPaymentView, PresentedView, Presentable {
    @Inject
    protected RecordPaymentPresenter presenter;

    @Bind(R.id.tender_card_amount)
    protected TextView _cardAmount;

    @Bind(R.id.numeric_entry)
    protected NumericEntryView numericEntry;

    @Bind(R.id.payment_method)
    protected RadioGroup paymentMethod;

    public RecordPaymentViewImpl(Context context, AttributeSet attrs) {
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

        paymentMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tender_card_entry:
                        presenter.payByCard();
                        break;
                    case R.id.tender_cash_entry:
                        presenter.payByCash();
                        break;
                    case R.id.tender_other_entry:
                        presenter.payByOther();
                        break;
                }
            }
        });

        if (isInEditMode()) {
            return;
        }

//        this.presenter.bindView(this);
    }

    @Override
    public void showEditCard() {
        _cardAmount.setVisibility(View.VISIBLE);

        hideNumericEntry();
    }

    @Override
    public void showEditCash() {
        _cardAmount.setVisibility(View.GONE);

        showNumericEntry();
    }

    @Override
    public void showEditOther() {
        _cardAmount.setVisibility(View.GONE);

        showNumericEntry();
    }

    @Override
    public void setInitialValue(BigDecimal defaultValue) {
        numericEntry.setInitialValue(defaultValue);
    }

    private void hideNumericEntry() {
        numericEntry.setVisibility(View.GONE);
    }

    private void showNumericEntry() {
        numericEntry.setVisibility(View.VISIBLE);
    }

    public ReceiptPayment getPayment() {
        PaymentTypes type;

        switch (paymentMethod.getCheckedRadioButtonId()) {
            case R.id.tender_card_entry:
                type = PaymentTypes.Card;
                return new ReceiptPayment(type);
            case R.id.tender_cash_entry:
                type = PaymentTypes.Cash;
                break;
            case R.id.tender_other_entry:
                type = PaymentTypes.Other;
                break;
            default:
                return null;
        }

        return new ReceiptPayment(type, numericEntry.getValue());
    }

    public void setDefaultValue(BigDecimal defaultValue) {
        this.presenter.setDefaultValue(defaultValue);
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
