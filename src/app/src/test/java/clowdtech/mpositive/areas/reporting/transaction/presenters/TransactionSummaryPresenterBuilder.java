package clowdtech.mpositive.areas.reporting.transaction.presenters;

import android.content.Context;

import com.clowdtech.data.entities.ITransaction;
import com.clowdtech.data.entities.ITransactionNoSale;
import com.clowdtech.data.repository.ITransactionsRepository;

import org.joda.time.DateTime;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import clowdtech.mpositive.areas.reporting.transaction.Container;
import clowdtech.mpositive.areas.reporting.transaction.IShareController;
import clowdtech.mpositive.areas.reporting.transaction.captions.CaptionsTransactionSummary;
import clowdtech.mpositive.areas.reporting.transaction.views.ITransactionSummaryView;
import clowdtech.mpositive.queue.IEventBus;
import clowdtech.printer.IPrintController;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doReturn;

public class TransactionSummaryPresenterBuilder {

    @Mock
    Context mockContext;

    @Mock
    Container mockContainer;

    @Mock
    ITransactionsRepository mockTransactionRepo;

    @Mock
    IPrintController mockPrintController;

    @Mock
    IShareController mockShareController;

    @Mock
    IEventBus mockEventBus;

    @Mock
    CaptionsTransactionSummary mockCaptions;

    @Mock
    ITransactionSummaryView mockView;

    private boolean withData;

    public TransactionSummaryPresenterBuilder() {
        MockitoAnnotations.initMocks(this);

        doReturn("").when(mockContext).getString(anyInt());

        doReturn("%s transaction").when(mockCaptions).getSingleCountCaption();
        doReturn("%s transactions").when(mockCaptions).getMultipleCountCaption();

        doReturn(mockContext).when(mockView).getContext();
        doReturn(mockContainer).when(mockView).getContainer();
    }

    public TransactionSummaryPresenter build() {
        TransactionSummaryPresenter presenter = new TransactionSummaryPresenter(mockCaptions, mockEventBus, mockTransactionRepo, mockPrintController, mockShareController);

        presenter.bindView(mockView);

        if (withData) {
            DateTime lower = new DateTime(2014, 10, 4, 14, 40);
            DateTime upper = new DateTime(2014, 11, 10, 10, 0);

            presenter.setData("test", lower.getMillis(), upper.getMillis());
        }

        return presenter;
    }

    public ITransactionsRepository verifyRepo(int times) {
        return Mockito.verify(mockTransactionRepo, Mockito.times(times));
    }

    public IPrintController verifyPrintController(int times) {
        return Mockito.verify(mockPrintController, Mockito.times(times));
    }

    public IShareController verifyShareController(int times) {
        return Mockito.verify(mockShareController, Mockito.times(times));
    }

    public Container verifyContainer(int times) {
        return Mockito.verify(mockContainer, Mockito.times(times));
    }

    public ITransactionSummaryView verifyView(int times) {
        return Mockito.verify(mockView, Mockito.times(times));
    }

    public TransactionSummaryPresenterBuilder withTransactions(ArrayList<ITransaction> receipts) {
        doReturn(receipts).when(mockTransactionRepo).getTransactionsBetween(anyLong(), anyLong());

        return this;
    }

    public TransactionSummaryPresenterBuilder withNoSales(List<ITransactionNoSale> noSales) {
        doReturn(noSales).when(mockTransactionRepo).getNoSalesBetween(anyLong(), anyLong());

        return this;
    }

//    public TransactionSummaryPresenterBuilder withCaption(int resourceId, String caption) {
//        when(mockContext.getString(resourceId)).thenReturn(caption);
//
//        return this;
//    }

    public TransactionSummaryPresenterBuilder withData() {
        this.withData = true;

        return this;
    }

    public TransactionSummaryPresenterBuilder withHeader(String expectedHeading) {
        doReturn(expectedHeading).when(mockCaptions).getHeader();

        return this;
    }

    public TransactionSummaryPresenterBuilder withCashCaption(String caption) {
        doReturn(caption).when(mockCaptions).getTenderCash();

        return this;
    }

    public TransactionSummaryPresenterBuilder withCardCaption(String caption) {
        doReturn(caption).when(mockCaptions).getTenderCard();

        return this;
    }

    public TransactionSummaryPresenterBuilder withOtherCaption(String caption) {
        doReturn(caption).when(mockCaptions).getTenderOther();

        return this;
    }
}