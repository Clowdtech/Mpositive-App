package clowdtech.mpositive.areas.till.views;

import java.util.List;

import clowdtech.mpositive.areas.shared.InventoryItem;

public interface InventoryView {
    void setInventoryItems(List<InventoryItem> dataSource);

    void setLoaded();

    void refreshProducts();

    boolean onBackPressed();

    void setLoading();
}
