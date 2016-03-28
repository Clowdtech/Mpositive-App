package clowdtech.mpositive.areas.reporting.transaction;

public interface ITransactionReportView {
    void showHelp();

    void showPeriods();

    void hideHelp();

    void showSummary(String header, long lowerStamp, long upperStamp);

    void showReceipt(long transactionId);

    void bindViews();

    void unbindViews();

    boolean transactionsInView();

    boolean receiptInView();
}
