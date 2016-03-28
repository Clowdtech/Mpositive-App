package clowdtech.mpositive.areas.reporting.transaction;

import android.content.Context;

public interface IShareController {
    void shareTransactionsReport(Context context, String period, String csvExport);
}
