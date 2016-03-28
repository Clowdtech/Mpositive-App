package clowdtech.mpositive.areas.inventory.views;

import com.clowdtech.data.entities.IProduct;
import com.clowdtech.data.entities.Category;

import java.util.List;

import clowdtech.mpositive.areas.inventory.Container;

public interface InventoryView {
    Container getContainer();

    void setProducts(List<IProduct> data);

    void setCategories(List<Category> data);

    void showCategories();

    void showProducts();

    void bindViews();

    void unbindViews();
}
