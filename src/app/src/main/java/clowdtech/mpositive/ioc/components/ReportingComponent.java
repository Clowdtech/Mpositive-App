package clowdtech.mpositive.ioc.components;

import clowdtech.mpositive.areas.reporting.transaction.DualPaneContainer;
import clowdtech.mpositive.areas.reporting.transaction.IShareController;
import clowdtech.mpositive.areas.reporting.transaction.SinglePaneContainer;
import clowdtech.mpositive.areas.reporting.transaction.captions.CaptionsTransactionPeriods;
import clowdtech.mpositive.areas.reporting.transaction.captions.CaptionsTransactionSummary;
import clowdtech.mpositive.areas.reporting.transaction.views.ReceiptView;
import clowdtech.mpositive.areas.reporting.transaction.views.TransactionPeriodsView;
import clowdtech.mpositive.areas.reporting.transaction.views.TransactionSummaryView;
import clowdtech.mpositive.ioc.modules.PrintingModule;
import clowdtech.mpositive.ioc.modules.ReportingModule;
import clowdtech.mpositive.ioc.scopes.ActivityScope;
import dagger.Component;

@Component(modules = { ReportingModule.class }, dependencies = {ApplicationComponent.class})
@ActivityScope
public interface ReportingComponent {
    CaptionsTransactionSummary captionsTransactionSummary();

    CaptionsTransactionPeriods captionsTransactionPeriods();

    IShareController shareController();

    void inject(SinglePaneContainer singlePaneContainer);

    void inject(DualPaneContainer dualPaneContainer);

    void inject(ReceiptView receiptView);

    void inject(TransactionSummaryView transactionSummaryView);

    void inject(TransactionPeriodsView transactionPeriodsView);

    void inject(clowdtech.mpositive.areas.reporting.product.SinglePaneContainer singlePaneContainer);

    void inject(clowdtech.mpositive.areas.reporting.product.DualPaneContainer dualPaneContainer);
}
