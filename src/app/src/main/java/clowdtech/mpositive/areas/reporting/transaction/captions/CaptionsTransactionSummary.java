package clowdtech.mpositive.areas.reporting.transaction.captions;

public interface CaptionsTransactionSummary {
    String getSingleCountCaption();
    String getMultipleCountCaption();
    String getHeader();

    String getTenderCash();

    String getTenderCard();

    String getTenderOther();
}
