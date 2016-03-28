package clowdtech.mpositive.areas.reporting.transaction;

import android.content.Context;

import com.clowdtech.data.repository.ITransactionsRepository;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import clowdtech.mpositive.ISharedPreferences;
import clowdtech.printer.IPrintController;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;


@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
public class TransactionReportDualContainerTest {

    @Mock
    Context mockContext;

    @Mock
    ITransactionReportView mockView;

    @Mock
    ITransactionsRepository mockTransactionRepo;

    @Mock
    IPrintController mockPrintController;

    @Mock
    ISharedPreferences mockSharedPreferences;

    @Mock
    IShareController mockShareController;

    @Captor
    ArgumentCaptor<String> stringCaptor;

    @Captor
    ArgumentCaptor<Long> longCaptor;

    private TransactionReportPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        this.presenter = new TransactionReportPresenter();

        this.presenter.bindView(mockView);
    }

    @After
    public void tearDown() throws Exception {
    }

//    @Test
//    public void Presenter_Initialise_ExpectSummaryHidden() throws Exception {
//        //assert
//        verify(mockView, Mockito.times(1)).showPeriods();
//    }

//    @Test
//    public void Presenter_Initialise_ExpectHelpTextDisplayed() throws Exception {
//        //assert
//        verify(mockView, Mockito.times(1)).showHelp();
//    }

    @Test
    public void Presenter_PeriodSelected_ExpectSummarySetAndDisplayed() throws Exception {
        //arrange
        DateTime lower = new DateTime(2014, 9, 21, 13, 37);
        DateTime upper = DateTime.now();

        //act
        this.presenter.periodSelected("Random Header", lower.getMillis(), upper.getMillis());

        //assert
        verify(mockView, Mockito.times(1)).hideHelp();
        verify(mockView, Mockito.times(1)).showSummary(stringCaptor.capture(), longCaptor.capture(), longCaptor.capture());

        assertEquals("Random Header", stringCaptor.getValue());
        assertEquals(lower.getMillis(), longCaptor.getAllValues().get(0).longValue());
        assertEquals(upper.getMillis(), longCaptor.getAllValues().get(1).longValue());
    }

    @Test
    public void Presenter_TransactionSelected_ExpectReceiptSetAndDisplayed() throws Exception {
        //arrange
        long transactionId = 2000;

        //act
        this.presenter.transactionSelected(transactionId);

        //assert
        verify(mockView, Mockito.times(1)).showReceipt(longCaptor.capture());

        assertEquals(transactionId, longCaptor.getValue().longValue());
    }
}