package clowdtech.mpositive.areas.inventory.viewModels;

import java.math.BigDecimal;

import clowdtech.mpositive.areas.shared.InventoryItem;

public class ProductViewModel implements InventoryItem {
    private Integer foreground;
    private Integer background;
    private String name;
    private long id;
    private BigDecimal price;

    public ProductViewModel(String name, int foreground, int background) {
        this.name = name;
        this.foreground = foreground;
        this.background = background;
    }

    public ProductViewModel(String name, Long id, BigDecimal price, int foreground, int background) {
        this(name, foreground, background);

        this.id = id;
        this.price = price;
    }

    public String getTitle() {
        return name;
    }

    @Override
    public String getSubTitle() {
        return "";
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public long getItemId() {
        return id;
    }

    public Integer getForeground() {
        return foreground;
    }

    public Integer getBackground() {
        return background;
    }
}
