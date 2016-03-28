package clowdtech.mpositive.queue.events;

public class MenuItemVisibilityEvent {
    private boolean visibility;

    public MenuItemVisibilityEvent(boolean visibility) {
        this.visibility = visibility;
    }

    public boolean getVisibility() {
        return visibility;
    }
}
