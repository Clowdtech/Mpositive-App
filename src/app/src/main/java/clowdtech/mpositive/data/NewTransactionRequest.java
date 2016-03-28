package clowdtech.mpositive.data;

import com.clowdtech.data.entities.ITransactionLineManual;
import com.clowdtech.data.entities.ITransactionLineProduct;

import clowdtech.mpositive.data.transactions.contracts.INewTransactionRequest;
import clowdtech.mpositive.data.transactions.entities.ReceiptPayment;

import java.util.List;

public class NewTransactionRequest implements INewTransactionRequest {
    private final List<ITransactionLineManual> _manualEntries;
    private final ReceiptPayment _payment;
    private final List<ITransactionLineProduct> _products;

    public NewTransactionRequest(ReceiptPayment receiptPayment, List<ITransactionLineProduct> products, List<ITransactionLineManual> manualEntries) {
        _payment = receiptPayment;
        _products = products;
        _manualEntries = manualEntries;
    }

    @Override
    public List<ITransactionLineProduct> getProductLines() {
        return _products;
    }

    @Override
    public List<ITransactionLineManual> getManualLines() {
        return _manualEntries;
    }

    @Override
    public ReceiptPayment getPayment() {
        return _payment;
    }
}
