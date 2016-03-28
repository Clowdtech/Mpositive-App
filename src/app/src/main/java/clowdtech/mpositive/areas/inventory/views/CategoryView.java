package clowdtech.mpositive.areas.inventory.views;

import com.clowdtech.data.entities.IProduct;

import java.util.List;

import clowdtech.mpositive.areas.inventory.Container;

public interface CategoryView {
    void setName(String name);

    void setProducts(List<IProduct> products);

    void setTileBackground(int defaultBackground);

    void setButtonBackgroundColour(int defaultBackground);

    void setTileTextColour(int defaultForeground);

    void setButtonForegroundColour(int defaultForeground);

    void refreshCategories(List<IProduct> products);

    void setTileText(String name);

    String getName();

    Container getContainer();

    int getTileBackgroundColor();

    int getTileForegroundColor();

    boolean getVisibleOnHomePage();

    void setVisibleOnHomePage(boolean visibleOnHomePage);

    void setAvailableProducts(List<IProduct> availableProducts);

    void showProductsPicker();

    void unbindViews();

    void bindViews();
}
