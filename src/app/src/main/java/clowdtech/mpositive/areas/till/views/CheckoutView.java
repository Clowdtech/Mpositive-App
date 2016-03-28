package clowdtech.mpositive.areas.till.views;

public interface CheckoutView {
    void setChargeText(String displayText);

    void setChargeEnabled(boolean enabled);

    boolean inventoryHandledBack();

    void resetEditableReceipt();

    void setOrderReference(String reference);

    void displayOrderReference();

    void unbindViews();

    void bindViews();
}
