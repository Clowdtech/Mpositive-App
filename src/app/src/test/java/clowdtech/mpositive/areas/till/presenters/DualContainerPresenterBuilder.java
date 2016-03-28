//package clowdtech.mpositive.areas.till.presenters;
//
//import com.clowdtech.data.entities.IProduct;
//import com.clowdtech.data.aa.entities.ProductTile;
//import com.clowdtech.data.repository.ICategoryRepository;
//import com.clowdtech.data.repository.IProductRepository;
//import com.clowdtech.data.repository.ITransactionsRepository;
//
//import org.joda.time.DateTime;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//
//import clowdtech.mpositive.areas.till.IDualPaneContainer;
//
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//public class DualContainerPresenterBuilder {
//
//    @Mock
//    IDualPaneContainer mockView;
//
//    @Mock
//    ITransactionsRepository transactionRepo;
//
//    @Mock
//    IProductRepository mockProductRepo;
//
//    @Mock
//    ICategoryRepository mockCategoryRepo;
//
//    private boolean withInit;
//
//    public DualContainerPresenterBuilder() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    public DualPaneContainerPresenter build() {
//        DualPaneContainerPresenter presenter = new DualPaneContainerPresenter();
//
//        presenter.setView(mockView);
//
//        return presenter;
//    }
//
//    public IDualPaneContainer verifyView(int times) {
//        return verify(mockView, Mockito.times(times));
//    }
//
//    public DualContainerPresenterBuilder withInit() {
//        this.withInit = true;
//
//        return this;
//    }
//
//    public DualContainerPresenterBuilder withProduct(final int id, final String name, final double price) {
//        when(mockProductRepo.getProductId(id)).thenReturn(new IProduct() {
//            @Override
//            public BigDecimal getPrice() {
//                return new BigDecimal(price);
//            }
//
//            @Override
//            public void setPrice(BigDecimal price) {
//
//            }
//
//            @Override
//            public String getName() {
//                return name;
//            }
//
//            @Override
//            public Boolean getDeleted() {
//                return null;
//            }
//
//            @Override
//            public int getRemoteId() {
//                return 0;
//            }
//
//            @Override
//            public Long getId() {
//                return (long) id;
//            }
//
//            @Override
//            public double getVat() {
//                return 0;
//            }
//
//            @Override
//            public String getDescription() {
//                return null;
//            }
//
//            @Override
//            public void setName(String name) {
//
//            }
//
//            @Override
//            public void setVat(double vat) {
//
//            }
//
//            @Override
//            public void setDescription(String description) {
//
//            }
//
//            @Override
//            public void setLastUpdatedDate(DateTime lastUpdate) {
//
//            }
//
//            @Override
//            public void setRemoteId(int id) {
//
//            }
//
//            @Override
//            public void setDeleted(boolean deleted) {
//
//            }
//
//            @Override
//            public void setTile(ProductTile tile) {
//
//            }
//
//            @Override
//            public ProductTile getTile() {
//                return null;
//            }
//        });
//
//        return this;
//    }
//
//    public DualContainerPresenterBuilder withProducts(ArrayList<IProduct> products) {
//        when(mockProductRepo.getProducts()).thenReturn(products);
//
//        return this;
//    }
//}
