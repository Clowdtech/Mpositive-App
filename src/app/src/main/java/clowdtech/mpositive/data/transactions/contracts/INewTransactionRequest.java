package clowdtech.mpositive.data.transactions.contracts;

import com.clowdtech.data.entities.ITransactionLineManual;
import com.clowdtech.data.entities.ITransactionLineProduct;

import clowdtech.mpositive.data.transactions.entities.ReceiptPayment;

import java.util.List;

public interface INewTransactionRequest {
    List<ITransactionLineProduct> getProductLines();

    List<ITransactionLineManual> getManualLines();

    ReceiptPayment getPayment();
}
