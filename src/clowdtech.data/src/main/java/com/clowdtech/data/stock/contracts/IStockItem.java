package com.clowdtech.data.stock.contracts;

import java.math.BigDecimal;

public interface IStockItem {
    Long getId();
    String getTitle();
    void setName(String title);
    BigDecimal getUnitPrice();
    void setUnitPrice(BigDecimal unitPrice);
}

