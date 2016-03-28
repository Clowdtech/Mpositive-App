package clowdtech.mpositive.areas.reporting.transaction.builder;

import com.clowdtech.data.entities.ITransactionLineManual;
import com.clowdtech.data.entities.ITransactionLineProduct;
import com.clowdtech.data.entities.ITransaction;
import com.clowdtech.data.entities.PaymentTypes;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import clowdtech.mpositive.data.transactions.entities.ReceiptPayment;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class TransactionBuilder {
    private ReceiptPayment payment;
    private List<ITransactionLineProduct> productLines;
    private List<ITransactionLineManual> manualLines;
    private DateTime createdDate;
    private boolean refunded;
    private long id;

    public TransactionBuilder() {
        this.payment = new ReceiptPayment(PaymentTypes.Cash, new BigDecimal(0.00));
        this.productLines = new ArrayList<>();
        this.manualLines = new ArrayList<>();
        this.createdDate = DateTime.now();
    }

    public ITransaction build() {
        ITransaction mockTransaction = mock(ITransaction.class);

        doReturn(this.id).when(mockTransaction).getId();

        doReturn(this.createdDate).when(mockTransaction).getCreatedDate();
        doReturn(this.payment).when(mockTransaction).getPayment();
        doReturn(this.refunded).when(mockTransaction).getRefunded();

        doReturn(this.productLines).when(mockTransaction).productLines();
        doReturn(this.manualLines).when(mockTransaction).manualLines();

        return mockTransaction;
    }

    public TransactionBuilder withPayment(PaymentTypes paymentType, double value) {
        // string conversions to keep exact numerical representations
        this.payment = new ReceiptPayment(paymentType, new BigDecimal(String.valueOf(value)));

        return this;
    }

    public TransactionBuilder withProduct(String name, double value, int count) {
        ITransactionLineProduct mockProductLine = mock(ITransactionLineProduct.class);

        doReturn((long)1).when(mockProductLine).getProductId();
        doReturn(count).when(mockProductLine).getQuantity();
        doReturn(name).when(mockProductLine).getName();
        doReturn(new BigDecimal(String.valueOf(value))).when(mockProductLine).getUnitPrice();

        this.productLines.add(mockProductLine);

        return this;
    }

    public TransactionBuilder withManualLine(double value) {
        ITransactionLineManual mockManualLine = mock(ITransactionLineManual.class);

        doReturn("mock line").when(mockManualLine).getName();
        doReturn(new BigDecimal(String.valueOf(value))).when(mockManualLine).getPrice();

        this.manualLines.add(mockManualLine);

        return this;
    }

    public TransactionBuilder withDate(DateTime dateTime) {
        this.createdDate = dateTime;

        return this;
    }

    public TransactionBuilder refunded() {
        this.refunded = true;

        return this;
    }

    public TransactionBuilder withId(long id) {
        this.id = id;

        return this;
    }
}
