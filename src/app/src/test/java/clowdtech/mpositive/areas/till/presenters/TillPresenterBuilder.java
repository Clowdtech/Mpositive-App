package clowdtech.mpositive.areas.till.presenters;

import com.clowdtech.data.entities.IProduct;
import com.clowdtech.data.entities.IProductTile;
import com.clowdtech.data.repository.ICategoryRepository;
import com.clowdtech.data.repository.IProductRepository;
import com.clowdtech.data.repository.ITransactionsRepository;

import org.joda.time.DateTime;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;

import clowdtech.mpositive.areas.till.TillView;
import clowdtech.mpositive.areas.till.TillPresenter;
import clowdtech.mpositive.data.RunningOrder;
import clowdtech.mpositive.queue.IEventBus;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TillPresenterBuilder {

    @Mock
    TillView mockView;

    @Mock
    IProductRepository mockProductRepo;

    @Mock
    ICategoryRepository mockCategoryRepo;

    @Mock
    ITransactionsRepository mockTransactionRepo;

    @Mock
    IEventBus mockEventBus;

    @Mock
    RunningOrder mockRunningOrder;

    private boolean withInit;

    public TillPresenterBuilder() {
        MockitoAnnotations.initMocks(this);
    }

    public TillPresenter build() {
        TillPresenter presenter = new TillPresenter(mockEventBus, mockRunningOrder);

        presenter.bindView(mockView);

        return presenter;
    }

    public TillView verifyView(int times) {
        return verify(mockView, Mockito.times(times));
    }

    public ITransactionsRepository verifyRepo(int times) {
        return verify(mockTransactionRepo, Mockito.times(times));
    }

    public TillPresenterBuilder withInit() {
        this.withInit = true;

        return this;
    }

    public TillPresenterBuilder withProduct(final int id, final String name, final double price) {
        when(mockProductRepo.getProduct(id)).thenReturn(new IProduct() {
            @Override
            public BigDecimal getPrice() {
                return new BigDecimal(price);
            }

            @Override
            public void setPrice(BigDecimal price) {

            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public Boolean getDeleted() {
                return null;
            }

            @Override
            public int getRemoteId() {
                return 0;
            }

            @Override
            public Long getId() {
                return (long) id;
            }

            @Override
            public double getVat() {
                return 0;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public void setName(String name) {

            }

            @Override
            public void setVat(double vat) {

            }

            @Override
            public void setDescription(String description) {

            }

            @Override
            public void setLastUpdatedDate(DateTime lastUpdate) {

            }

            @Override
            public void setRemoteId(int id) {

            }

            @Override
            public void setDeleted(boolean deleted) {

            }

            @Override
            public void setTile(IProductTile tile) {

            }

            @Override
            public IProductTile getTile() {
                return null;
            }
        });

        return this;
    }

    public TillPresenterBuilder withProducts(ArrayList<IProduct> products) {
        when(mockProductRepo.getProducts()).thenReturn(products);

        return this;
    }

    public TillPresenterBuilder withPaymentsInView() {
//        when(mockView.isPaymentChoiceInView()).thenReturn(true);

        return this;
    }

    public TillPresenterBuilder withPaymentAmount(String value) {
//        when(mockView.getPaymentValue()).thenReturn(new ReceiptPayment(PaymentTypes.Cash, new BigDecimal(value)));

        return this;
    }

//    public SingleContainerPresenterBuilder withNewTransactionId(int tranId) {
//        when(mockTransactionRepo.addTransaction()).thenReturn()
//
//        return this;
//    }
}
