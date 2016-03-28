package clowdtech.mpositive.sync.builder;

import com.clowdtech.data.entities.IProduct;
import com.clowdtech.data.entities.ISyncData;
import com.clowdtech.data.repository.IProductRepository;
import com.clowdtech.data.repository.ISyncRepository;

import org.joda.time.DateTime;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import clowdtech.mpositive.sync.ProductSync;
import clowdtech.mpositive.sync.api.ISyncProductApiService;
import clowdtech.mpositive.sync.api.NewProductsRequest;
import clowdtech.mpositive.sync.api.NewProductsResponse;
import clowdtech.mpositive.sync.api.UpdateProductsRequest;
import clowdtech.mpositive.sync.api.UpdateProductsResponse;
import retrofit.Callback;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;

public class ProductSyncBuilder {
    private final ISyncProductApiService syncService;
    private final ISyncRepository syncRepo;
    IProductRepository productRepo;

    @Captor
    private ArgumentCaptor<Callback<NewProductsResponse>> createCallback;

    public static final List<IProduct> NO_PRODUCTS = new ArrayList<>();

    public ProductSyncBuilder(ISyncProductApiService syncService, ISyncRepository syncRepo) {
        this.syncService = syncService;
        this.syncRepo = syncRepo;

        productRepo = Mockito.mock(IProductRepository.class);

        withNoProductsToCreate();
        withNoProductsToUpdate();
        withLastSync(DateTime.now().minusHours(1));

        MockitoAnnotations.initMocks(this);
    }

    public ProductSync Build() {
        return new ProductSync(productRepo, syncService, syncRepo);
    }

    public ProductSyncBuilder withLastSync(DateTime dateTime) {
        ISyncData lastSync = Mockito.mock(ISyncData.class);

        doReturn(dateTime).when(lastSync).getLastSync();

        doReturn(lastSync).when(syncRepo).getLatestSync();

        return this;
    }

    public ProductSyncBuilder withProductsToCreate(List<IProduct> newProducts) {
        doReturn(newProducts).when(productRepo).getNewProducts();

        return this;
    }

    public ProductSyncBuilder withNoProductsToCreate() {
        doReturn(NO_PRODUCTS).when(productRepo).getNewProducts();

        return this;
    }

    public ProductSyncBuilder withProductsToUpdate(List<IProduct> newProducts) {
        doReturn(newProducts).when(productRepo).getProductsToUpdate(anyLong(), anyLong());

        return this;
    }

    public ProductSyncBuilder withNoProductsToUpdate() {
        doReturn(NO_PRODUCTS).when(productRepo).getProductsToUpdate(anyLong(), anyLong());

        return this;
    }

    public ProductSyncBuilder withLocalProduct() {
        doReturn(new DummyProduct()).when(productRepo).getProduct(anyLong());

        return this;
    }

    public void verifyNoSyncServiceAttempts() {
        Mockito.verifyZeroInteractions(syncService);
    }

    public void verifyNoCreateAttempt() {
        Mockito.verify(syncService, Mockito.never()).createProducts(anyString(), any(NewProductsRequest.class), Matchers.<Callback<NewProductsResponse>>any()); // what is this even testing?!!
    }

    public void verifyCreateAttempt() {
        Mockito.verify(syncService, Mockito.times(1)).createProducts(anyString(), any(NewProductsRequest.class), Matchers.<Callback<NewProductsResponse>>any()); // what is this even testing?!!
    }

    public void verifyNoUpdateAttempt() {
        Mockito.verify(syncService, Mockito.never()).updateProducts(anyString(), any(UpdateProductsRequest.class), Matchers.<Callback<UpdateProductsResponse>>any()); // what is this even testing?!!
    }

    public void verifyUpdateAttempt() {
        Mockito.verify(syncService, Mockito.times(1)).updateProducts(anyString(), any(UpdateProductsRequest.class), Matchers.<Callback<UpdateProductsResponse>>any()); // what is this even testing?!!
    }

    public NewProductsRequest getCreateRequest() {
        ArgumentCaptor<NewProductsRequest> argument = ArgumentCaptor.forClass(NewProductsRequest.class);
        Mockito.verify(syncService).createProducts(anyString(), argument.capture(), Matchers.<Callback<NewProductsResponse>>any());
        return argument.getValue();
    }

    public Callback<NewProductsResponse> getCreateCallback() {
        Mockito.verify(syncService).createProducts(anyString(), any(NewProductsRequest.class), createCallback.capture());
        return createCallback.getValue();
    }

    public UpdateProductsRequest getUpdateRequest() {
        ArgumentCaptor<UpdateProductsRequest> argument = ArgumentCaptor.forClass(UpdateProductsRequest.class);
        Mockito.verify(syncService).updateProducts(anyString(), argument.capture(), Matchers.<Callback<UpdateProductsResponse>>any());
        return argument.getValue();
    }

//    public IProduct getProductSavedLocally() {
//        ArgumentCaptor<Product> argument = ArgumentCaptor.forClass(Product.class);
//        Mockito.verify(productRepo).saveProduct(argument.capture());
//        return argument.getValue();
//    }
}

