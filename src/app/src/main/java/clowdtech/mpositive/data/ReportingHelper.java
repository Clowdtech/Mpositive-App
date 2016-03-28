package clowdtech.mpositive.data;

import com.clowdtech.data.entities.ITransactionLineManual;
import com.clowdtech.data.entities.ITransactionLineProduct;
import com.clowdtech.data.entities.ITransaction;
import com.clowdtech.data.entities.PaymentTypes;
import com.clowdtech.data.repository.ITransactionsRepository;

import java.util.ArrayList;
import java.util.List;

import clowdtech.mpositive.data.lines.EntryLineManual;
import clowdtech.mpositive.data.lines.EntryLineProduct;
import clowdtech.mpositive.data.transactions.entities.Receipt;
import clowdtech.mpositive.data.transactions.entities.ReceiptPayment;

public class ReportingHelper {

    private ITransactionsRepository transactionsRepository;

    public ReportingHelper(ITransactionsRepository transactionRepo) {
        this.transactionsRepository = transactionRepo;
    }

    public Receipt getReceipt(long id) {
        ITransaction storedTransaction = transactionsRepository.getTransaction(id);

        return buildReceipt(storedTransaction);
    }

    private Receipt buildReceipt(ITransaction transaction) {
        // TODO: can the lines be pushed through without translation???

        ArrayList<ITransactionLineProduct> productLines = new ArrayList<>();
        for (ITransactionLineProduct ptl : transaction.productLines()) {
            StockUnit sku = new StockUnit(ptl.getProductId(), ptl.getName(), ptl.getUnitPrice());
            EntryLineProduct pl = new EntryLineProduct(sku, ptl.getDateCreated(), ptl.getQuantity());
            productLines.add(pl);
        }

        ArrayList<ITransactionLineManual> manualLines = new ArrayList<>();
        for (ITransactionLineManual mtl : transaction.manualLines()) {
            EntryLineManual ml = new EntryLineManual(mtl.getName(), mtl.getPrice(), mtl.getDateCreated());
            manualLines.add(ml);
        }

        ReceiptPayment receiptPayment = new ReceiptPayment(
                PaymentTypes.values()[transaction.getPayment().getPaymentType().getValue()],
                transaction.getPayment().getAmount());

        return new Receipt(
                transaction.getId(),
                transaction.getCreatedDate(),
                receiptPayment,
                productLines,
                manualLines,
                transaction.getRefunded());
    }

    public void markAsRefunded(long transactionId) {
        ITransaction transaction = transactionsRepository.getTransaction(transactionId);

        if (transaction == null) {
            return;
        }

        transaction.setRefunded();

        transactionsRepository.refundTransaction(transaction.getId());
    }

    public List<ITransaction> getTransactionsBetweenDates_forProductReporting(long startTime, long endTime) {
        return transactionsRepository.getTransactionsBetween(startTime, endTime);
    }


    public Boolean transactionsOlderThan(long time) {
        return transactionsRepository.transactionsOlderThan(time);
    }
}