package clowdtech.mpositive.areas.reporting.transaction;

import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import clowdtech.mpositive.App;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.reporting.transaction.views.ReceiptView;
import clowdtech.mpositive.areas.reporting.transaction.views.TransactionPeriodsView;
import clowdtech.mpositive.areas.reporting.transaction.views.TransactionSummaryView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class DualPaneContainer extends RelativeLayout implements Container, ITransactionReportView, Presentable {
    private TransactionPeriodsView periodsView;
    private TransactionSummaryView transactionsView;
    private ReceiptView receiptView;
    private TextView helpText;

    private Dialog receiptDialog;

    @Bind(R.id.view_container)
    protected ViewGroup viewContainer;

    @Inject
    protected TransactionReportPresenter presenter;

    public DualPaneContainer(Context context, AttributeSet attrs) {
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

        this.periodsView = (TransactionPeriodsView) viewContainer.getChildAt(0);
        this.transactionsView = (TransactionSummaryView) viewContainer.getChildAt(1);
        this.helpText = (TextView) viewContainer.getChildAt(2);

        this.receiptView = (ReceiptView) inflate(getContext(), R.layout.shared_receipt, null);

        receiptDialog = new Dialog(getContext());

        receiptDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        receiptDialog.setContentView(this.receiptView);
    }

    @Override
    public void transactionPeriodSelected(String header, Long lowerStamp, Long upperStamp) {
        this.presenter.periodSelected(header, lowerStamp, upperStamp);
    }

    @Override
    public void transactionSelected(long transactionId) {
        this.presenter.transactionSelected(transactionId);
    }

    @Override
    public boolean isBackHandled() {
        return false;
    }

    @Override
    public boolean isNavItemHandled(int position) {
        return false;
    }

    @Override
    public void bindView() {
        this.presenter.bindView(this);
    }

    @Override
    public void showHelp() {
        this.helpText.setVisibility(VISIBLE);
    }

    @Override
    public void showPeriods() {
        this.transactionsView.setVisibility(GONE);
    }

    @Override
    public void hideHelp() {
        this.helpText.setVisibility(GONE);
    }

    @Override
    public void showSummary(String header, long lowerStamp, long upperStamp) {
        this.transactionsView.setData(header, lowerStamp, upperStamp);
        this.transactionsView.setVisibility(VISIBLE);
    }

    @Override
    public void showReceipt(long transactionId) {
        this.receiptView.setData(transactionId);

        this.receiptDialog.show();
    }

    @Override
    public void bindViews() {
        periodsView.bindView();
        transactionsView.bindView();
        receiptView.bindView();
    }

    @Override
    public void unbindViews() {
        periodsView.getPresenter().unbindView();
        transactionsView.getPresenter().unbindView();
        receiptView.getPresenter().unbindView();
    }

    @Override
    public boolean transactionsInView() {
        return this.transactionsView != null && this.transactionsView.getParent() != null;
    }

    @Override
    public boolean receiptInView() {
        return this.receiptDialog != null && this.receiptDialog.isShowing();
    }

    @Override
    public Presenter getPresenter() {
        return this.presenter;
    }
}
