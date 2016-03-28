package clowdtech.mpositive.queue.events;

public class CategorySavedEvent {
    private long categoryId;

    public CategorySavedEvent(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getCategoryId() {
        return categoryId;
    }
}
