package clowdtech.mpositive.areas.reporting.transaction.views;

import java.util.List;

import clowdtech.mpositive.areas.reporting.transaction.Container;
import clowdtech.mpositive.areas.reporting.transaction.viewModels.TransactionPeriodsViewModel;

public interface ITransactionPeriodsView {
    void setSelectedPeriod(int position);

    void setPeriods(List<TransactionPeriodsViewModel> periodViewModels);

    void addMorePeriods(List<TransactionPeriodsViewModel> data);

    void setNoMorePeriodsAvailable();

    void setLoading();

    void setLoaded();

    Container getContainer();
}
