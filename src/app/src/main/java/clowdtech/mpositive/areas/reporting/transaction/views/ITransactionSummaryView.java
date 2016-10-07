package clowdtech.mpositive.areas.reporting.transaction.views;

import android.content.Context;

import java.util.List;

import clowdtech.mpositive.areas.reporting.transaction.Container;
import clowdtech.mpositive.areas.reporting.transaction.viewModels.TransactionListItemViewModel;

public interface ITransactionSummaryView {
    void setPrintEnabled(boolean enabled);

    void setItems(List<TransactionListItemViewModel> transactions);

    void setHeading(String reportHeading);

    void setCardTotal(String total);

    void setCashTotal(String total);

    void setOtherTotal(String total);

    void setTransactionTotal(String total);

    void setSelectedTransaction(int position);

    void setAverageTransaction(String averageTransactionValue);

    void setShareEnabled(Boolean enabled);

    Context getContext();

    Container getContainer();
}
