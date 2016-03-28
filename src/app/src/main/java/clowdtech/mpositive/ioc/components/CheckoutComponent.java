package clowdtech.mpositive.ioc.components;

import clowdtech.mpositive.areas.till.views.EditableReceiptViewImpl;
import clowdtech.mpositive.areas.till.views.PaymentCompleteDualView;
import clowdtech.mpositive.areas.till.views.PaymentCompleteSingleView;
import clowdtech.mpositive.areas.till.views.SaveOrderDialogImpl;
import clowdtech.mpositive.areas.till.views.SavedOrdersViewImpl;
import clowdtech.mpositive.ioc.modules.ActivityModule;
import clowdtech.mpositive.ioc.modules.CheckoutModule;
import clowdtech.mpositive.ioc.scopes.ActivityScope;
import clowdtech.mpositive.receipt.IOrderExporter;
import dagger.Component;

@Component(modules = {CheckoutModule.class, ActivityModule.class}, dependencies = {ApplicationComponent.class})
@ActivityScope
public interface CheckoutComponent {
    IOrderExporter orderExporter();

    void inject(EditableReceiptViewImpl editableReceiptView);

    void inject(PaymentCompleteSingleView paymentCompleteSingleView);

    void inject(PaymentCompleteDualView paymentCompleteDualView);

    void inject(SaveOrderDialogImpl saveOrderView);

    void inject(SavedOrdersViewImpl savedOrdersView);
}
