package clowdtech.mpositive.areas.reporting.transaction;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import clowdtech.mpositive.App;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.reporting.transaction.views.ReceiptView;
import clowdtech.mpositive.areas.reporting.transaction.views.TransactionPeriodsView;
import clowdtech.mpositive.areas.reporting.transaction.views.TransactionSummaryView;
import clowdtech.mpositive.lifecycle.PresentedView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class SinglePaneContainer extends RelativeLayout implements Container, Presentable, ITransactionReportView {
    private TransactionPeriodsView periodsView;
    private TransactionSummaryView transactionsView;
    private ReceiptView receiptView;

    @Bind(R.id.view_container)
    protected ViewGroup viewContainer;

    @Inject
    protected TransactionReportPresenter presenter;

    public SinglePaneContainer(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) {
            return;
        }

        ((App) context.getApplicationContext()).getReportingComponent().inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.bind(this);

        this.periodsView = (TransactionPeriodsView) getContentView();
    }

    @Override
    public boolean isBackHandled() {
        return this.presenter.isBackHandled();
    }

    @Override
    public boolean isNavItemHandled(int position) {
        if (position == 2 && !periodsInView()) {
            showPeriods();
        }

        return false;
    }

    @Override
    public void bindView() {
        this.presenter.bindView(this);
    }

    @Override
    public void showHelp() {

    }

    @Override
    public void hideHelp() {

    }

    @Override
    public void showPeriods() {
        if (!periodsInView()) {
            injectView(R.layout.reporting_transaction_periods);
        }

        this.periodsView = (TransactionPeriodsView) getContentView();
    }

    @Override
    public void showSummary(String header, long lowerStamp, long upperStamp) {
        showSummary();

        this.transactionsView.setData(header, lowerStamp, upperStamp);
    }

    private void showSummary() {
        if (!transactionsInView()) {
            injectView(R.layout.reporting_transaction_summary);
        }

        this.transactionsView = (TransactionSummaryView) getContentView();
    }

    @Override
    public void showReceipt(long transactionId) {
        showReceipt();

        this.receiptView.setData(transactionId);
    }

    private void showReceipt() {
        if (!receiptInView()) {
            injectView(R.layout.shared_receipt);
        }

        this.receiptView = (ReceiptView) getContentView();
    }

    @Override
    public void bindViews() {
        ((PresentedView) getContentView()).bindView();
    }

    @Override
    public void unbindViews() {
        ((Presentable) getContentView()).getPresenter().unbindView();
    }

    private boolean periodsInView() {
        return this.periodsView != null && this.periodsView.getParent() != null;
    }

    @Override
    public boolean transactionsInView() {
        return this.transactionsView != null && this.transactionsView.getParent() != null;
    }

    @Override
    public boolean receiptInView() {
        return this.receiptView != null && this.receiptView.getParent() != null;
    }

    @Override
    public void transactionPeriodSelected(String header, Long lowerStamp, Long upperStamp) {
        this.presenter.periodSelected(header, lowerStamp, upperStamp);
    }

    @Override
    public void transactionSelected(long transactionId) {
        this.presenter.transactionSelected(transactionId);
    }

    public void injectView(int view) {
        removeContentView();

        inflate(getContext(), view, viewContainer);

        ((PresentedView) getContentView()).bindView();
    }

    private View getContentView() {
        return viewContainer.getChildAt(0);
    }

    private void removeContentView() {
        ((Presentable) getContentView()).getPresenter().unbindView();

        viewContainer.removeViewAt(0);
    }

    @Override
    public Presenter getPresenter() {
        return this.presenter;
    }
}
