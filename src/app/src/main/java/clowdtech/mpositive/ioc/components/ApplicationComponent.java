package clowdtech.mpositive.ioc.components;

import android.content.Context;

import com.clowdtech.data.repository.IOrderRepository;
import com.clowdtech.data.repository.ITransactionsRepository;

import javax.inject.Singleton;

import clowdtech.mpositive.App;
import clowdtech.mpositive.ISharedPreferences;
import clowdtech.mpositive.ITracker;
import clowdtech.mpositive.activities.BaseActivity;
import clowdtech.mpositive.activities.SplashActivity;
import clowdtech.mpositive.areas.inventory.views.AreaSelectView;
import clowdtech.mpositive.areas.inventory.views.CategoriesView;
import clowdtech.mpositive.areas.inventory.views.CategorySelectView;
import clowdtech.mpositive.areas.inventory.views.CategoryViewImpl;
import clowdtech.mpositive.areas.inventory.views.InventoryDualView;
import clowdtech.mpositive.areas.inventory.views.ProductSelectView;
import clowdtech.mpositive.areas.inventory.views.ProductViewImpl;
import clowdtech.mpositive.areas.inventory.views.ProductsView;
import clowdtech.mpositive.areas.preferences.PreferencesFrag;
import clowdtech.mpositive.areas.shared.views.ReadOnlyReceiptView;
import clowdtech.mpositive.areas.till.DualPaneContainer;
import clowdtech.mpositive.areas.till.SinglePaneContainer;
import clowdtech.mpositive.areas.till.activities.TillActivity;
import clowdtech.mpositive.areas.till.views.CheckoutDualView;
import clowdtech.mpositive.areas.till.views.CheckoutSingleView;
import clowdtech.mpositive.areas.till.views.CommandPadView;
import clowdtech.mpositive.areas.till.views.InventoryViewImpl;
import clowdtech.mpositive.areas.till.views.RecordPaymentViewImpl;
import clowdtech.mpositive.areas.till.views.TenderViewDual;
import clowdtech.mpositive.areas.till.views.TransactionCompleteViewImpl;
import clowdtech.mpositive.data.InventoryStore;
import clowdtech.mpositive.data.RunningOrder;
import clowdtech.mpositive.ioc.modules.ApplicationModule;
import clowdtech.mpositive.ioc.modules.CheckoutModule;
import clowdtech.mpositive.ioc.modules.PrintingModule;
import clowdtech.mpositive.ioc.modules.RepositoryModule;
import clowdtech.mpositive.ioc.qualifiers.QualifierApplication;
import clowdtech.mpositive.queue.IEventBus;
import clowdtech.printer.IPrintController;
import dagger.Component;

@Component(modules = { ApplicationModule.class, RepositoryModule.class, CheckoutModule.class, PrintingModule.class })
@Singleton
public interface ApplicationComponent {
    RunningOrder runningReceipt();

    ITransactionsRepository transactionsRepository();

    IOrderRepository orderRepository();

    IEventBus eventBus();

    ITracker tracker();

    ISharedPreferences sharedPreferences();

    @QualifierApplication
    Context context();

    IPrintController printController();

    InventoryStore inventoryStore();

    void inject(App application);

    void inject(InventoryViewImpl view);

    void inject(CheckoutDualView view);
    void inject(CheckoutSingleView view);

    void inject(CommandPadView view);

    void inject(SinglePaneContainer container);

    void inject(SplashActivity splash);

    void inject(InventoryDualView inventoryDualView);

    void inject(ProductsView productsView);

    void inject(CategoriesView categoriesView);

    void inject(CategoryViewImpl categoryView);

    void inject(ProductViewImpl productView);

    void inject(AreaSelectView areaSelectView);

    void inject(CategorySelectView view);

    void inject(ProductSelectView view);

    void inject(RecordPaymentViewImpl view);

    void inject(ReadOnlyReceiptView view);

    void inject(TransactionCompleteViewImpl view);

    void inject(DualPaneContainer view);

    void inject(BaseActivity baseActivity);

    void inject(TenderViewDual tenderViewDual);

    void inject(PreferencesFrag preferencesFrag);

    void inject(TillActivity tillActivity);

    void inject(clowdtech.mpositive.areas.inventory.SinglePaneContainer singlePaneContainer);

    void inject(clowdtech.mpositive.areas.inventory.DualPaneContainer dualPaneContainer);
}
