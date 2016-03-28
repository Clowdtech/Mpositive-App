package clowdtech.mpositive.areas.reporting.transaction.presenters;

import com.clowdtech.data.entities.ITransactionLineManual;
import com.clowdtech.data.entities.ITransactionLineProduct;
import com.clowdtech.data.entities.ITransaction;
import com.clowdtech.data.entities.PaymentTypes;
import com.clowdtech.data.repository.ITransactionsRepository;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;

import clowdtech.mpositive.ISharedPreferences;
import clowdtech.mpositive.areas.reporting.transaction.Container;
import clowdtech.mpositive.areas.reporting.transaction.views.IReceiptView;
import clowdtech.mpositive.data.transactions.entities.ReceiptPayment;
import clowdtech.mpositive.queue.IEventBus;
import clowdtech.mpositive.receipt.IReceiptExporter;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReceiptPresenterBuilder {

    @Mock
    IReceiptView view;

    @Mock
    Container mockContainer;

    @Mock
    ITransactionsRepository mockTransactionRepo;

    @Mock
    IEventBus mockEventBus;

    @Mock
    ISharedPreferences mockSharedPreferences;

    @Mock
    IReceiptExporter mockReceiptExporter;

    public ReceiptPresenterBuilder() {
        MockitoAnnotations.initMocks(this);

        ITransaction transaction = mock(ITransaction.class);

        when(transaction.getPayment()).thenReturn(new ReceiptPayment(PaymentTypes.Cash, new BigDecimal("10.50")));
        when(transaction.productLines()).thenReturn(new ArrayList<ITransactionLineProduct>());
        when(transaction.manualLines()).thenReturn(new ArrayList<ITransactionLineManual>());

        when(mockTransactionRepo.getTransaction(anyLong())).thenReturn(transaction);
    }

    public ReceiptPresenter build() {
        ReceiptPresenter presenter = new ReceiptPresenter(mockEventBus, mockTransactionRepo, mockSharedPreferences);

        presenter.bindView(this.view);

        return presenter;
    }

    public ITransactionsRepository verifyRepo(int count) {
        return verify(mockTransactionRepo, times(count));
    }
}
