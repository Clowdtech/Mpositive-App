package clowdtech.mpositive.queue.events;

import com.clowdtech.data.entities.IProduct;

import java.util.List;

public class ProductsLoadedEvent {
    public List<IProduct> getData() {
        return data;
    }

    private List<IProduct> data;

    public ProductsLoadedEvent(List<IProduct> data) {
        this.data = data;
    }
}
