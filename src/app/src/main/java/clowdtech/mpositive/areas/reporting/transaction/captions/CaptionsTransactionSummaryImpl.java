package clowdtech.mpositive.areas.reporting.transaction.captions;

public class CaptionsTransactionSummaryImpl implements CaptionsTransactionSummary {
    private String singleCountCaption;
    private String multipleCountCaption;
    private String header;
    private String tenderCash;
    private String tenderCard;
    private String tenderOther;

    @Override
    public String getSingleCountCaption() {
        return singleCountCaption;
    }

    @Override
    public String getMultipleCountCaption() {
        return multipleCountCaption;
    }

    @Override
    public String getHeader() {
        return header;
    }

    @Override
    public String getTenderCash() {
        return tenderCash;
    }

    @Override
    public String getTenderCard() {
        return tenderCard;
    }

    @Override
    public String getTenderOther() {
        return tenderOther;
    }

    public void setSingleCountCaption(String singleCountCaption) {
        this.singleCountCaption = singleCountCaption;
    }

    public void setMultipleCountCaption(String multipleCountCaption) {
        this.multipleCountCaption = multipleCountCaption;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setTenderCash(String tenderCash) {
        this.tenderCash = tenderCash;
    }

    public void setTenderCard(String tenderCard) {
        this.tenderCard = tenderCard;
    }

    public void setTenderOther(String tenderOther) {
        this.tenderOther = tenderOther;
    }
}
