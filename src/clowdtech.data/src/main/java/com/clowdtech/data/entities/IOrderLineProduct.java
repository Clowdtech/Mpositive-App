package com.clowdtech.data.entities;

import org.joda.time.DateTime;

public interface IOrderLineProduct {
    DateTime getCreatedDate();

    void setCreatedDate(DateTime dateCreated);

    long getProductId();

    void setProductId(long productId);

    int getQuantity();

    void setQuantity(int quantity);
}
