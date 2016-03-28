package clowdtech.mpositive.areas.till.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import javax.inject.Inject;

import clowdtech.mpositive.App;
import clowdtech.mpositive.areas.till.presenters.TransactionCompletePresenter;
import clowdtech.mpositive.lifecycle.PresentedView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class TransactionCompleteViewImpl extends LinearLayout implements TransactionCompleteView, PresentedView, Presentable {
    @Inject
    protected TransactionCompletePresenter presenter;

    public TransactionCompleteViewImpl(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) {
            return;
        }

        ((App) context.getApplicationContext()).getApplicationComponent().inject(this);
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
