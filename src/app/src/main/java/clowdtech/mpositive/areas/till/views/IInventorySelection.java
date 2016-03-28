package clowdtech.mpositive.areas.till.views;

import clowdtech.mpositive.areas.inventory.viewModels.ProductViewModel;
import clowdtech.mpositive.areas.till.viewModels.CategoryTileViewModel;

public interface IInventorySelection {
    void productSelected(ProductViewModel product);

    void categorySelected(CategoryTileViewModel category);
}
