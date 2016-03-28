package clowdtech.mpositive.areas.reporting.transaction.viewModels;

import com.clowdtech.data.DateRange;
import com.clowdtech.data.DateTimeHelper;

import org.joda.time.DateTime;

public class TransactionPeriodsViewModel {
    private String title;
    private Long start;
    private Long end;

    public TransactionPeriodsViewModel(DateTime start, DateTime end) {
        if(DateTime.now().withTimeAtStartOfDay().equals(start)) {
            title = "Today";
        }
        else {
            title = DateTimeHelper.getFriendlyDateString(start);
        }

        this.start = start.toDate().getTime();
        this.end = end.toDate().getTime();
    }

    public TransactionPeriodsViewModel(String title) {
        this.title = title;

        // hack attack - lets just make it work fo the time being
    }

    public TransactionPeriodsViewModel(String title, DateRange lastTenRange) {
        this(title);

        this.start = lastTenRange.getLowerStamp();
        this.end = lastTenRange.getUpperStamp();
    }

    public String getPeriodTitle() {
        return title;
    }

    public Long getLowerStamp() { return start; }

    public Long getUpperStamp() { return end; }
}
