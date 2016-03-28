package clowdtech.mpositive.ioc.modules;

import clowdtech.mpositive.ISharedPreferences;
import clowdtech.printer.IPrintController;
import clowdtech.printer.StarIoPrintController;
import dagger.Module;
import dagger.Provides;

@Module
public class PrintingModule {
    @Provides
    IPrintController providesPrintController(ISharedPreferences preferences) {
        return new StarIoPrintController(preferences.getPrinterName(), preferences.getPrinterCommunicationType(), preferences.getRetry());
    }
}