package com.clowdtech.data.dbflow.repository;

import com.clowdtech.data.dbflow.entities.Order;
import com.clowdtech.data.dbflow.entities.OrderLineManual;
import com.clowdtech.data.dbflow.entities.OrderLineProduct;
import com.clowdtech.data.dbflow.entities.Order_Table;
import com.clowdtech.data.entities.IOrder;
import com.clowdtech.data.entities.IOrderLineManual;
import com.clowdtech.data.entities.IOrderLineProduct;
import com.clowdtech.data.repository.IOrderRepository;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OrderRepository implements IOrderRepository {
    @Override
    public long addOrder(String reference, List<IOrderLineManual> manualEntries, Collection<IOrderLineProduct> products) {

        Order order = new Order();

        order.setReference(reference);
        order.setCreatedDate(DateTime.now());

        order.save();

        for (IOrderLineManual manualLine : manualEntries) {
            OrderLineManual mtl = new OrderLineManual();
            mtl.setName(manualLine.getName());
            mtl.setPrice(manualLine.getPrice());
            mtl.setCreatedDate(manualLine.getCreatedDate());
            mtl.associateOrder(order);
            mtl.save();
        }

        for (IOrderLineProduct productLine : products) {
            OrderLineProduct newPtl = new OrderLineProduct();
            newPtl.setQuantity(productLine.getQuantity());
            newPtl.setProductId(productLine.getProductId());
            newPtl.setCreatedDate(productLine.getCreatedDate());
            newPtl.associateOrder(order);
            newPtl.save();
        }

        return order.getId();
    }

    @Override
    public IOrder getOrder(long id) {
        return SQLite.select()
                .from(Order.class)
                .where(Order_Table.Id.eq(id))
                .querySingle();
    }

    @Override
    public List<IOrder> getOrders() {
        List<Order> orders = SQLite.select()
                .from(Order.class)
                .queryList();

        List<IOrder> returnOrders = new ArrayList<>();

        for (Order order :
                orders) {
            returnOrders.add(order);
        }

        return returnOrders;
    }

    @Override
    public void deleteOrder(long id) {
        SQLite.delete(Order.class)
                .where(Order_Table.Id.eq(id))
                .query();
    }

    @Override
    public void updateOrder(Long orderId, String reference, List<IOrderLineManual> manualRoll, List<IOrderLineProduct> lines) {

    }
}
