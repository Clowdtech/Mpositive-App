package clowdtech.mpositive.areas.reporting.transaction.presenters;

import android.content.Context;

import com.clowdtech.data.entities.ITransaction;
import com.clowdtech.data.entities.ITransactionNoSale;
import com.clowdtech.data.entities.PaymentTypes;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import clowdtech.mpositive.areas.reporting.transaction.builder.TransactionBuilder;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;


@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
public class TransactionSummaryPresenterShareTest {

    @Captor
    ArgumentCaptor<String> stringCaptor;

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
    public void Share_SingleTransaction_ExpectCorrectFormatRequested() {
        //arrange
        String expectedHeading =    "\"Transactions Report\"\r\n" +
                                    "\"2014-11-01\"\r\n" +
                                    "\r\n" +
                                    "\"TransId\",\"Date\",\"PType\",\"Total\"\r\n" +
                                    "\"20049\",\"2014-11-01 10:37:16\",\"Card\",\"3.45\"\r\n" +
                                    "\r\n" +
                                    "\"Card\",\"Cash\",\"Other\",\"Count\",\"Average\"\r\n" +
                                    "\"3.45\",\"0.00\",\"0.00\",\"1\",\"3.45\"";

        ArrayList<ITransaction> receipts = new ArrayList<>();

        ITransaction receipt1 = new TransactionBuilder()
                .withId(20049)
                .withDate(new DateTime(2014, 11, 1, 10, 37, 16))
                .withPayment(PaymentTypes.Card, 11.75)
                .withManualLine(3.45)
                .build();

        receipts.add(receipt1);

        TransactionSummaryPresenter presenter = presenterBuilder
                .withTransactions(receipts)
                .withHeader("Transactions Report")
                .withCashCaption("Cash")
                .withCardCaption("Card")
                .withOtherCaption("Other")
                .withData()
                .build();

        //act
        presenter.shareTransactions();

        //assert
        presenterBuilder.verifyShareController(1).shareTransactionsReport(any(Context.class), anyString(), stringCaptor.capture());

        assertEquals(expectedHeading, stringCaptor.getValue());
    }

    @Test
    public void Share_MultipleTransactions_ExpectCorrectFormatRequested() {
        //arrange
        String expectedHeading =    "\"Transactions Report\"\r\n" +
                                    "\"2014-11-01 - 2014-02-25\"\r\n" +
                                    "\r\n" +
                                    "\"TransId\",\"Date\",\"PType\",\"Total\"\r\n" +
                                    "\"20049\",\"2014-11-01 10:37:16\",\"Card\",\"3.45\"\r\n" +
                                    "\"1127\",\"2014-02-25 18:20:34\",\"Cash\",\"15.00\"\r\n" +
                                    "\r\n" +
                                    "\"Card\",\"Cash\",\"Other\",\"Count\",\"Average\"\r\n" +
                                    "\"3.45\",\"15.00\",\"0.00\",\"2\",\"9.23\"";

        ArrayList<ITransaction> receipts = new ArrayList<>();

        ITransaction receipt1 = new TransactionBuilder()
                .withId(20049)
                .withDate(new DateTime(2014, 11, 1, 10, 37, 16))
                .withPayment(PaymentTypes.Card, 11.75)
                .withManualLine(3.45)
                .build();

        receipts.add(receipt1);

        ITransaction receipt2 = new TransactionBuilder()
                .withId(1127)
                .withDate(new DateTime(2014, 2, 25, 18, 20, 34))
                .withPayment(PaymentTypes.Cash, 24.78)
                .withProduct("mars bar", 7.5, 2)
                .build();

        receipts.add(receipt2);

        TransactionSummaryPresenter presenter = presenterBuilder
                .withTransactions(receipts)
                .withHeader("Transactions Report")
                .withCashCaption("Cash")
                .withCardCaption("Card")
                .withOtherCaption("Other")
                .withData()
                .build();

        //act
        presenter.shareTransactions();

        //assert
        presenterBuilder.verifyShareController(1).shareTransactionsReport(any(Context.class), anyString(), stringCaptor.capture());

        assertEquals(expectedHeading, stringCaptor.getValue());
    }

    @Test
    public void Share_MultipleTransactionsOneRefunded_ExpectCorrectFormatRequested() {
        //arrange
        String expectedHeading =    "\"Transactions Report\"\r\n" +
                                    "\"2014-11-01 - 2014-02-25\"\r\n" +
                                    "\r\n" +
                                    "\"TransId\",\"Date\",\"PType\",\"Total\"\r\n" +
                                    "\"20049\",\"2014-11-01 10:37:16\",\"Card\",\"3.45\"\r\n" +
                                    "\"1127\",\"2014-02-25 18:20:34\",\"Cash\",\"(15.00)\"\r\n" +
                                    "\r\n" +
                                    "\"Card\",\"Cash\",\"Other\",\"Count\",\"Average\"\r\n" +
                                    "\"3.45\",\"0.00\",\"0.00\",\"1\",\"3.45\"";

        ArrayList<ITransaction> receipts = new ArrayList<>();

        ITransaction receipt1 = new TransactionBuilder()
                .withId(20049)
                .withDate(new DateTime(2014, 11, 1, 10, 37, 16))
                .withPayment(PaymentTypes.Card, 11.75)
                .withManualLine(3.45)
                .build();

        receipts.add(receipt1);

        ITransaction receipt2 = new TransactionBuilder()
                .withId(1127)
                .withDate(new DateTime(2014, 2, 25, 18, 20, 34))
                .withPayment(PaymentTypes.Cash, 24.78)
                .withProduct("mars bar", 7.5, 2)
                .refunded()
                .build();

        receipts.add(receipt2);

        TransactionSummaryPresenter presenter = presenterBuilder
                .withTransactions(receipts)
                .withHeader("Transactions Report")
                .withCashCaption("Cash")
                .withCardCaption("Card")
                .withOtherCaption("Other")
                .withData()
                .build();

        //act
        presenter.shareTransactions();

        //assert
        presenterBuilder.verifyShareController(1).shareTransactionsReport(any(Context.class), anyString(), stringCaptor.capture());

        assertEquals(expectedHeading, stringCaptor.getValue());
    }

    @Test
    public void Share_MultipleTransactionsAndNoSale_ExpectCorrectFormatRequested() {
        //arrange
        String expectedHeading =    "\"Transactions Report\"\r\n" +
                                    "\"2014-11-01 - 2014-02-25\"\r\n" +
                                    "\r\n" +
                                    "\"TransId\",\"Date\",\"PType\",\"Total\"\r\n" +
                                    "\"20049\",\"2014-11-01 10:37:16\",\"Card\",\"3.45\"\r\n" +
                                    "\"No Sale\",\"2014-07-24 19:57:04\",\"\",\"\"\r\n" +
                                    "\"1127\",\"2014-02-25 18:20:34\",\"Cash\",\"15.00\"\r\n" +
                                    "\r\n" +
                                    "\"Card\",\"Cash\",\"Other\",\"Count\",\"Average\"\r\n" +
                                    "\"3.45\",\"15.00\",\"0.00\",\"2\",\"9.23\"";

        ArrayList<ITransaction> receipts = new ArrayList<>();

        ITransaction receipt1 = new TransactionBuilder()
                .withId(20049)
                .withDate(new DateTime(2014, 11, 1, 10, 37, 16))
                .withPayment(PaymentTypes.Card, 11.75)
                .withManualLine(3.45)
                .build();

        receipts.add(receipt1);

        ITransaction receipt2 = new TransactionBuilder()
                .withId(1127)
                .withDate(new DateTime(2014, 2, 25, 18, 20, 34))
                .withPayment(PaymentTypes.Cash, 24.78)
                .withProduct("mars bar", 7.5, 2)
                .build();

        receipts.add(receipt2);

        List<ITransactionNoSale> noSales = new ArrayList<>();

        noSales.add(new ITransactionNoSale() {
            @Override
            public DateTime getCreatedDate() {
                return new DateTime(2014, 7, 24, 19, 57, 4);
            }

            @Override
            public void setCreatedDate(DateTime datetime) {
            }
        });

        TransactionSummaryPresenter presenter = presenterBuilder
                .withTransactions(receipts)
                .withNoSales(noSales)
                .withHeader("Transactions Report")
                .withCashCaption("Cash")
                .withCardCaption("Card")
                .withOtherCaption("Other")
                .withData()
                .build();

        //act
        presenter.shareTransactions();

        //assert
        presenterBuilder.verifyShareController(1).shareTransactionsReport(any(Context.class), anyString(), stringCaptor.capture());

        assertEquals(expectedHeading, stringCaptor.getValue());
    }
}