package clowdtech.mpositive.areas.till.presenters;

import android.os.AsyncTask;

import com.clowdtech.data.entities.IOrderLineManual;
import com.clowdtech.data.entities.ITransactionLineProduct;
import com.clowdtech.data.repository.ITransactionsRepository;
import com.squareup.otto.Subscribe;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.inject.Inject;

import clowdtech.mpositive.ISharedPreferences;
import clowdtech.mpositive.TaskListener;
import clowdtech.mpositive.areas.preferences.PreferenceEntry;
import clowdtech.mpositive.areas.till.views.IPaymentCompleteView;
import clowdtech.mpositive.areas.till.views.IReceiptLineView;
import clowdtech.mpositive.areas.till.views.ManualLineReadOnlyView;
import clowdtech.mpositive.areas.till.views.ProductLineReadOnlyView;
import clowdtech.mpositive.data.ReportingHelper;
import clowdtech.mpositive.data.RunningOrder;
import clowdtech.mpositive.data.transactions.entities.Receipt;
import clowdtech.mpositive.queue.IEventBus;
import clowdtech.mpositive.queue.events.PreferenceChangedEvent;
import clowdtech.mpositive.queue.events.TillReceiptCompleteEvent;
import clowdtech.mpositive.receipt.PopCashDrawerAsync;
import clowdtech.mpositive.receipt.ReceiptExporter;
import clowdtech.mpositive.ui.BasePresenter;

public class PaymentCompletePresenter extends BasePresenter<IPaymentCompleteView> {
    private final ReceiptLineModelComparer comparator;
    private final NumberFormat currencyFormatter;

    private long receiptId;

    private RunningOrder runningOrder;
    private ITransactionsRepository repo;
    private ISharedPreferences preferences;
    private IEventBus eventBus;

    @Inject
    public PaymentCompletePresenter(RunningOrder runningOrder, ITransactionsRepository repo, ISharedPreferences preferences, IEventBus eventBus) {
        this.runningOrder = runningOrder;
        this.repo = repo;
        this.preferences = preferences;
        this.eventBus = eventBus;

        this.comparator = new ReceiptLineModelComparer();

        this.currencyFormatter = NumberFormat.getCurrencyInstance();
    }

    @Override
    public void bindView(IPaymentCompleteView view) {
        super.bindView(view);

        this.view.bindViews();

        eventBus.register(this);

        if (preferences.getCashDrawerIntegrated()) {
            view.showPrintOption();
        } else {
            view.hidePrintOption();
        }
    }

    @Override
    public void unbindView() {
        view.unbindViews();

        super.unbindView();

        eventBus.unregister(this);
    }

    @Subscribe
    public void subscribePreferenceChangedEvent(PreferenceChangedEvent event) {
        if (this.view == null) {
            return;
        }

        if (event.getKey() == PreferenceEntry.PrinterIntegration) {
            Boolean value = (Boolean) event.getValue();

            if (value) {
                this.view.showPrintOption();
            } else {
                this.view.hidePrintOption();
            }
        }
    }

    public void setItem(long receiptId) {
        this.receiptId = receiptId;

        BigDecimal total = new BigDecimal("0.00");

        ArrayList<IReceiptLineView> lineModels = new ArrayList<>();

        for (ITransactionLineProduct line : runningOrder.getProductLines()) {
            lineModels.add(new ProductLineReadOnlyView(line));

            total = total.add(line.getUnitPrice().multiply(new BigDecimal(line.getQuantity())));
        }

        for (IOrderLineManual line : runningOrder.getManualEntries()) {
            lineModels.add(new ManualLineReadOnlyView(line.getCreatedDate(), line.getName(), line.getPrice()));

            total = total.add(line.getPrice());
        }

        Collections.sort(lineModels, comparator);

        this.view.setReadOnlyReceiptItems(lineModels);
        this.view.setPaymentValue(this.currencyFormatter.format(total));

        if (preferences.getPrinterIntegration()) {
            AsyncTask<Void, Void, String> popper = new PopCashDrawerAsync(this.view.getContext());

            popper.execute();
        }
    }

    public boolean isBackHandled() {
        return false;
    }

    public void noReceipt() {
        eventBus.post(new TillReceiptCompleteEvent());

        this.view.completeCheckout();
    }

    public void printReceipt() {
        final ReceiptExporter receiptExporter = new ReceiptExporter(this.view.getContext(), preferences);
        final ReportingHelper receiptGenerator = new ReportingHelper(repo);

        Receipt receipt = receiptGenerator.getReceipt(this.receiptId);

        receiptExporter.printReceipt(receipt, new TaskListener() {
            @Override
            public void onFinished(boolean success) {
                if (success) {
                    eventBus.post(new TillReceiptCompleteEvent());

                    view.completeCheckout();
                }
            }
        });
    }

    // TODO: duplicate
    private static class ReceiptLineModelComparer implements Comparator<IReceiptLineView> {
        @Override
        public int compare(IReceiptLineView lhs, IReceiptLineView rhs) {
            return lhs.getSort().isBefore(rhs.getSort()) ? -1
                    : lhs.getSort().isAfter(rhs.getSort()) ? 1
                    : 0;
        }
    }
}
