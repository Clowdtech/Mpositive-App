package clowdtech.mpositive.areas.reporting.transaction.presenters;

import com.clowdtech.data.entities.ITransactionLineManual;
import com.clowdtech.data.entities.ITransactionLineProduct;
import com.clowdtech.data.repository.ITransactionsRepository;
import com.squareup.otto.Subscribe;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import clowdtech.mpositive.ISharedPreferences;
import clowdtech.mpositive.TaskListener;
import clowdtech.mpositive.areas.reporting.transaction.views.IReceiptView;
import clowdtech.mpositive.areas.till.views.IReceiptLineView;
import clowdtech.mpositive.areas.till.views.ManualLineReadOnlyView;
import clowdtech.mpositive.areas.till.views.ProductLineReadOnlyView;
import clowdtech.mpositive.data.ReportingHelper;
import clowdtech.mpositive.data.transactions.entities.Receipt;
import clowdtech.mpositive.queue.IEventBus;
import clowdtech.mpositive.queue.events.ReceiptRefundEvent;
import clowdtech.mpositive.receipt.IReceiptExporter;
import clowdtech.mpositive.receipt.ReceiptExporter;
import clowdtech.mpositive.ui.BasePresenter;

public class ReceiptPresenter extends BasePresenter<IReceiptView> implements IReceiptPresenter {
    private ReportingHelper reportingHelper;
    private IReceiptExporter receiptExporter;

    private final NumberFormat currencyFormatter;
    private long receiptId;

    private final ReceiptLineModelComparer comparator;

    private IEventBus eventBus;
    private ISharedPreferences preferences;

    @Inject
    public ReceiptPresenter(IEventBus eventBus, ITransactionsRepository transactionsRepository, ISharedPreferences preferences) {
        this.eventBus = eventBus;
        this.preferences = preferences;

        this.currencyFormatter = NumberFormat.getCurrencyInstance();

        this.comparator = new ReceiptLineModelComparer();
        this.reportingHelper = new ReportingHelper(transactionsRepository);
    }

    @Override
    public void bindView(IReceiptView view) {
        super.bindView(view);

        this.view.bindViews();

        eventBus.register(this);

        this.receiptExporter = new ReceiptExporter(this.view.getContext(), this.preferences);
    }

    @Override
    public void unbindView() {
        this.view.unbindViews();

        super.unbindView();

        eventBus.unregister(this);
    }

    @Override
    public void setData(long receiptId) {
        this.receiptId = receiptId;

        Receipt currentReceipt = reportingHelper.getReceipt(receiptId);

        ArrayList<IReceiptLineView> lineModels = new ArrayList<>();

        List<ITransactionLineProduct> productRoll = currentReceipt.getProductRoll();

        for (ITransactionLineProduct line : productRoll) {
            lineModels.add(new ProductLineReadOnlyView(line));
        }

        List<ITransactionLineManual> manualRoll = currentReceipt.getManualRoll();

        for (ITransactionLineManual line : manualRoll) {
            lineModels.add(new ManualLineReadOnlyView(line.getDateCreated(), line.getName(), line.getPrice()));
        }

        Collections.sort(lineModels, comparator);

        this.view.setLines(lineModels);

        String receiptTotal = currencyFormatter.format(currentReceipt.getTotal());

        this.view.setTotalHeading(receiptTotal);

        int lineCount = productRoll.size() + manualRoll.size();

        this.view.setLineCount(lineCount == 1 ? "1 item" : lineCount + " items");

        this.view.setTotalInfo(receiptTotal);

        String receiptPaid = currencyFormatter.format(currentReceipt.getPayment().getAmountPaid());

        this.view.setPaidInfo(receiptPaid);

        this.view.setPaidLabel(currentReceipt.getPayment().getType() + " transaction");

        String receiptChange = currencyFormatter.format(currentReceipt.getChange());

        this.view.setChangeInfo(receiptChange);

        if(currentReceipt.isRefunded()) {
            this.view.setRefunded();
        } else {
            this.view.resetRefunded();
        }
    }

    @Override
    public void refundReceipt() {
        this.reportingHelper.markAsRefunded(this.receiptId);

        eventBus.post(new ReceiptRefundEvent(this.receiptId));
    }

    @Subscribe
    public void subscribeReceiptRefund(ReceiptRefundEvent event) {
        this.view.setRefunded();
    }

    @Override
    public void printReceipt() {
        Receipt receipt = reportingHelper.getReceipt(this.receiptId);

        this.view.setPrinting();

        this.receiptExporter.printReceipt(receipt, new TaskListener() {
            @Override
            public void onFinished(boolean success) {
                view.setPrinted();
            }
        });
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
