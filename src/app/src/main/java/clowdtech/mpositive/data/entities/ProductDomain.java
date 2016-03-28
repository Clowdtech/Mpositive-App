package clowdtech.mpositive.data.entities;

import com.clowdtech.data.entities.IProduct;
import com.clowdtech.data.entities.IProductTile;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public class ProductDomain implements IProduct {
    @Override
    public Long getId() {
        return null;
    }

    @Override
    public int getRemoteId() {
        return 0;
    }

    @Override
    public void setRemoteId(int id) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public BigDecimal getPrice() {
        return null;
    }

    @Override
    public void setPrice(BigDecimal price) {

    }

    @Override
    public Boolean getDeleted() {
        return null;
    }

    @Override
    public void setDeleted(boolean deleted) {

    }

    @Override
    public double getVat() {
        return 0;
    }

    @Override
    public void setVat(double vat) {

    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void setDescription(String description) {

    }

    @Override
    public void setLastUpdatedDate(DateTime lastUpdate) {

    }

    @Override
    public IProductTile getTile() {
        return null;
    }

    @Override
    public void setTile(IProductTile tile) {

    }
}
