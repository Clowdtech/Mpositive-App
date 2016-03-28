package clowdtech.mpositive.ioc.modules;

import clowdtech.mpositive.data.RunningOrder;
import clowdtech.mpositive.receipt.IOrderExporter;
import clowdtech.mpositive.receipt.OrderExporter;
import dagger.Module;
import dagger.Provides;

@Module
public class CheckoutModule {
    @Provides
    IOrderExporter providesOrderExporter(RunningOrder runningOrder) {
        return new OrderExporter(runningOrder);
    }
}