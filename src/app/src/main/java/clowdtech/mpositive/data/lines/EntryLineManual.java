package clowdtech.mpositive.data.lines;

import com.clowdtech.data.entities.IOrderLineManual;
import com.clowdtech.data.entities.ITransactionLineManual;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public class EntryLineManual implements IOrderLineManual, ITransactionLineManual {
    private DateTime dateCreated;
    private BigDecimal price;
    private String name;

    public EntryLineManual(String name, BigDecimal price) {
        this(name, price, DateTime.now());
    }

    public EntryLineManual(String name, BigDecimal price, DateTime dateCreated) {
        this.dateCreated = dateCreated;
        this.name = name;
        this.price = price;
    }

    @Override
    public DateTime getCreatedDate() {
        return dateCreated;
    }

    @Override
    public void setCreatedDate(DateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public void setPrice(BigDecimal manualPrice) {
        this.price = manualPrice;
    }

    @Override
    public DateTime getDateCreated() {
        return dateCreated;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
