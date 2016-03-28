package clowdtech.mpositive.areas.reporting.transaction.presenters;

import android.content.Context;

import com.clowdtech.data.entities.ITransaction;
import com.clowdtech.data.entities.ITransactionNoSale;
import com.clowdtech.data.entities.PaymentTypes;
import com.starmicronics.stario.StarIOPortException;

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

import java.util.ArrayList;
import java.util.List;

import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.reporting.transaction.builder.TransactionBuilder;
import clowdtech.mpositive.areas.reporting.transaction.captions.CaptionsTransactionSummary;
import clowdtech.mpositive.areas.reporting.transaction.viewModels.TransactionListItemViewModel;
import clowdtech.printer.TransactionsReportFormatInfo;

import static junit.framework.Assert.assertEquals;


@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
public class TransactionSummaryPresenterPrintTest {

    @Mock
    Context context;

    @Captor
    ArgumentCaptor<String> stringCaptor;

    @Captor
    ArgumentCaptor<Long> longCaptor;

    @Captor
    ArgumentCaptor<Boolean> boolCaptor;

    @Captor
    ArgumentCaptor<List<TransactionListItemViewModel>> listItemCaptor;

    @Captor
    ArgumentCaptor<TransactionsReportFormatInfo> formatCaptor;

    private TransactionSummaryPresenterBuilder presenterBuilder;

    @Before
    public void setUp() throws Exception {
        presenterBuilder = new TransactionSummaryPresenterBuilder();

        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void Print_MultipleTransactions_ExpectCorrectFormatHeaderRequested() throws StarIOPortException {
        //arrange
        String expectedHeading = "Transactions Report";

        ArrayList<ITransaction> receipts = new ArrayList<>();

        ITransaction receipt1 = new TransactionBuilder()
                .withDate(new DateTime(2014, 11, 1, 10, 0))
                .withPayment(PaymentTypes.Card, 11.75)
                .withManualLine(3.45)
                .build();

        receipts.add(receipt1);

        TransactionSummaryPresenter presenter = presenterBuilder
                .withTransactions(receipts)
                .withHeader(expectedHeading)
                .withData()
                .build();

        //act
        presenter.printTransactions();

        //assert
        presenterBuilder.verifyPrintController(1).printTransactionsReport(Mockito.isA(Context.class), formatCaptor.capture());

        TransactionsReportFormatInfo formatInfo = formatCaptor.getValue();

        assertEquals(expectedHeading, formatInfo.getHeader());
    }

    @Test
    public void Print_MultipleTransactions_ExpectCorrectFormatSubHeaderRequested() throws StarIOPortException {
        //arrange
        String expectedHeading = "2014-11-01";

        ArrayList<ITransaction> receipts = new ArrayList<>();

        ITransaction receipt1 = new TransactionBuilder()
                .withDate(new DateTime(2014, 11, 1, 10, 0))
                .withPayment(PaymentTypes.Card, 11.75)
                .withManualLine(3.45)
                .build();

        receipts.add(receipt1);

        TransactionSummaryPresenter presenter = presenterBuilder
                .withTransactions(receipts)
                .withData()
                .build();

        //act
        presenter.printTransactions();

        //assert
        presenterBuilder.verifyPrintController(1).printTransactionsReport(Mockito.isA(Context.class), formatCaptor.capture());

        TransactionsReportFormatInfo formatInfo = formatCaptor.getValue();

        assertEquals(expectedHeading, formatInfo.getSubHeader());
    }

    @Test
    public void Print_WithSingleTransaction_ExpectTransactionSet() throws Exception {
        //arrange
        DateTime now = DateTime.now();

        ArrayList<ITransaction> receipts = new ArrayList<>();

        ITransaction receipt1 = new TransactionBuilder()
                .withDate(now)
                .withPayment(PaymentTypes.Card, 11.75)
                .withProduct("mars bar", 2.00, 1).withProduct("mars bar", 3.50, 1)
                .withManualLine(3.45)
                .build();

        receipts.add(receipt1);

        TransactionSummaryPresenter presenter = presenterBuilder
                .withTransactions(receipts)
                .withData()
                .build();

        //act
        presenter.printTransactions();

        //assert
        presenterBuilder.verifyPrintController(1).printTransactionsReport(Mockito.isA(Context.class), formatCaptor.capture());

        TransactionsReportFormatInfo formatInfo = formatCaptor.getValue();

        assertEquals(1, formatInfo.getReportRows().size());
        assertEquals(now.toString("yyyy-MM-dd HH:mm:ss"), formatInfo.getReportRows().get(0)[1]);
        assertEquals("8.95", formatInfo.getReportRows().get(0)[3]);
    }

    @Test
    public void Print_WithMultipleTransactions_ExpectTransactionsSet() throws Exception {
        //arrange
        ArrayList<ITransaction> receipts = new ArrayList<>();

        ITransaction receipt1 = new TransactionBuilder()
                .withPayment(PaymentTypes.Card, 11.75)
                .withProduct("mars bar", 2.00, 1).withProduct("mars bar", 3.50, 1)
                .withManualLine(3.45)
                .build();

        receipts.add(receipt1);

        ITransaction receipt2 = new TransactionBuilder()
                .withPayment(PaymentTypes.Card, 11.75)
                .withProduct("mars bar", 2.00, 1).withProduct("mars bar", 3.50, 1)
                .withManualLine(3.45)
                .build();

        receipts.add(receipt2);

        ITransaction receipt3 = new TransactionBuilder()
                .withDate(new DateTime(2014, 10, 2, 10, 37))
                .withPayment(PaymentTypes.Card, 11.75)
                .withProduct("mars bar", 2.00, 1).withProduct("mars bar", 3.50, 1)
                .withManualLine(3.45)
                .build();

        receipts.add(receipt3);

        TransactionSummaryPresenter presenter = presenterBuilder
                .withTransactions(receipts)
                .withData()
                .build();

        //act
        presenter.printTransactions();

        //assert
        presenterBuilder.verifyPrintController(1).printTransactionsReport(Mockito.isA(Context.class), formatCaptor.capture());

        TransactionsReportFormatInfo formatInfo = formatCaptor.getValue();

        assertEquals(3, formatInfo.getReportRows().size());
        assertEquals("2014-10-02 10:37:00", formatInfo.getReportRows().get(2)[1]);
        assertEquals("8.95", formatInfo.getReportRows().get(2)[3]);
    }

    @Test
    public void Print_WithMultipleTransactionsAndNoSale_ExpectTransactionsSet() throws Exception {
        //arrange
        ArrayList<ITransaction> receipts = new ArrayList<>();

        ITransaction receipt1 = new TransactionBuilder()
                .withPayment(PaymentTypes.Card, 11.75)
                .withProduct("mars bar", 2.00, 1).withProduct("mars bar", 3.50, 1)
                .withManualLine(3.45)
                .build();

        receipts.add(receipt1);

        ITransaction receipt2 = new TransactionBuilder()
                .withPayment(PaymentTypes.Card, 11.75)
                .withProduct("mars bar", 2.00, 1).withProduct("mars bar", 3.50, 1)
                .withManualLine(3.45)
                .build();

        receipts.add(receipt2);

        ITransaction receipt3 = new TransactionBuilder()
                .withDate(new DateTime(2014, 10, 2, 10, 37))
                .withPayment(PaymentTypes.Card, 11.75)
                .withProduct("mars bar", 2.00, 1).withProduct("mars bar", 3.50, 1)
                .withManualLine(3.45)
                .build();

        receipts.add(receipt3);

        List<ITransactionNoSale> noSales = new ArrayList<>();

        noSales.add(new ITransactionNoSale() {
            @Override
            public DateTime getCreatedDate() {
                return new DateTime(2015, 1, 10, 10, 37);
            }

            @Override
            public void setCreatedDate(DateTime datetime) {
            }
        });

        TransactionSummaryPresenter presenter = presenterBuilder
                .withTransactions(receipts)
                .withNoSales(noSales)
                .withData()
                .build();

        //act
        presenter.printTransactions();

        //assert
        presenterBuilder.verifyPrintController(1).printTransactionsReport(Mockito.isA(Context.class), formatCaptor.capture());

        TransactionsReportFormatInfo formatInfo = formatCaptor.getValue();

        assertEquals(4, formatInfo.getReportRows().size());
        assertEquals("2015-01-10 10:37:00", formatInfo.getReportRows().get(2)[1]);
        assertEquals("No Sale", formatInfo.getReportRows().get(2)[0]);
    }

    @Test
    public void Print_WithMultipleTransactions_ExpectTransactionCountSet() throws Exception {
        //arrange
        ArrayList<ITransaction> receipts = new ArrayList<>();

        ITransaction receipt1 = new TransactionBuilder()
                .withPayment(PaymentTypes.Card, 11.75)
                .withManualLine(3.45)
                .build();

        receipts.add(receipt1);

        ITransaction receipt2 = new TransactionBuilder()
                .withPayment(PaymentTypes.Card, 11.75)
                .withManualLine(3.45)
                .build();

        receipts.add(receipt2);

        ITransaction receipt3 = new TransactionBuilder()
                .withPayment(PaymentTypes.Card, 11.75)
                .withProduct("mars bar", 2.00, 1)
                .build();

        receipts.add(receipt3);

        TransactionSummaryPresenter presenter = presenterBuilder
                .withTransactions(receipts)
                .withData()
                .build();

        //act
        presenter.printTransactions();

        //assert
        presenterBuilder.verifyPrintController(1).printTransactionsReport(Mockito.isA(Context.class), formatCaptor.capture());

        TransactionsReportFormatInfo formatInfo = formatCaptor.getValue();

        assertEquals(3, formatInfo.TransactionCount);
    }

    @Test
    public void Print_WithMultipleTransactionsOneRefunded_ExpectTransactionCountSet() throws Exception {
        //arrange
        ArrayList<ITransaction> receipts = new ArrayList<>();

        ITransaction receipt1 = new TransactionBuilder()
                .withPayment(PaymentTypes.Card, 11.75)
                .withManualLine(3.45)
                .build();

        receipts.add(receipt1);

        ITransaction receipt2 = new TransactionBuilder()
                .withPayment(PaymentTypes.Card, 11.75)
                .withManualLine(3.45)
                .refunded()
                .build();

        receipts.add(receipt2);

        ITransaction receipt3 = new TransactionBuilder()
                .withPayment(PaymentTypes.Card, 11.75)
                .withProduct("mars bar", 2.00, 1)
                .build();

        receipts.add(receipt3);

        TransactionSummaryPresenter presenter = presenterBuilder
                .withTransactions(receipts)
                .withData()
                .build();

        //act
        presenter.printTransactions();

        //assert
        presenterBuilder.verifyPrintController(1).printTransactionsReport(Mockito.isA(Context.class), formatCaptor.capture());

        TransactionsReportFormatInfo formatInfo = formatCaptor.getValue();

        assertEquals(2, formatInfo.TransactionCount);
    }

    @Test
    public void Print_WithMultipleTransactions_ExpectCardValueSet() throws Exception {
        //arrange
        TransactionBuilder transactionBuilder = new TransactionBuilder();

        ArrayList<ITransaction> receipts = new ArrayList<>();

        ITransaction receipt1 = transactionBuilder
                .withPayment(PaymentTypes.Card, 11.75)
                .withProduct("mars bar", 2.00, 1).withProduct("mars bar", 3.50, 1)
                .withManualLine(3.45)
                .build();

        receipts.add(receipt1);

        TransactionBuilder transactionBuilder2 = new TransactionBuilder();

        ITransaction receipt2 = transactionBuilder2
                .withPayment(PaymentTypes.Cash, 11.75)
                .withProduct("mars bar", 2.00, 1)
                .withManualLine(3.45).withManualLine(3.45)
                .build();

        receipts.add(receipt2);

        TransactionSummaryPresenter presenter = presenterBuilder
                .withTransactions(receipts)
                .withData()
                .build();

        //act
        presenter.printTransactions();

        //assert
        presenterBuilder.verifyPrintController(1).printTransactionsReport(Mockito.isA(Context.class), formatCaptor.capture());

        TransactionsReportFormatInfo formatInfo = formatCaptor.getValue();

        assertEquals("8.95", formatInfo.AmountCard);
    }

    @Test
    public void Print_WithMultipleTransactionsOneRefunded_ExpectCardValueSet() throws Exception {
        //arrange
        TransactionBuilder transactionBuilder = new TransactionBuilder();

        ArrayList<ITransaction> receipts = new ArrayList<>();

        ITransaction receipt1 = transactionBuilder
                .withPayment(PaymentTypes.Card, 11.75)
                .withProduct("mars bar", 2.00, 1).withProduct("mars bar", 3.50, 1)
                .withManualLine(3.45)
                .build();

        receipts.add(receipt1);

        TransactionBuilder transactionBuilder2 = new TransactionBuilder();

        ITransaction receipt2 = transactionBuilder2
                .withPayment(PaymentTypes.Card, 11.75)
                .withProduct("mars bar", 2.00, 1)
                .withManualLine(3.45).withManualLine(3.45)
                .refunded()
                .build();

        receipts.add(receipt2);

        TransactionSummaryPresenter presenter = presenterBuilder
                .withTransactions(receipts)
                .withData()
                .build();

        //act
        presenter.printTransactions();

        //assert
        presenterBuilder.verifyPrintController(1).printTransactionsReport(Mockito.isA(Context.class), formatCaptor.capture());

        TransactionsReportFormatInfo formatInfo = formatCaptor.getValue();

        assertEquals("8.95", formatInfo.AmountCard);
    }

    @Test
    public void Print_WithMultipleTransactions_ExpectCashValueSet() throws Exception {
        //arrange
        TransactionBuilder transactionBuilder1 = new TransactionBuilder();

        ArrayList<ITransaction> receipts = new ArrayList<>();

        ITransaction receipt1 = transactionBuilder1
                .withPayment(PaymentTypes.Card, 11.75)
                .withProduct("mars bar", 2.00, 1).withProduct("mars bar", 3.50, 1)
                .withManualLine(3.45)
                .build();

        receipts.add(receipt1);

        TransactionBuilder transactionBuilder2 = new TransactionBuilder();

        ITransaction receipt2 = transactionBuilder2
                .withPayment(PaymentTypes.Cash, 10.25)
                .withProduct("mars bar", 2.40, 1)
                .withManualLine(2.75).withManualLine(3.05)
                .build();

        receipts.add(receipt2);

        TransactionSummaryPresenter presenter = presenterBuilder
                .withTransactions(receipts)
                .withData()
                .build();

        //act
        presenter.printTransactions();

        //assert
        presenterBuilder.verifyPrintController(1).printTransactionsReport(Mockito.isA(Context.class), formatCaptor.capture());

        TransactionsReportFormatInfo formatInfo = formatCaptor.getValue();

        assertEquals("8.20", formatInfo.AmountCash);
    }

    @Test
    public void Print_WithMultipleTransactionsOneRefunded_ExpectCashValueSet() throws Exception {
        //arrange
        TransactionBuilder transactionBuilder1 = new TransactionBuilder();

        ArrayList<ITransaction> receipts = new ArrayList<>();

        ITransaction receipt1 = transactionBuilder1
                .withPayment(PaymentTypes.Cash, 11.75)
                .withProduct("mars bar", 2.00, 1).withProduct("mars bar", 3.50, 1)
                .withManualLine(3.45)
                .refunded()
                .build();

        receipts.add(receipt1);

        TransactionBuilder transactionBuilder2 = new TransactionBuilder();

        ITransaction receipt2 = transactionBuilder2
                .withPayment(PaymentTypes.Cash, 10.25)
                .withProduct("mars bar", 2.40, 1)
                .withManualLine(2.75).withManualLine(3.05)
                .build();

        receipts.add(receipt2);

        TransactionSummaryPresenter presenter = presenterBuilder
                .withTransactions(receipts)
                .withData()
                .build();

        //act
        presenter.printTransactions();

        //assert
        presenterBuilder.verifyPrintController(1).printTransactionsReport(Mockito.isA(Context.class), formatCaptor.capture());

        TransactionsReportFormatInfo formatInfo = formatCaptor.getValue();

        assertEquals("8.20", formatInfo.AmountCash);
    }

    @Test
    public void Print_WithMultipleTransactions_ExpectOtherValueSet() throws Exception {
        //arrange
        TransactionBuilder transactionBuilder = new TransactionBuilder();

        ArrayList<ITransaction> receipts = new ArrayList<>();

        ITransaction receipt1 = transactionBuilder
                .withPayment(PaymentTypes.Other, 1256.87)
                .withProduct("mars bar", 1.20, 1).withProduct("mars bar", 1.30, 1)
                .build();

        receipts.add(receipt1);

        TransactionBuilder transactionBuilder2 = new TransactionBuilder();

        ITransaction receipt2 = transactionBuilder2
                .withPayment(PaymentTypes.Cash, 10.25)
                .withProduct("mars bar", 2.40, 1)
                .withManualLine(2.75).withManualLine(3.05)
                .build();

        receipts.add(receipt2);

        TransactionSummaryPresenter presenter = presenterBuilder
                .withTransactions(receipts)
                .withData()
                .build();

        //act
        presenter.printTransactions();

        //assert
        presenterBuilder.verifyPrintController(1).printTransactionsReport(Mockito.isA(Context.class), formatCaptor.capture());

        TransactionsReportFormatInfo formatInfo = formatCaptor.getValue();

        assertEquals("2.50", formatInfo.AmountOther);
    }

    @Test
    public void Print_WithMultipleTransactionsOneRefunded_ExpectOtherValueSet() throws Exception {
        //arrange
        TransactionBuilder transactionBuilder = new TransactionBuilder();

        ArrayList<ITransaction> receipts = new ArrayList<>();

        ITransaction receipt1 = transactionBuilder
                .withPayment(PaymentTypes.Other, 1256.87)
                .withProduct("mars bar", 1.20, 1).withProduct("mars bar", 1.30, 1)
                .build();

        receipts.add(receipt1);

        TransactionBuilder transactionBuilder2 = new TransactionBuilder();

        ITransaction receipt2 = transactionBuilder2
                .withPayment(PaymentTypes.Other, 10.25)
                .withProduct("mars bar", 2.40, 1)
                .withManualLine(2.75).withManualLine(3.05)
                .refunded()
                .build();

        receipts.add(receipt2);

        TransactionSummaryPresenter presenter = presenterBuilder
                .withTransactions(receipts)
                .withData()
                .build();

        //act
        presenter.printTransactions();

        //assert
        presenterBuilder.verifyPrintController(1).printTransactionsReport(Mockito.isA(Context.class), formatCaptor.capture());

        TransactionsReportFormatInfo formatInfo = formatCaptor.getValue();

        assertEquals("2.50", formatInfo.AmountOther);
    }
}