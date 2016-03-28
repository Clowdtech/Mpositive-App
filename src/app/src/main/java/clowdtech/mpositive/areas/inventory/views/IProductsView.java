package clowdtech.mpositive.areas.inventory.views;

import com.clowdtech.data.entities.IProduct;

import java.util.List;

import clowdtech.mpositive.areas.inventory.Container;

public interface IProductsView {
    void setProducts(List<IProduct> products);

    Container getContainer();

    void setLoaded();

    void setLoading();
}
