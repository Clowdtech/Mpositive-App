package clowdtech.mpositive.areas.shared.models;

import org.joda.time.DateTime;

public class ReceiptLineViewModel {
    private DateTime added;
    private String title;
    private String subTitle;
    private int viewId;

    private int lineCount = 1;

    public ReceiptLineViewModel(DateTime added, String title, String subTitle, int viewId) {
        this.added = added;
        this.title = title;
        this.subTitle = subTitle;
        this.viewId = viewId;
    }

    public ReceiptLineViewModel(DateTime added, String title, int viewId) {
        this.added = added;
        this.title = title;
        this.viewId = viewId;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public int getLineCount() {
        return lineCount;
    }

    public DateTime getAdded() {
        return added;
    }

    public void incrementCount() {
        this.lineCount++;
    }

    public int getViewId() {
        return viewId;
    }
}

