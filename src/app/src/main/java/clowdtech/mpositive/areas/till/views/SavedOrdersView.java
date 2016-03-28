package clowdtech.mpositive.areas.till.views;

import com.clowdtech.data.entities.IOrder;

import java.util.List;

import clowdtech.mpositive.areas.till.Container;

public interface SavedOrdersView {
    void showOrders(List<IOrder> orders);

    Container getContainer();

    void confirmDiscardRunningOrder();

    void showNoOrdersIndicator();

    void hideNoOrdersIndicator();

    void hideOrders();
}
