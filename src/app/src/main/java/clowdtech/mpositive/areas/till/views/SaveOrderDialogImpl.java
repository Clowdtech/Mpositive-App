package clowdtech.mpositive.areas.till.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import clowdtech.mpositive.App;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.till.presenters.OrderSaveDialogPresenter;
import clowdtech.mpositive.lifecycle.PresentedView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class SaveOrderDialogImpl extends Dialog implements SaveOrderView, Presentable, PresentedView {

    @Bind(R.id.order_save_reference)
    TextView referenceText;

    @Bind(R.id.order_save_message)
    TextView messageText;

    @Inject
    OrderSaveDialogPresenter presenter;

    public SaveOrderDialogImpl(Context context) {
        super(context);

        ((App)context.getApplicationContext()).getCheckoutComponent().inject(this);

        ViewGroup saveDialogView = (ViewGroup) getLayoutInflater().inflate(R.layout.till_save_order, null);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(saveDialogView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.save_ok)
    public void OnSave() {
        presenter.saveOrder();
    }

    @OnClick(R.id.save_cancel)
    public void OnCancel() {
        cancel();
    }

    @Override
    public String getReference() {
        return referenceText.getText().toString();
    }

    public void setMessageText(String message) {
        messageText.setText(message);
    }

    @Override
    public Presenter getPresenter() {
        return this.presenter;
    }

    @Override
    public void bindView() {
        this.presenter.bindView(this);
    }
}
