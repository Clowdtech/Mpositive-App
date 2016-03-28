package clowdtech.mpositive.areas.inventory.views;

import com.clowdtech.data.entities.Category;

import java.util.List;

import clowdtech.mpositive.areas.inventory.Container;

public interface ICategoriesView {
    void setLoading();

    void setLoaded();

    void setCategories(List<Category> categories);

    Container getContainer();
}
