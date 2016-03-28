package com.clowdtech.data.entities;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public interface IProduct {
    Long getId();

    int getRemoteId();

    void setRemoteId(int id);

    String getName();

    void setName(String name);

    BigDecimal getPrice();

    void setPrice(BigDecimal price);

    Boolean getDeleted();

    void setDeleted(boolean deleted);

    double getVat();

    void setVat(double vat);

    String getDescription();

    void setDescription(String description);

    void setLastUpdatedDate(DateTime lastUpdate);

    IProductTile getTile();

    void setTile(IProductTile tile);
}
