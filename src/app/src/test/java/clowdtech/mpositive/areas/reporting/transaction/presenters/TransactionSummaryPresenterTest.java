package clowdtech.mpositive.areas.reporting.transaction.presenters;

import com.clowdtech.data.entities.ITransaction;
import com.clowdtech.data.entities.ITransactionNoSale;
import com.clowdtech.data.entities.PaymentTypes;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import clowdtech.mpositive.areas.reporting.transaction.builder.TransactionBuilder;
import clowdtech.mpositive.areas.reporting.transaction.viewModels.TransactionListItemViewModel;
import clowdtech.printer.TransactionsReportFormatInfo;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;


@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
public class TransactionSummaryPresenterTest {

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
    public void View_WithHeading_ExpectHeadingSet() throws Exception {
        //arrange

        String expectedHeading = "test heading";

        TransactionSummaryPresenter presenter = presenterBuilder
                .build();

        //act
        presenter.setData(expectedHeading, DateTime.now().getMillis(), DateTime.now().getMillis());

        //assert
        presenterBuilder.verifyView(1).setHeading(stringCaptor.capture());

        assertEquals(expectedHeading, stringCaptor.getValue());
    }

    @Test
    public void View_WithSingleTransaction_ExpectTransactionSet() throws Exception {
        //arrange
        ArrayList<ITransaction> receipts = new ArrayList<>();

        DateTime now = DateTime.now();

        ITransaction receipt1 = new TransactionBuilder()
                .withDate(now)
                .withPayment(PaymentTypes.Card, 11.75)
                .withProduct("mars bar", 2.00, 1).withProduct("mars bar", 3.50, 1)
                .withManualLine(3.45)
                .build();

        receipts.add(receipt1);

        TransactionSummaryPresenter presenter = presenterBuilder
                .withTransactions(receipts)
                .build();

        //act
        presenter.setData("", DateTime.now().getMillis(), DateTime.now().getMillis());

        //assert
        presenterBuilder.verifyView(1).setItems(listItemCaptor.capture());

        assertEquals(1, listItemCaptor.getValue().size());
        assertEquals(String.format("Today %s", DateTimeFormat.forPattern("HH:mm:ss a").print(now)), listItemCaptor.getValue().get(0).getTitle());
        assertEquals(NumberFormat.getCurrencyInstance().format(8.95), listItemCaptor.getValue().get(0).getInfo());
    }

    @Test
    public void View_WithMultipleTransactions_ExpectAverageTransactionsSet() throws Exception {
        //arrange
        ArrayList<ITransaction> receipts = new ArrayList<>();

        ITransaction receipt1 = new TransactionBuilder()
                .withPayment(PaymentTypes.Card, 11.75)
                .withProduct("mars bar", 2.00, 1)
                .build();

        receipts.add(receipt1);

        ITransaction receipt2 = new TransactionBuilder()
                .withPayment(PaymentTypes.Cash, 11.75)
                .withProduct("mars bar", 3.00, 1)
                .build();

        receipts.add(receipt2);

        ITransaction receipt3 = new TransactionBuilder()
                .withDate(new DateTime(2014, 10, 2, 10, 37))
                .withPayment(PaymentTypes.Card, 11.75)
                .withManualLine(4.00)
                .build();

        receipts.add(receipt3);

        TransactionSummaryPresenter presenter = presenterBuilder
                .withTransactions(receipts)
                .build();

        //act
        presenter.setData("", DateTime.now().getMillis(), DateTime.now().getMillis());

        //assert
        presenterBuilder.verifyView(1).setAverageTransaction(stringCaptor.capture());

        assertEquals(String.format("(avg. %s)", NumberFormat.getCurrencyInstance().format(3.00)), stringCaptor.getValue());
    }

    @Test
    public void View_WithZeroTransactions_ExpectAverageTransactionsSetToZero() throws Exception {
        //arrange
        ArrayList<ITransaction> receipts = new ArrayList<>();

        TransactionSummaryPresenter presenter = presenterBuilder
                .withTransactions(receipts)
                .build();

        //act
        presenter.setData("", DateTime.now().getMillis(), DateTime.now().getMillis());

        //assert
        presenterBuilder.verifyView(1).setAverageTransaction(stringCaptor.capture());

        assertEquals(String.format("(avg. %s)", NumberFormat.getCurrencyInstance().format(0.00)), stringCaptor.getValue());
    }

    @Test
    public void View_WithMultipleTransactions_ExpectTransactionsSet() throws Exception {
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
                .build();

        //act
        presenter.setData("", DateTime.now().getMillis(), DateTime.now().getMillis());

        //assert
        presenterBuilder.verifyView(1).setItems(listItemCaptor.capture());

        assertEquals(3, listItemCaptor.getValue().size());

        assertEquals("02/10/14 10:37:00 AM", listItemCaptor.getValue().get(2).getTitle());
        assertEquals(NumberFormat.getCurrencyInstance().format(8.95), listItemCaptor.getValue().get(2).getInfo());
    }

    @Test
    public void View_WithMultipleTransactionsAndNoSale_ExpectTransactionsSet() throws Exception {
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
                .build();

        //act
        presenter.setData("", DateTime.now().getMillis(), DateTime.now().getMillis());

        //assert
        presenterBuilder.verifyView(1).setItems(listItemCaptor.capture());

        assertEquals(4, listItemCaptor.getValue().size());

        assertEquals("10/01/15 10:37:00 AM", listItemCaptor.getValue().get(2).getTitle());
        assertEquals("No Sale", listItemCaptor.getValue().get(2).getInfo());
    }

    @Test
    public void View_WithMultipleTransactions_ExpectTransactionCountSet() throws Exception {
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
                .build();

        //act
        presenter.setData("", DateTime.now().getMillis(), DateTime.now().getMillis());

        //assert
        presenterBuilder.verifyView(1).setTransactionTotal(stringCaptor.capture());

        assertEquals("3 transactions", stringCaptor.getValue());
    }

    @Test
    public void View_WithMultipleTransactionsOneRefunded_ExpectTransactionCountSet() throws Exception {
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
                .build();

        //act
        presenter.setData("", DateTime.now().getMillis(), DateTime.now().getMillis());

        //assert
        presenterBuilder.verifyView(1).setTransactionTotal(stringCaptor.capture());

        assertEquals("2 transactions", stringCaptor.getValue());
    }

    @Test
    public void View_WithMultipleTransactions_ExpectCardValueSet() throws Exception {
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
                .build();

        //act
        presenter.setData("", DateTime.now().getMillis(), DateTime.now().getMillis());

        //assert
        presenterBuilder.verifyView(1).setCardTotal(stringCaptor.capture());

        assertEquals(NumberFormat.getCurrencyInstance().format(8.95), stringCaptor.getValue());
    }

    @Test
    public void View_WithMultipleTransactionsOneRefunded_ExpectCardValueSet() throws Exception {
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
                .build();

        //act
        presenter.setData("", DateTime.now().getMillis(), DateTime.now().getMillis());

        //assert
        presenterBuilder.verifyView(1).setCardTotal(stringCaptor.capture());

        assertEquals(NumberFormat.getCurrencyInstance().format(8.95), stringCaptor.getValue());
    }

    @Test
    public void View_WithMultipleTransactions_ExpectCashValueSet() throws Exception {
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
                .build();

        //act
        presenter.setData("", DateTime.now().getMillis(), DateTime.now().getMillis());

        //assert
        presenterBuilder.verifyView(1).setCashTotal(stringCaptor.capture());

        assertEquals(NumberFormat.getCurrencyInstance().format(8.20), stringCaptor.getValue());
    }

    @Test
    public void View_WithMultipleTransactionsOneRefunded_ExpectCashValueSet() throws Exception {
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
                .build();

        //act
        presenter.setData("", DateTime.now().getMillis(), DateTime.now().getMillis());

        //assert
        presenterBuilder.verifyView(1).setCashTotal(stringCaptor.capture());

        assertEquals(NumberFormat.getCurrencyInstance().format(8.20), stringCaptor.getValue());
    }

    @Test
    public void View_WithMultipleTransactions_ExpectOtherValueSet() throws Exception {
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
                .build();

        //act
        presenter.setData("", DateTime.now().getMillis(), DateTime.now().getMillis());

        //assert
        presenterBuilder.verifyView(1).setOtherTotal(stringCaptor.capture());

        assertEquals(NumberFormat.getCurrencyInstance().format(2.50), stringCaptor.getValue());
    }

    @Test
    public void View_WithMultipleTransactionsOneRefunded_ExpectOtherValueSet() throws Exception {
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
                .build();

        //act
        presenter.setData("", DateTime.now().getMillis(), DateTime.now().getMillis());

        //assert
        presenterBuilder.verifyView(1).setOtherTotal(stringCaptor.capture());

        assertEquals(NumberFormat.getCurrencyInstance().format(2.50), stringCaptor.getValue());
    }

    @Test
    public void View_WithNoTransactions_ExpectSharedDisabled() throws Exception {
        //arrange
        TransactionSummaryPresenter presenter = presenterBuilder
                .build();

        //act
        presenter.setData("", DateTime.now().getMillis(), DateTime.now().getMillis());

        //assert
        presenterBuilder.verifyView(1).setShareEnabled(boolCaptor.capture());

        assertEquals(false, boolCaptor.getValue().booleanValue());
    }

    @Test
    public void View_WithMultipleTransactions_ExpectShareEnabled() throws Exception {
        //arrange
        ArrayList<ITransaction> receipts = new ArrayList<>();

        ITransaction receipt1 = new TransactionBuilder()
                .withPayment(PaymentTypes.Card, 11.75)
                .withManualLine(3.45)
                .build();

        receipts.add(receipt1);

        ITransaction receipt2 = new TransactionBuilder()
                .withPayment(PaymentTypes.Cash, 11.75)
                .withManualLine(3.45)
                .refunded()
                .build();

        receipts.add(receipt2);

        TransactionSummaryPresenter presenter = presenterBuilder
                .withTransactions(receipts)
                .build();

        //act
        presenter.setData("", DateTime.now().getMillis(), DateTime.now().getMillis());

        //assert
        presenterBuilder.verifyView(1).setShareEnabled(boolCaptor.capture());

        assertEquals(true, boolCaptor.getValue().booleanValue());
    }

    @Test
    public void View_WithNoTransactions_ExpectPrintDisabled() throws Exception {
        //arrange
        TransactionSummaryPresenter presenter = presenterBuilder
                .build();

        //act
        presenter.setData("", DateTime.now().getMillis(), DateTime.now().getMillis());

        //assert
        presenterBuilder.verifyView(1).setPrintEnabled(boolCaptor.capture());

        assertEquals(false, boolCaptor.getValue().booleanValue());
    }

    @Test
    public void View_WithMultipleTransactions_ExpectPrintEnabled() throws Exception {
        //arrange
        ArrayList<ITransaction> receipts = new ArrayList<>();

        ITransaction receipt1 = new TransactionBuilder()
                .withPayment(PaymentTypes.Card, 11.75)
                .withManualLine(3.45)
                .build();

        receipts.add(receipt1);

        ITransaction receipt2 = new TransactionBuilder()
                .withPayment(PaymentTypes.Cash, 11.75)
                .withManualLine(3.45)
                .refunded()
                .build();

        receipts.add(receipt2);

        TransactionSummaryPresenter presenter = presenterBuilder
                .withTransactions(receipts)
                .build();

        //act
        presenter.setData("", DateTime.now().getMillis(), DateTime.now().getMillis());

        //assert
        presenterBuilder.verifyView(1).setPrintEnabled(boolCaptor.capture());

        assertEquals(true, boolCaptor.getValue().booleanValue());
    }

    @Test
    public void Context_TransactionSelected_ExpectSelectionCalled() throws Exception {
        //arrange
        TransactionSummaryPresenter presenter = presenterBuilder
                .build();

        presenter.setData("", DateTime.now().getMillis(), DateTime.now().getMillis());

        //act
        presenter.transactionSelected(0, 15);

        //assert
        presenterBuilder.verifyContainer(1).transactionSelected(longCaptor.capture());

        assertEquals(15, longCaptor.getValue().longValue());
    }

    @Test
    public void Context_TransactionSelectedNoSale_ExpectSelectionNotCalled() throws Exception {
        //arrange
        TransactionSummaryPresenter presenter = presenterBuilder
                .build();

        presenter.setData("", DateTime.now().getMillis(), DateTime.now().getMillis());

        //act
        presenter.transactionSelected(5, 0);

        //assert
        presenterBuilder.verifyContainer(0).transactionSelected(anyLong());
    }
}