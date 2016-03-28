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
//import clowdtech.mpositive.areas.shared.models.ReceiptLineViewModel;
//import clowdtech.mpositive.areas.till.TillPresenter;
//import clowdtech.mpositive.data.IReceiptLine;
//import clowdtech.mpositive.data.StockUnit;
//import clowdtech.mpositive.data.lines.ManualEntryLine;
//import clowdtech.mpositive.sync.builder.DummyProduct;
//
//import static org.mockito.Matchers.any;
//import static org.mockito.Matchers.anyCollectionOf;
//import static org.mockito.Matchers.anyListOf;
//import static org.mockito.internal.matchers.Equality.areEqual;
//
//
//@RunWith(RobolectricTestRunner.class)
//@Config(manifest = Config.NONE)
//public class CheckoutPresenterTest {
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
//    private CheckoutPresenterBuilder builder;
//
//    @Before
//    public void setUp() throws Exception {
//        MockitoAnnotations.initMocks(this);
//
//        this.builder = new CheckoutPresenterBuilder();
//    }
//
//    @After
//    public void tearDown() throws Exception {
//    }
//
//    @Test
//    public void Presenter_AddProduct_ExpectChargeUpdated() throws Exception {
//        ArrayList<Product> products = new ArrayList<>();
//
//        products.add(new DummyProduct());
//
//        CheckoutPresenter presenter = builder
//                .withProducts(products)
//                .build();
//
//        //act
//        presenter.saveProduct(58);
//
//        //assert
//        builder.verifyView(1).setChargeText("Charge: " + NumberFormat.getCurrencyInstance().format(12.59));
//        builder.verifyView(1).setChargeEnabled(true);
//    }
//
//    @Test
//    public void Presenter_AddManualEntry_ExpectChargeUpdated() throws Exception {
//        CheckoutPresenter presenter = builder
//                .build();
//
//        //act
//        presenter.addManualEntry("manual", new BigDecimal("34.78"));
//
//        //assert
//        builder.verifyView(1).setChargeText("Charge: " + NumberFormat.getCurrencyInstance().format(34.78));
//        builder.verifyView(1).setChargeEnabled(true);
//    }
//
//    @Test
//    public void Presenter_RemoveProduct_ExpectChargeUpdated() throws Exception {
//        ArrayList<Product> products = new ArrayList<>();
//
//        products.add(new DummyProduct());
//
//        CheckoutPresenter presenter = builder
//                .withProducts(products)
//                .build();
//
//        presenter.saveProduct(58);
//
//        //act
//        presenter.removeProduct(new ProductLine(new StockUnit(58, "random", new BigDecimal("12.50")), DateTime.now()));
//
//        //assert
//        builder.verifyView(2).setChargeText("Charge");
//        builder.verifyView(2).setChargeEnabled(false);
//    }
//
//    @Test
//    public void Presenter_RemoveManualEntry_ExpectChargeUpdated() throws Exception {
//        ArrayList<Product> products = new ArrayList<>();
//
//        products.add(new DummyProduct());
//
//        CheckoutPresenter presenter = builder
//                .withProducts(products)
//                .build();
//
//        presenter.addManualEntry("manual", new BigDecimal("34.78"));
//
//        builder.verifyView(1).addReceiptEntry(receiptLineCaptor.capture());
//
//        //act
//        presenter.removeManualEntry((ManualEntryLine) receiptLineCaptor.getValue());
//
//        //assert
//        builder.verifyView(2).setChargeText("Charge");
//        builder.verifyView(2).setChargeEnabled(false);
//    }
//
//    @Test
//    public void Presenter_ClearReceipt_ExpectChargeCleared() throws Exception {
//        ArrayList<Product> products = new ArrayList<>();
//
//        products.add(new DummyProduct());
//
//        CheckoutPresenter presenter = builder
//                .withProducts(products)
//                .build();
//
//        presenter.addManualEntry("manual", new BigDecimal("34.78"));
//        presenter.saveProduct(58);
//
//        //act
//        presenter.clearCurrentReceipt();
//
//        //assert
//        builder.verifyView(2).setChargeText("Charge");
//        builder.verifyView(2).setChargeEnabled(false);
//    }
//
//    @Test
//    public void Presenter_ChargeRequested_ExpectChargeScreenDisplayed() throws Exception {
//        ArrayList<Product> products = new ArrayList<>();
//
//        products.add(new DummyProduct());
//
//        CheckoutPresenter presenter = builder
//                .withProducts(products)
//                .build();
//
//        presenter.addManualEntry("manual", new BigDecimal("34.78"));
//        presenter.saveProduct(58);
//
//        //act
//        presenter.chargeRequested();
//
//        //assert
//        builder.verifyView(1).hideCheckout();
//        builder.verifyView(1).navigateToPaymentChoice();
//        builder.verifyView(1).setReadOnlyReceiptItems(anyCollectionOf(ReceiptLineViewModel.class));
//        builder.verifyView(1).setPaymentValue(new BigDecimal("47.37"));
//        builder.verifyView(1).setChargeText("Record Payment");
//    }
//
//    @Test
//    public void Presenter_BackButtonOnChargeView_ExpectNavigateBackToCheckout() throws Exception {
//        CheckoutPresenter presenter = builder
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
//        CheckoutPresenter presenter = builder
//                .withPaymentsInView()
//                .withPaymentAmount("40.00")
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
//
//    @Test
//    public void ChargeInView_ValueNotCovered_ExpectNoNavigation() throws Exception {
//        CheckoutPresenter presenter = builder
//                .withPaymentsInView()
//                .withPaymentAmount("10.00")
//                .build();
//
//        presenter.addManualEntry("manual", new BigDecimal("34.78"));
//
//        //act
//        presenter.completePayment();
//
//        //assert
//        builder.verifyView(0).hidePaymentChoice();
//        builder.verifyView(0).navigateToPaymentComplete();
//    }
//}