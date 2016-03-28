package com.clowdtech.data.repository;

import com.clowdtech.data.entities.IOrder;
import com.clowdtech.data.entities.IOrderLineManual;
import com.clowdtech.data.entities.IOrderLineProduct;

import java.util.Collection;
import java.util.List;

public interface IOrderRepository {
    long addOrder(String reference, List<IOrderLineManual> manualEntries, Collection<IOrderLineProduct> products);

    IOrder getOrder(long id);

    List<IOrder> getOrders();

    void deleteOrder(long id);

    void updateOrder(Long orderId, String reference, List<IOrderLineManual> manualRoll, List<IOrderLineProduct> lines);
}
