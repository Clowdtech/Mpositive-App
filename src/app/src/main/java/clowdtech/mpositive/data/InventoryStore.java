package clowdtech.mpositive.data;

import com.clowdtech.data.entities.IProduct;
import com.clowdtech.data.entities.Category;

import java.util.List;

public interface InventoryStore {
    void initialise();

    void refresh();


    IProduct getProduct();

    IProduct getProduct(long value);

    List<IProduct> getProductsFor(Long id);


    Category getCategory();

    Category getCategory(long id);

    List<Category> getCategoriesFor(long id);

    List<Category> getCategories();
}
