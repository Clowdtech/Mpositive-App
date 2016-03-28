package clowdtech.mpositive.areas.till.views;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import android.view.Window;

import butterknife.ButterKnife;
import butterknife.OnClick;
import clowdtech.mpositive.R;

public class OrderDiscardDialogImpl extends Dialog {

    private OnClickListener positiveClick;

    public OrderDiscardDialogImpl(Context context) {
        super(context);

        ViewGroup saveDialogView = (ViewGroup) getLayoutInflater().inflate(R.layout.till_order_load_with_existing, null);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(saveDialogView);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.save_discard)
    public void OnDiscard() {
        positiveClick.onClick(this, BUTTON_POSITIVE);

        dismiss();
    }

    @OnClick(R.id.save_cancel)
    public void OnCancel() {
        cancel();
    }

    public void setPositiveClick(OnClickListener positiveClick) {
        this.positiveClick = positiveClick;
    }
}
