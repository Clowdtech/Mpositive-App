package clowdtech.mpositive.areas.inventory.views;

import com.clowdtech.data.entities.Category;

import java.math.BigDecimal;
import java.util.List;

import clowdtech.mpositive.areas.inventory.Container;

public interface ProductView {
    void showActionItems();

    void setName(String name);

    void setPrice(BigDecimal price);

    void setVat(double vat);

    void setDescription(String description);

    void setCategories(List<Category> categories);

    void setTileBackground(int background);

    void setBackgroundButtonColour(int background);

    void setTileTextColour(int foreground);

    void setForegroundButtonColour(int foreground);

    void setVisibleOnHomePage(boolean visibleOnHomePage);

    void setVisibleInCategory(boolean visibleInCategory);

    void refreshCategories(List<Category> categories);

    void setTileTitle(String name);

    String getName();

    BigDecimal getPrice();

    double getVat();

    String getDescription();

    int getTileBackgroundColor();

    int getTileForegroundColor();

    boolean getVisibleOnHomePage();

    boolean getVisibleInCategory();

    Container getContainer();

    void setTilePrice(String formattedValue);

    void confirmDelete();

    void setAvailableProducts(List<Category> availableCategories);

    void showCategoriesPicker();

    void unbindViews();

    void bindViews();
}
