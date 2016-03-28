package clowdtech.mpositive.sync.builder;

import com.clowdtech.data.entities.IProduct;
import com.clowdtech.data.entities.IProductTile;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public class DummyProduct implements IProduct {

    private int remoteId = 37;

    @Override
    public BigDecimal getPrice() {
        return new BigDecimal("12.59");
    }

    @Override
    public void setPrice(BigDecimal price) {
    }

    @Override
    public String getName() {
        return "dummy product";
    }

    @Override
    public Boolean getDeleted() {
        return false;
    }

    @Override
    public int getRemoteId() {
        return remoteId;
    }

    @Override
    public Long getId() {
        return (long) 58;
    }

    @Override
    public double getVat() {
        return 0;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public void setVat(double vat) {

    }

    @Override
    public void setDescription(String description) {

    }

    @Override
    public void setLastUpdatedDate(DateTime lastUpdate) {

    }

    @Override
    public void setRemoteId(int id) {
        remoteId = id;
    }

    @Override
    public void setDeleted(boolean deleted) {

    }

    @Override
    public void setTile(IProductTile tile) {

    }

    @Override
    public IProductTile getTile() {
        return null;
    }
}
