package clowdtech.mpositive.areas.reporting.transaction;

public interface Container {
    void transactionPeriodSelected(String heading, Long lowerStamp, Long upperStamp);

    void transactionSelected(long transactionId);

    boolean isBackHandled();

    boolean isNavItemHandled(int position);

//    void setHeading(String heading);

    void bindView();
}
