package clowdtech.mpositive.areas.reporting.transaction.captions;

public class CaptionsTransactionPeriodsImpl implements CaptionsTransactionPeriods {

    private String captionRent;

    @Override
    public String getRecent() {
        return captionRent;
    }

    public void setCaptionRent(String captionRent) {
        this.captionRent = captionRent;
    }
}
