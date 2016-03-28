package clowdtech.mpositive.data;

import com.clowdtech.data.entities.Category;

import java.util.List;

public interface ICategoriesLoadHandler {
    void categoriesLoaded(List<Category> inventoryItems);
}
