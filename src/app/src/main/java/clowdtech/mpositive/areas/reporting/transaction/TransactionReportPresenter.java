package clowdtech.mpositive.areas.reporting.transaction;

import javax.inject.Inject;

import clowdtech.mpositive.ui.BasePresenter;

public class TransactionReportPresenter extends BasePresenter<ITransactionReportView> {
    private String header;
    private Long lowerStamp;
    private Long upperStamp;

    @Inject
    public TransactionReportPresenter() {
    }

    @Override
    public void bindView(ITransactionReportView view) {
        super.bindView(view);

        this.view.bindViews();
    }

    @Override
    public void unbindView() {
        this.view.unbindViews();

        super.unbindView();
    }

    public void periodSelected(String header, Long lowerStamp, Long upperStamp) {
        this.header = header;
        this.lowerStamp = lowerStamp;
        this.upperStamp = upperStamp;

        this.view.hideHelp();
        this.view.showSummary(header, lowerStamp, upperStamp);
    }

    public void transactionSelected(long transactionId) {
        this.view.showReceipt(transactionId);
    }

    public boolean isBackHandled() {
        if (this.view.receiptInView()) {
            this.view.showSummary(header, lowerStamp, upperStamp);

            return true;
        }

        if (this.view.transactionsInView()) {
            this.view.showPeriods();

            return true;
        }

        return false;
    }
}
