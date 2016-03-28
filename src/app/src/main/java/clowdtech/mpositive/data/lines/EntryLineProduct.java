package clowdtech.mpositive.data.lines;

import com.clowdtech.data.stock.contracts.IStockItem;

import org.joda.time.DateTime;

import java.math.BigDecimal;

import clowdtech.mpositive.areas.till.contracts.IReceiptLineProduct;

public class EntryLineProduct implements IReceiptLineProduct {

    private DateTime added;
    private IStockItem lineItem;
    private int quantity;

    public EntryLineProduct(IStockItem lineItem, DateTime added, int quantity) {
        this.added = added;
        this.lineItem = lineItem;
        this.quantity = quantity;
    }

    @Override
    public DateTime getCreatedDate() {
        return this.added;
    }

    @Override
    public void setCreatedDate(DateTime dateCreated) {

    }

    @Override
    public String getName() {
        return this.lineItem.getTitle();
    }

    @Override
    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public void setQuantity(int quantity) {

    }

    @Override
    public long getProductId() {
        return lineItem.getId();
    }

    @Override
    public DateTime getDateCreated() {
        return this.added;
    }

    @Override
    public void setProductId(long productId) {

    }

    @Override
    public BigDecimal getUnitPrice() {
        return this.lineItem.getUnitPrice(); // shouldn't be tied to an sku
    }

    @Override
    public void decrementQuantity() {
        this.quantity--;
    }

    @Override
    public void incrementQuantity() {
        this.quantity++;
    }

    @Override
    public void setName(String name) {
        this.lineItem.setName(name);
    }

    @Override
    public void setUnitPrice(BigDecimal unitPrice) {
        this.lineItem.setUnitPrice(unitPrice);
    }
}

