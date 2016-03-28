package clowdtech.mpositive.areas.till;

public interface Container {
    boolean onBackPressed();

    void navigateToPaymentChoice();

    void navigateToPaymentComplete(long receiptId);

    void navigateToCheckout();

    void bindView();
}
