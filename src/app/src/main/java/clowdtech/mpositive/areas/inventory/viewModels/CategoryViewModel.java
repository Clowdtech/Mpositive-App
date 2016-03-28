package clowdtech.mpositive.areas.inventory.viewModels;

import android.graphics.Color;

import com.clowdtech.data.entities.CategoryTile;

public class CategoryViewModel {
    private String name;
    private Integer foreground;
    private Integer background;
    private long id;

    public CategoryViewModel(String name, long id, CategoryTile tile) {
        this.name = name;
        this.id = id;

        if (tile != null) {
            if (tile.getForeground() != null)
            {
                this.foreground = Color.parseColor(tile.getForeground());
            }

            if (tile.getBackground() != null)
            {
                this.background = Color.parseColor(tile.getBackground());
            }
        }
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public Integer getForeground() {
        return foreground;
    }

    public Integer getBackground() {
        return background;
    }
}
