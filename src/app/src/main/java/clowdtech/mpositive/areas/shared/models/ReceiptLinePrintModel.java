package clowdtech.mpositive.areas.shared.models;

import org.joda.time.DateTime;

public class ReceiptLinePrintModel {
    private DateTime added;
    private String title;
    private String subTitle;
    private int quantity = 1;

    public ReceiptLinePrintModel(DateTime added, String title, String subTitle, int quantity) {
        this.added = added;
        this.title = title;
        this.subTitle = subTitle;
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public int getLineCount() {
        return quantity;
    }

    public DateTime getAdded() {
        return added;
    }

    public void incrementCount() {
        this.quantity++;
    }
}
