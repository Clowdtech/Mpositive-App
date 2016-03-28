package clowdtech.mpositive.areas.till;

public interface TillView {
    boolean isPaymentChoiceInView();

    boolean isCheckoutBackHandled();


    void displayCheckout();

    void displayPaymentChoice();

    void displayPaymentComplete(long receiptId);

    void displaySavedOrders();


    boolean isBackHandled();

    void clearReceipt();

    void bindViews();

    void unbindViews();
}
