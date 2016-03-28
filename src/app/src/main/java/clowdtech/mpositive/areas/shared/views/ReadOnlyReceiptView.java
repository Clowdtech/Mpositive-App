package clowdtech.mpositive.areas.shared.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import javax.inject.Inject;

import clowdtech.mpositive.App;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.shared.adapters.ReceiptAdapter;
import clowdtech.mpositive.areas.shared.presenters.ReadOnlyReceiptPresenter;
import clowdtech.mpositive.areas.till.views.IReceiptLineView;
import clowdtech.mpositive.lifecycle.PresentedView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class ReadOnlyReceiptView extends LinearLayout implements IReadOnlyReceiptView, PresentedView, Presentable {
    @Inject
    protected ReadOnlyReceiptPresenter presenter;

    private ListView listView;

    public ReadOnlyReceiptView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) {
            return;
        }

        ((App) context.getApplicationContext()).getApplicationComponent().inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        listView = (ListView) findViewById(R.id.checkout_lines);
    }

    @Override
    public void setItems(ArrayList<IReceiptLineView> lines) {
        ReceiptAdapter lineArrayAdapter = new ReceiptAdapter(getContext(), lines);

        listView.setAdapter(lineArrayAdapter);
    }

    public void setData(ArrayList<IReceiptLineView> receiptLineViewModels) {
        this.presenter.setData(receiptLineViewModels);
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
