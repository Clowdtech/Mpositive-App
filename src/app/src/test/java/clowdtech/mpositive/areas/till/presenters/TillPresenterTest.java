//package clowdtech.mpositive.areas.till.presenters;
//
//import android.content.Context;
//
//import com.clowdtech.data.entities.IManualTransactionLine;
//import com.clowdtech.data.entities.IProductTransactionLine;
//import com.clowdtech.data.entities.PaymentTypes;
//import com.clowdtech.data.entities.Product;
//import com.clowdtech.data.repository.CategoryRepository;
//import com.clowdtech.data.repository.IProductRepository;
//import com.clowdtech.data.repository.ITransactionsRepository;
//
//import org.joda.time.DateTime;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.robolectric.RobolectricTestRunner;
//import org.robolectric.annotation.Config;
//
//import java.math.BigDecimal;
//import java.text.NumberFormat;
//import java.util.ArrayList;
//
//import clowdtech.mpositive.areas.till.TillPresenter;
//import clowdtech.mpositive.data.IReceiptLine;
//import clowdtech.mpositive.data.StockUnit;
//import clowdtech.mpositive.data.lines.ManualEntryLine;
//import clowdtech.mpositive.sync.builder.DummyProduct;
//
//import static org.mockito.Matchers.any;
//import static org.mockito.Matchers.anyListOf;
//import static org.mockito.Mockito.when;
//
//
//@RunWith(RobolectricTestRunner.class)
//@Config(manifest = Config.NONE)
//public class TillPresenterTest {
//
//    @Mock
//    Context mockContext;
//
//    @Mock
//    IProductRepository mockProductRepo;
//
//    @Mock
//    CategoryRepository mockCategoryRepo;
//
//    @Mock
//    ITransactionsRepository mockTransactionRepo;
//
//    @Captor
//    ArgumentCaptor<String> stringCaptor;
//
//    @Captor
//    ArgumentCaptor<Long> longCaptor;
//
//    @Captor
//    ArgumentCaptor<IReceiptLine> receiptLineCaptor;
//
//    private TillPresenterBuilder builder;
//
//    @Before
//    public void setUp() throws Exception {
//        MockitoAnnotations.initMocks(this);
//
//        this.builder = new TillPresenterBuilder();
//    }
//
//    @After
//    public void tearDown() throws Exception {
//    }
//
//    @Test
//    public void Presenter_BackButtonOnChargeView_ExpectNavigateBackToCheckout() throws Exception {
//        TillPresenter presenter = builder
//                .withPaymentsInView()
//                .build();
//
//        presenter.addManualEntry("manual", new BigDecimal("34.78"));
//
//        //act
//        boolean backHandled = presenter.isBackHandled();
//
//        //assert
//        areEqual(true, backHandled);
//
//        builder.verifyView(1).hidePaymentChoice();
//        builder.verifyView(1).displayCheckout();
//        builder.verifyView(2).setChargeText("Charge: " + NumberFormat.getCurrencyInstance().format(34.78));
//    }
//
//    @Test
//    public void ChargeInView_ValueCovered_ExpectNavigateToPaymentComplete() throws Exception {
//        TillPresenter presenter = builder
//                .withPaymentsInView()
//                .withPaymentAmount("40.00")
////                .withNewTransactionId(2000)
//                .withInit()
//                .build();
//
//        presenter.addManualEntry("manual", new BigDecimal("34.78"));
//
//        //act
//        presenter.completePayment();
//
//        //assert
//        builder.verifyRepo(1).addTransaction(anyListOf(IManualTransactionLine.class), anyListOf(IProductTransactionLine.class), any(BigDecimal.class), any(PaymentTypes.class));
//        builder.verifyView(1).hidePaymentChoice();
//        builder.verifyView(1).displayCheckout();
//        builder.verifyView(1).navigateToPaymentComplete();
//    }
//}