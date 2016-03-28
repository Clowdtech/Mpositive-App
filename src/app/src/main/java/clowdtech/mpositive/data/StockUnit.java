package clowdtech.mpositive.data;

import com.clowdtech.data.stock.contracts.IStockItem;

import java.math.BigDecimal;

public class StockUnit implements IStockItem {
    public long id;
    public String name;
    public BigDecimal price;

    public StockUnit(long id, String name, BigDecimal cost) {
        this.id = id;
        this.name = name;
        this.price = cost;
    }

    @Override
    public String toString() {
        return name + " " + price;
    }

    public String getTitle() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public BigDecimal getUnitPrice() {
        return price;
    }

    @Override
    public void setUnitPrice(BigDecimal unitPrice) {
        this.price = unitPrice;
    }

    @Override
    public Long getId() {
        return this.id;
    }
}
