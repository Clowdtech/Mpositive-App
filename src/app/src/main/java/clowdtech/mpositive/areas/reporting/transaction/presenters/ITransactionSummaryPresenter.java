package clowdtech.mpositive.areas.reporting.transaction.presenters;

import clowdtech.mpositive.areas.reporting.transaction.views.ITransactionSummaryView;
import clowdtech.mpositive.ui.Presenter;

public interface ITransactionSummaryPresenter extends Presenter<ITransactionSummaryView> {
    void transactionSelected(int position, long id);

    void printTransactions();

    void setData(String header, long lowerStamp, long upperStamp);

    void shareTransactions();
}
