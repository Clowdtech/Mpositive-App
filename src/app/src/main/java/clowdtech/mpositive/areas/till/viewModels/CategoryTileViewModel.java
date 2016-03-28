package clowdtech.mpositive.areas.till.viewModels;

import clowdtech.mpositive.areas.shared.InventoryItem;

public class CategoryTileViewModel implements InventoryItem {
    private final long id;
    private final String name;

    private Integer foreground;
    private Integer background;

    public CategoryTileViewModel(Long id, String name, int foreground, int background) {
        this.id = id;
        this.name = name;

        this.foreground = foreground;
        this.background = background;
    }

    @Override
    public long getItemId() {
        return id;
    }

    @Override
    public String getTitle() {
        return this.name;
    }

    @Override
    public String getSubTitle() {
        return "";
    }

    public Integer getForeground() {
        return foreground;
    }

    public Integer getBackground() {
        return background;
    }
}
