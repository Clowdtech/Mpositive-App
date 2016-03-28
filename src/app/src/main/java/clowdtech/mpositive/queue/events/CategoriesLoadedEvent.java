package clowdtech.mpositive.queue.events;

import com.clowdtech.data.entities.Category;

import java.util.List;

public class CategoriesLoadedEvent {
    public List<Category> getData() {
        return data;
    }

    private List<Category> data;

    public CategoriesLoadedEvent(List<Category> data) {
        this.data = data;
    }
}
