package clowdtech.mpositive.areas.inventory.views;

import clowdtech.mpositive.areas.shared.InventoryItem;

public interface IProductSelection {
    void productSelected(InventoryItem product);

    void setLoading();

    void setLoaded();
}
