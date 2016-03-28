package clowdtech.mpositive.data;

import com.clowdtech.data.entities.IProduct;

import java.util.List;

public interface IProductsLoadHandler {
    void productsLoaded(List<IProduct> inventoryItems);
}
