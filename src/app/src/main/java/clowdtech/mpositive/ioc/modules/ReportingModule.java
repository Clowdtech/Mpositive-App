package clowdtech.mpositive.ioc.modules;

import android.content.Context;
import android.content.res.Resources;

import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.reporting.transaction.IShareController;
import clowdtech.mpositive.areas.reporting.transaction.ShareController;
import clowdtech.mpositive.areas.reporting.transaction.captions.CaptionsTransactionPeriods;
import clowdtech.mpositive.areas.reporting.transaction.captions.CaptionsTransactionPeriodsImpl;
import clowdtech.mpositive.areas.reporting.transaction.captions.CaptionsTransactionSummary;
import clowdtech.mpositive.areas.reporting.transaction.captions.CaptionsTransactionSummaryImpl;
import clowdtech.mpositive.ioc.qualifiers.QualifierApplication;
import dagger.Module;
import dagger.Provides;

@Module
public class ReportingModule {
    @Provides
    CaptionsTransactionSummary providesCaptionsTransactionSummary(@QualifierApplication Context context) {
        Resources resources = context.getResources();

        CaptionsTransactionSummaryImpl captions = new CaptionsTransactionSummaryImpl();

        captions.setSingleCountCaption(resources.getString(R.string.reporting_transactions_count_single));
        captions.setMultipleCountCaption(resources.getString(R.string.reporting_transactions_count_multiple));
        captions.setHeader(resources.getString(R.string.reporting_transactions_header));
        captions.setTenderCash(resources.getString(R.string.captions_tender_cash));
        captions.setTenderCard(resources.getString(R.string.captions_tender_card));
        captions.setTenderOther(resources.getString(R.string.captions_tender_other));

        return captions;
    }

    @Provides
    CaptionsTransactionPeriods providesCaptionsTransactionPeriods(@QualifierApplication Context context) {
        Resources resources = context.getResources();

        CaptionsTransactionPeriodsImpl captions = new CaptionsTransactionPeriodsImpl();

        captions.setCaptionRent(resources.getString(R.string.reporting_transactions_recent));

        return captions;
    }

    @Provides
    IShareController provideShareController() {
        return new ShareController();
    }
}