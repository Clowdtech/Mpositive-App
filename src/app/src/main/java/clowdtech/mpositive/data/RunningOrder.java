package clowdtech.mpositive.data;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import com.clowdtech.data.entities.IOrder;
import com.clowdtech.data.entities.IOrderLineManual;
import com.clowdtech.data.entities.IOrderLineProduct;
import com.clowdtech.data.entities.IProduct;
import com.clowdtech.data.entities.ITransactionLineProduct;
import com.clowdtech.data.repository.IOrderRepository;
import com.clowdtech.data.repository.IProductRepository;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import clowdtech.mpositive.areas.till.contracts.IReceiptLineProduct;
import clowdtech.mpositive.data.lines.EntryLineManual;
import clowdtech.mpositive.data.lines.EntryLineProduct;
import clowdtech.mpositive.queue.events.OrderLoadEvent;
import clowdtech.mpositive.queue.events.OrderLoadedEvent;
import clowdtech.mpositive.queue.events.OrderSaveEvent;
import clowdtech.mpositive.queue.events.ProductSavedEvent;
import clowdtech.mpositive.queue.events.OrderProductAmended;
import clowdtech.mpositive.queue.events.RunningReceiptAddManualEntryEvent;
import clowdtech.mpositive.queue.events.RunningReceiptAddProductEvent;
import clowdtech.mpositive.queue.events.OrderClearEvent;
import clowdtech.mpositive.queue.IEventBus;
import clowdtech.mpositive.queue.events.ManualEntryAddedToReceipt;
import clowdtech.mpositive.queue.events.ManualEntryRemovedFromReceipt;
import clowdtech.mpositive.queue.events.OrderProductAdded;
import clowdtech.mpositive.queue.events.ProductRemovedFromReceipt;
import clowdtech.mpositive.queue.events.RunningReceiptClearedEvent;
import clowdtech.mpositive.queue.events.OrderValueChangeEvent;
import clowdtech.mpositive.queue.events.RemoveManualEntryFromReceiptEvent;
import clowdtech.mpositive.queue.events.RemoveProductFromReceiptEvent;
import clowdtech.mpositive.queue.events.TillReceiptCompleteEvent;

@Singleton
public class RunningOrder {

    private BigDecimal runningTotal;

    private ArrayMap<Long, IReceiptLineProduct> productRoll;
    private List<IOrderLineManual> manualRoll;

    private IEventBus eventBus;
    private InventoryStore inventoryStore;
    private IOrderRepository orderRepository;
    private IProductRepository productRepository;

    private Long orderId;

    @Inject // this is killing SRP
    public RunningOrder(IEventBus eventBus, InventoryStore inventoryStore, IOrderRepository orderRepository, IProductRepository productRepository) {
        this.eventBus = eventBus;
        this.inventoryStore = inventoryStore;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;

        runningTotal = new BigDecimal("0.00");
        productRoll = new ArrayMap<>();
        manualRoll = new ArrayList<>();

        eventBus.register(this);
    }

    private void addProductToReceipt(long id, String name, BigDecimal price) {
        IReceiptLineProduct line;

        if (productRoll.containsKey(id)) {
            line = productRoll.get(id);
            line.incrementQuantity();
        } else {
            line = createProductLine(id, name, price, DateTime.now(), 1);

            productRoll.put(line.getProductId(), line);
        }

        increaseRunningTotal(line.getUnitPrice());

        eventBus.post(new OrderProductAdded(line));
    }

    private void addManualEntryToReceipt(String note, BigDecimal price) {
        IOrderLineManual line = createManualLine(note, price, DateTime.now());

        manualRoll.add(line);

        increaseRunningTotal(line.getPrice());

        eventBus.post(new ManualEntryAddedToReceipt(line));
    }

    private void removeManualEntryFromReceipt(IOrderLineManual line) {
        manualRoll.remove(line);

        reduceRunningTotal(line.getPrice());

        eventBus.post(new ManualEntryRemovedFromReceipt(line));
    }

    private void clearReceipt() {
        productRoll.clear();
        manualRoll.clear();

        eventBus.post(new RunningReceiptClearedEvent());

        setRunningTotal(new BigDecimal("0.00"));
    }

    public BigDecimal getReceiptTotal() {
        return runningTotal;
    }

    public List<ITransactionLineProduct> getProductLines() {
        return new ArrayList<ITransactionLineProduct>(productRoll.values());
    }

    public List<IOrderLineManual> getManualEntries() {
        return manualRoll;
    }

    private void removeProductFromReceipt(long id) {
        IReceiptLineProduct line = productRoll.get(id);

        line.decrementQuantity();

        if (line.getQuantity() == 0) {
            productRoll.remove(id);
        }

        reduceRunningTotal(line.getUnitPrice());

        eventBus.post(new ProductRemovedFromReceipt(line));
    }

    private void setRunningTotal(BigDecimal unitPrice) {
        runningTotal = unitPrice;

        eventBus.post(new OrderValueChangeEvent(runningTotal));
    }

    private void increaseRunningTotal(BigDecimal unitPrice) {
        runningTotal = runningTotal.add(unitPrice);

        eventBus.post(new OrderValueChangeEvent(runningTotal));
    }

    private void reduceRunningTotal(BigDecimal unitPrice) {
        runningTotal = runningTotal.subtract(unitPrice);

        eventBus.post(new OrderValueChangeEvent(runningTotal));
    }

    @Produce
    public OrderValueChangeEvent produceReceiptValue() {
        return new OrderValueChangeEvent(runningTotal);
    }

//    @Produce
//    public OrderLoadedEvent produceOrderLoadedEvent() {
//        return new OrderLoadedEvent()
//    }

    @Subscribe
    public void subscribeProductAdd(RunningReceiptAddProductEvent event) {
        addProductToReceipt(event.getItemId(), event.getTitle(), event.getPrice());
    }

    @Subscribe
    public void subscribeProductRemove(RemoveProductFromReceiptEvent event) {
        removeProductFromReceipt(event.getProductId());
    }

    @Subscribe
    public void subscribeManualEntryAdd(RunningReceiptAddManualEntryEvent event) {
        addManualEntryToReceipt(event.getNote(), event.getValue());
    }

    @Subscribe
    public void subscribeManualEntryRemove(RemoveManualEntryFromReceiptEvent event) {
        removeManualEntryFromReceipt(event.getLine());
    }

    @Subscribe
    public void subscribeClearReceipt(OrderClearEvent event) {
        clearReceipt();
    }

    @Subscribe
    public void subscribeReceiptComplete(TillReceiptCompleteEvent event) {
        clearReceipt();
    }

    @Subscribe
    public void subscribeProductSaved(ProductSavedEvent event) {
        IProduct product = inventoryStore.getProduct(event.getProductId()); // TODO: database call !!

        if (!productRoll.containsKey(product.getId())) {
            return;
        }

        IReceiptLineProduct line = productRoll.get(product.getId());

        line.setName(product.getName());
        line.setUnitPrice(product.getPrice());

        recalculateRunningTotal();

        eventBus.post(new OrderProductAmended(line));
    }

    @Subscribe
    public void subscribeOrderSave(OrderSaveEvent event) {
        List<IOrderLineProduct> lines = new ArrayList<>();

        for (IOrderLineProduct line :
                productRoll.values()) {
            lines.add(line);
        }

        if (this.orderId != null) {
            orderRepository.updateOrder(this.orderId, event.getReference(), manualRoll, lines);
        } else {
            orderRepository.addOrder(event.getReference(), manualRoll, lines);
        }

        this.orderId = null;

        clearReceipt();
    }

    private void recalculateRunningTotal() {
        BigDecimal total = new BigDecimal("0.00");

        for (IReceiptLineProduct line :
                productRoll.values()) {
            total = total.add(line.getUnitPrice().multiply(BigDecimal.valueOf(line.getQuantity())));
        }

        for (IOrderLineManual line :
                manualRoll) {
            total = total.add(line.getPrice());
        }

        setRunningTotal(total);
    }

    public boolean hasCurrentOrder() {
        return runningTotal.compareTo(BigDecimal.ZERO) > 0;
    }

    // bad bad bad #hack
    @Subscribe
    public void subscribeOrderLoad(OrderLoadEvent event) {
        clearReceipt();

        // get order
        IOrder order = event.getOrder();

        // set current order, ready for save to come along and either update or create
        this.orderId = order.getOrderId();

        // populate products + manual items
        BigDecimal total = new BigDecimal("0.00");

        for (IOrderLineProduct orderLine:
             order.productLines()) {
            IProduct product = productRepository.getProduct(orderLine.getProductId());

            IReceiptLineProduct entryLineProduct = createProductLine(product.getId(), product.getName(), product.getPrice(), orderLine.getCreatedDate(), orderLine.getQuantity());

            productRoll.put(product.getId(), entryLineProduct);

            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(orderLine.getQuantity())));
        }

        for (IOrderLineManual manualOrderLine:
             order.manualLines()) {
            manualRoll.add(createManualLine(manualOrderLine.getName(), manualOrderLine.getPrice(), manualOrderLine.getCreatedDate()));

            total = total.add(manualOrderLine.getPrice());
        }

        // set total
        runningTotal = total; // price change fires

        // fire events
        eventBus.post(new OrderLoadedEvent(order));
    }

    @NonNull
    private IReceiptLineProduct createProductLine(long id, String name, BigDecimal price, DateTime added, int quantity) {
        StockUnit sku = new StockUnit(id, name, price);

        return new EntryLineProduct(sku, added, quantity);
    }

    @NonNull
    private IOrderLineManual createManualLine(String note, BigDecimal price, DateTime dateCreated) {
        return new EntryLineManual(note.equals("") ? "Manual Entry" : note, price, dateCreated);
    }
}