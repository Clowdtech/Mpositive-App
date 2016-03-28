//package clowdtech.mpositive.areas.till.presenters;
//
//import android.content.Context;
//
//import com.clowdtech.data.entities.IProduct;
//import com.clowdtech.data.entities.IProductTransactionLine;
//import com.clowdtech.data.repository.ICategoryRepository;
//import com.clowdtech.data.repository.IProductRepository;
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
//import clowdtech.mpositive.areas.till.DualPaneContainerPresenter;
//import clowdtech.mpositive.data.StockUnit;
//import clowdtech.mpositive.data.lines.ManualEntryLine;
//import clowdtech.mpositive.data.lines.ProductEntryLine;
//import clowdtech.mpositive.sync.builder.DummyProduct;
//
//import static org.mockito.Mockito.when;
//
//
//@RunWith(RobolectricTestRunner.class)
//@Config(manifest= Config.NONE)
//public class DualContainerTest {
//
//    @Mock
//    Context mockContext;
//
//    @Mock
//    IProductRepository mockProductRepo;
//
//    @Mock
//    ICategoryRepository mockCategoryRepo;
//
//    @Captor
//    ArgumentCaptor<String> stringCaptor;
//
//    @Captor
//    ArgumentCaptor<Long> longCaptor;
//
//    @Captor
//    ArgumentCaptor<IProductTransactionLine> productLineCaptor;
//
//    @Captor
//    ArgumentCaptor<ManualEntryLine> manualLineCaptor;
//
//    private DualContainerPresenterBuilder builder;
//
//    @Before
//    public void setUp() throws Exception {
//        MockitoAnnotations.initMocks(this);
//
//        this.builder = new DualContainerPresenterBuilder();
//    }
//
//    @After
//    public void tearDown() throws Exception {
//    }
//
//    @Test
//    public void Presenter_Initialise_ExpectAllViewsInitialised() throws Exception {
//        ArrayList<IProduct> products = new ArrayList<>();
//
//        products.add(new DummyProduct());
//
//        when(mockProductRepo.getProducts()).thenReturn(products);
//
//        DualPaneContainerPresenter presenter = builder
//                .build();
//
//        //act
//        presenter.setItem(mockProductRepo, mockCategoryRepo, transactionRepo);
//
//        //assert
//        builder.verifyView(1).initialiseInventory(mockProductRepo, mockCategoryRepo);
//        builder.verifyView(1).setChargeText("Charge");
//        builder.verifyView(1).setChargeEnabled(false);
//    }
//
//    @Test
//    public void Presenter_AddProduct_ExpectChargeUpdated() throws Exception {
//        ArrayList<IProduct> products = new ArrayList<>();
//
//        products.add(new DummyProduct());
//
//        DualPaneContainerPresenter presenter = builder
//                .withInit()
//                .withProducts(products)
//                .build();
//
//        //act
//        presenter.saveProduct(new ProductEntryLine(new StockUnit(58, "", new BigDecimal("0")), DateTime.now()));
//
//        //assert
//        builder.verifyView(1).setChargeText("Charge: " + NumberFormat.getCurrencyInstance().format(12.59));
//        builder.verifyView(1).setChargeEnabled(true);
//    }
//
//    @Test
//    public void Presenter_AddManualEntry_ExpectChargeUpdated() throws Exception {
//        DualPaneContainerPresenter presenter = builder
//                .withInit()
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
////    @Test
////    public void Presenter_RemoveProduct_ExpectChargeUpdated() throws Exception {
////        ArrayList<IProduct> products = new ArrayList<>();
////
////        products.add(new DummyProduct());
////
////        DualPaneContainerPresenter presenter = builder
////                .withInit()
////                .withProducts(products)
////                .build();
////
////        presenter.saveProduct(new ProductEntryLine(new StockUnit(58, "", new BigDecimal("0")), DateTime.now()));
////
////        //act
////        presenter.removeProduct(new ProductEntryLine(new StockUnit(58, "random", new BigDecimal("12.50")), DateTime.now()));
////
////        //assert
////        builder.verifyView(2).setChargeText("Charge");
////        builder.verifyView(2).setChargeEnabled(false);
////    }
//
//    @Test
//    public void Presenter_RemoveManualEntry_ExpectChargeUpdated() throws Exception {
//        ArrayList<IProduct> products = new ArrayList<>();
//
//        products.add(new DummyProduct());
//
//        DualPaneContainerPresenter presenter = builder
//                .withInit()
//                .withProducts(products)
//                .build();
//
//        presenter.addManualEntry("manual", new BigDecimal("34.78"));
//
//        builder.verifyView(1).addReceiptEntry(manualLineCaptor.capture());
//
//        //act
//        presenter.removeManualEntry(manualLineCaptor.getValue());
//
//        //assert
//        builder.verifyView(2).setChargeText("Charge");
//        builder.verifyView(2).setChargeEnabled(false);
//    }
//
//    @Test
//    public void Presenter_ClearReceipt_ExpectChargeCleared() throws Exception {
//        ArrayList<IProduct> products = new ArrayList<>();
//
//        products.add(new DummyProduct());
//
//        DualPaneContainerPresenter presenter = builder
//                .withInit()
//                .withProducts(products)
//                .build();
//
//        presenter.addManualEntry("manual", new BigDecimal("34.78"));
//        presenter.saveProduct(new ProductEntryLine(new StockUnit(58, "", new BigDecimal("0")), DateTime.now()));
//
//        //act
//        presenter.clearCurrentReceipt();
//
//        //assert
//        builder.verifyView(2).setChargeText("Charge");
//        builder.verifyView(2).setChargeEnabled(false);
//    }
//}