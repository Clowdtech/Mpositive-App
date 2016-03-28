package clowdtech.mpositive.areas.till.presenters;

import com.clowdtech.data.entities.IOrderLineManual;
import com.clowdtech.data.entities.ITransactionLineManual;
import com.clowdtech.data.entities.ITransactionLineProduct;
import com.clowdtech.data.entities.PaymentTypes;
import com.clowdtech.data.repository.ITransactionsRepository;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import clowdtech.mpositive.areas.till.views.IReceiptLineView;
import clowdtech.mpositive.areas.till.views.ManualLineReadOnlyView;
import clowdtech.mpositive.areas.till.views.ProductLineReadOnlyView;
import clowdtech.mpositive.areas.till.views.TenderView;
import clowdtech.mpositive.data.RunningOrder;
import clowdtech.mpositive.data.lines.EntryLineManual;
import clowdtech.mpositive.data.transactions.entities.ReceiptPayment;
import clowdtech.mpositive.ui.BasePresenter;

public class TenderPresenter extends BasePresenter<TenderView> {
    private final RunningOrder runningOrder;
    private final NumberFormat currencyFormatter;

    private ArrayList<IReceiptLineView> lineModels;
    private ReceiptLineModelComparer comparator;
    private ITransactionsRepository transactionsRepo;

    @Inject
    public TenderPresenter(RunningOrder runningOrder, ITransactionsRepository transactionsRepo) {
        this.runningOrder = runningOrder;
        this.transactionsRepo = transactionsRepo;

        this.lineModels = new ArrayList<>();
        this.comparator = new ReceiptLineModelComparer();
        this.currencyFormatter = NumberFormat.getCurrencyInstance();
    }

    @Override
    public void bindView(TenderView view) {
        super.bindView(view);

        this.view.bindViews();

        for (ITransactionLineProduct line : runningOrder.getProductLines()) {
            lineModels.add(new ProductLineReadOnlyView(line));
        }

        for (IOrderLineManual line : runningOrder.getManualEntries()) {
            lineModels.add(new ManualLineReadOnlyView(line.getCreatedDate(), line.getName(), line.getPrice()));
        }

        Collections.sort(lineModels, comparator);

        this.view.setReadOnlyReceiptItems(lineModels);

        BigDecimal receiptTotal = runningOrder.getReceiptTotal();

        this.view.setPaymentValue(receiptTotal);
        this.view.setTotal(currencyFormatter.format(receiptTotal));
    }

    @Override
    public void unbindView() {
        view.unbindViews();

        super.unbindView();
    }

    public boolean isBackHandled() {
        return false;
    }

    public void completePayment() {
        ReceiptPayment receiptPayment = this.view.getPaymentValue();

        if (receiptPayment == null) {
            return;
        }

        BigDecimal receiptTotal = runningOrder.getReceiptTotal();

        if (receiptPayment.getType() == PaymentTypes.Card) {
            receiptPayment.setAmountPaid(receiptTotal);
        } else if (receiptPayment.getAmountPaid() == null || receiptPayment.getAmountPaid().compareTo(receiptTotal) < 0) {
            return;
        }

        // move this to an insert command
        List<ITransactionLineManual> manualLines = new ArrayList<>();

        for (IOrderLineManual line :
                runningOrder.getManualEntries()) {
            manualLines.add(new EntryLineManual(line.getName(), line.getPrice(), line.getCreatedDate())); // hack: one object 2 interfaces
        }

        long receiptId = transactionsRepo.addTransaction(
                manualLines,
                runningOrder.getProductLines(),
                receiptPayment.getAmountPaid(),
                receiptPayment.getType());

        this.view.displayPaymentComplete(receiptId);
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
