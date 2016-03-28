package clowdtech.mpositive.areas.reporting.transaction.viewModels;

import org.joda.time.DateTime;

public class TransactionListItemViewModel {
    public DateTime getSort() {
        return sort;
    }

    private DateTime sort;
    private String title;
    private String info;
    private long _transactionId;
    private Boolean _refunded;

    // no longer a transaction, merely a view, refunded and id a little out of scope here
    public TransactionListItemViewModel(DateTime sort, String title, String info, long id, Boolean refunded) {
        this.sort = sort;
        this.title = title;
        this.info = info;
        _transactionId = id;
        _refunded = refunded;
    }

    public String getTitle() {
        return title;
    }

    public String getInfo() {
        return info;
    }

    public long getId() {
        return _transactionId;
    }

    public Boolean isRefunded() { return _refunded; }
}
