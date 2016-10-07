package clowdtech.mpositive.areas.till.presenters;

import android.support.annotation.NonNull;

import com.clowdtech.data.entities.IOrderLineManual;
import com.clowdtech.data.entities.ITransactionLineManual;
import com.clowdtech.data.entities.ITransactionLineProduct;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.inject.Inject;

import clowdtech.mpositive.ISharedPreferences;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.preferences.PreferenceEntry;
import clowdtech.mpositive.areas.till.contracts.IManualLineSelection;
import clowdtech.mpositive.areas.till.contracts.IProductLineSelection;
import clowdtech.mpositive.areas.till.views.EditableReceiptView;
import clowdtech.mpositive.areas.till.views.IReceiptLineView;
import clowdtech.mpositive.areas.till.views.ManualLineEditableView;
import clowdtech.mpositive.areas.till.views.ProductLineEditableView;
import clowdtech.mpositive.data.RunningOrder;
import clowdtech.mpositive.queue.IEventBus;
import clowdtech.mpositive.queue.events.ManualEntryAddedToReceipt;
import clowdtech.mpositive.queue.events.ManualEntryRemovedFromReceipt;
import clowdtech.mpositive.queue.events.OrderClearEvent;
import clowdtech.mpositive.queue.events.OrderLoadedEvent;
import clowdtech.mpositive.queue.events.OrderProductAdded;
import clowdtech.mpositive.queue.events.OrderProductAmended;
import clowdtech.mpositive.queue.events.PreferenceChangedEvent;
import clowdtech.mpositive.queue.events.ProductRemovedFromReceipt;
import clowdtech.mpositive.queue.events.RemoveManualEntryFromReceiptEvent;
import clowdtech.mpositive.queue.events.RemoveProductFromReceiptEvent;
import clowdtech.mpositive.queue.events.RunningReceiptAddProductEvent;
import clowdtech.mpositive.queue.events.RunningReceiptClearedEvent;
import clowdtech.mpositive.receipt.IOrderExporter;
import clowdtech.mpositive.ui.BasePresenter;

public class EditableReceiptPresenter extends BasePresenter<EditableReceiptView> {
    private ArrayList<IReceiptLineView> lineModels;

    private ReceiptLineModelComparer comparator;

    private RunningOrder runningOrder;
    private IEventBus eventBus;
    private IOrderExporter orderExporter;
    private ISharedPreferences sharedPreferences;

    private Integer maxNameLength;

    @Inject
    public EditableReceiptPresenter(RunningOrder runningOrder, IEventBus eventBus, IOrderExporter orderExporter, ISharedPreferences sharedPreferences) {
        this.runningOrder = runningOrder;
        this.eventBus = eventBus;
        this.orderExporter = orderExporter;
        this.sharedPreferences = sharedPreferences;
        this.comparator = new ReceiptLineModelComparer();

        this.lineModels = new ArrayList<>();
    }

    @Override
    public void bindView(EditableReceiptView view) {
        super.bindView(view);

        this.view.bindViews();

        eventBus.register(this);

        this.maxNameLength = this.view.getContext().getResources().getInteger(R.integer.receipt_max_character);

        displayCurrentOrder();

        if (sharedPreferences.getPrinterIntegration()) {
            this.view.showPrintOrder();
        } else {
            this.view.hidePrintOrder();
        }
    }

    @Override
    public void unbindView() {
        this.view.unbindViews();

        super.unbindView();

        eventBus.unregister(this);
    }

    public void clearRunningReceipt() {
        eventBus.post(new OrderClearEvent());
    }

    @Subscribe
    public void subscribeReceiptCleared(RunningReceiptClearedEvent event) {
        this.lineModels.clear();

        setReceiptLines();
    }

    @Subscribe
    public void subscribeManualEntryAdded(ManualEntryAddedToReceipt event) {
        lineModels.add(getNewManualView(event.getLine()));

        setReceiptLines();
    }

    @Subscribe
    public void subscribeManualEntryRemoved(ManualEntryRemovedFromReceipt event) {
        for (int i = lineModels.size() - 1; i >= 0; i--) {
            IReceiptLineView line = lineModels.get(i);

            if (event.getLine().getCreatedDate().equals(line.getSort())) {
                lineModels.remove(i);

                break;
            }
        }

        setReceiptLines();
    }

    @Subscribe
    public void subscribeProductAdded(OrderProductAdded event) {
        boolean found = false;

        for (IReceiptLineView model : lineModels) {
            if (model.getIdentifier() != null && model.getIdentifier() == event.getLine().getProductId()) {
                found = true;

                break;
            }
        }

        if (!found) {
            lineModels.add(getNewProductView(event.getLine()));
        }

        setReceiptLines();
    }

    @Subscribe
    public void subscribeProductRemoved(ProductRemovedFromReceipt event) {
        for (int i = lineModels.size() - 1; i >= 0; i--) {
            IReceiptLineView model = lineModels.get(i);

            if (model.getIdentifier() != null && model.getIdentifier() == event.getLine().getProductId()) {
                lineModels.remove(i);

                break;
            }
        }

        if (event.getLine().getQuantity() > 0) {
            lineModels.add(getNewProductView(event.getLine()));
        }

        setReceiptLines();
    }

    @Subscribe // possibly overkill as there is no view where this is included alongside a product edit - provides clarity though
    public void subscribeProductAmended(OrderProductAmended event) {
        setReceiptLines();
    }

    @Subscribe
    public void subscribeOrderLoaded(OrderLoadedEvent event) {
        displayCurrentOrder();
    }

    @Subscribe
    public void subscribePreferenceChangedEvent(PreferenceChangedEvent event) {
        if (this.view == null) {
            return;
        }

        if (event.getKey() == PreferenceEntry.PrinterIntegration) {
            Boolean value = (Boolean) event.getValue();

            if (value) {
                this.view.showPrintOrder();
            } else {
                this.view.hidePrintOrder();
            }
        }
    }

    private void setReceiptLines() {
        if (this.view != null) {
            Collections.sort(lineModels, comparator);

            this.view.setReceiptLines(this.lineModels);
        }
    }

    @NonNull
    private ManualLineEditableView getNewManualView(IOrderLineManual line) {
        return new ManualLineEditableView(line, this.maxNameLength, new IManualLineSelection<ManualLineEditableView, ITransactionLineManual>() {
            @Override
            public void removeLine(ManualLineEditableView item, IOrderLineManual line) {
                eventBus.post(new RemoveManualEntryFromReceiptEvent(line));
            }
        });
    }

    @NonNull
    private ProductLineEditableView getNewProductView(ITransactionLineProduct line) {
        return new ProductLineEditableView(line, this.maxNameLength, new IProductLineSelection<ProductLineEditableView, ITransactionLineProduct>() {
            @Override
            public void incrementLine(ITransactionLineProduct line) {
                eventBus.post(new RunningReceiptAddProductEvent(line.getProductId(), line.getName(), line.getUnitPrice()));
            }

            @Override
            public void decrementLine(ProductLineEditableView item, ITransactionLineProduct line) {
                eventBus.post(new RemoveProductFromReceiptEvent(line.getProductId()));
            }
        });
    }

    public void reset() {
        lineModels.clear();

        setReceiptLines();
    }

    public void printRunningReceipt() {
        orderExporter.printOrder(this.view.getContext());
    }

    public void requestSaveOrder() {
        this.view.showOrderSave();
    }

    private void displayCurrentOrder() {
        for (ITransactionLineProduct line : runningOrder.getProductLines()) {
            lineModels.add(getNewProductView(line));
        }

        for (IOrderLineManual line : runningOrder.getManualEntries()) {
            lineModels.add(getNewManualView(line));
        }

        Collections.sort(lineModels, comparator);

        this.view.setReceiptLines(this.lineModels);
    }

    private static class ReceiptLineModelComparer implements Comparator<IReceiptLineView> {
        @Override
        public int compare(IReceiptLineView lhs, IReceiptLineView rhs) {
            return lhs.getSort().isBefore(rhs.getSort()) ? -1
                    : lhs.getSort().isAfter(rhs.getSort()) ? 1
                    : 0;
        }
    }
}

