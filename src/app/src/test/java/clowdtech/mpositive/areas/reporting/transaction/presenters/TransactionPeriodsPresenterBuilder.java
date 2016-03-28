package clowdtech.mpositive.areas.reporting.transaction.presenters;

import com.clowdtech.data.repository.ITransactionsRepository;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import clowdtech.mpositive.areas.reporting.transaction.Container;

import com.clowdtech.data.DateRange;

import clowdtech.mpositive.areas.reporting.transaction.captions.CaptionsTransactionPeriods;
import clowdtech.mpositive.areas.reporting.transaction.views.ITransactionPeriodsView;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doReturn;

public class TransactionPeriodsPresenterBuilder {
    @Mock
    ITransactionsRepository mockTransactionRepo;

    @Mock
    ITransactionPeriodsView mockView;

    @Mock
    CaptionsTransactionPeriods mockCaptions;

    @Mock
    Container mockContainer;

    public TransactionPeriodsPresenterBuilder() {
        MockitoAnnotations.initMocks(this);

        // basic setup
        doReturn(new DateRange(1000, 2000)).when(mockTransactionRepo).getLastTenDateRange();

        doReturn("Last 10 Transactions").when(mockCaptions).getRecent();

        doReturn(mockContainer).when(mockView).getContainer();
    }

    public TransactionPeriodsPresenterBuilder setTransactionsRecentString(String transactionsRecentString) {
        doReturn(transactionsRecentString).when(mockCaptions).getRecent();

        return this;
    }

    public TransactionPeriodsPresenter build() {
        TransactionPeriodsPresenter presenter = new TransactionPeriodsPresenter(mockTransactionRepo, mockCaptions);

        presenter.bindView(mockView);

        return presenter;
    }

    public ITransactionPeriodsView verifyView(int times) {
        return Mockito.verify(mockView, Mockito.times(times));
    }

    public ITransactionPeriodsView verifyView() {
        return Mockito.verify(mockView);
    }

    public Container verifyContainer() {
        return Mockito.verify(mockContainer);
    }

    public TransactionPeriodsPresenterBuilder olderTransactionsAvailable(boolean transactionsAvailable) {
        doReturn(transactionsAvailable).when(mockTransactionRepo).transactionsOlderThan(anyLong());

        return this;
    }

    public TransactionPeriodsPresenterBuilder withLastTenDateRange(long lowerStamp, long upperStamp) {
        doReturn(new DateRange(lowerStamp, upperStamp)).when(mockTransactionRepo).getLastTenDateRange();

        return this;
    }

    public TransactionPeriodsPresenterBuilder withLastTenRange(long lowerStampMillis, long upperStampMillis) {
        doReturn(new DateRange(lowerStampMillis, upperStampMillis)).when(mockTransactionRepo).getLastTenDateRange();

        return this;
    }
}