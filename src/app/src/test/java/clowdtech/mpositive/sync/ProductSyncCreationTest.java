package clowdtech.mpositive.sync;

import com.clowdtech.data.entities.IProduct;
import com.clowdtech.data.repository.ISyncRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import clowdtech.mpositive.sync.api.ISyncProductApiService;
import clowdtech.mpositive.sync.api.NewProductsRequest;
import clowdtech.mpositive.sync.builder.DummyProduct;
import clowdtech.mpositive.sync.builder.ProductSyncBuilder;

import static junit.framework.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ProductSyncCreationTest {
    ProductSyncBuilder builder;

    @Mock
    ISyncProductApiService syncService;

    @Mock
    ISyncRepository syncRepo;

    @Before
    public void setUp() throws Exception {
        builder = new ProductSyncBuilder(syncService, syncRepo);
    }

    @After
    public void tearDown() throws Exception {

    }

    // these tests are not going as far as integration with the local sql db for new products logic
    // really need an integration piece for this

    @Test
    public void syncWithProductsToCreate_OnlyCreateAttemptMade() throws Exception {
        List<IProduct> newProducts = new ArrayList<>();

        IProduct newProduct = Mockito.mock(IProduct.class);

        newProducts.add(newProduct);

        ProductSync syncer = builder
                .withProductsToCreate(newProducts)
                .Build();

        syncer.Sync("666");

        builder.verifyCreateAttempt();
        builder.verifyNoUpdateAttempt();
    }

    @Test
    public void syncWithProductsToCreate_AttemptCarriesCorrectRequest() throws Exception {
        List<IProduct> newProducts = new ArrayList<>();

        IProduct newProduct = new DummyProduct();

        newProducts.add(newProduct);

        ProductSync syncer = builder
                .withProductsToCreate(newProducts)
                .Build();

        syncer.Sync("666");

        NewProductsRequest request = builder.getCreateRequest();

        assertEquals(newProduct.getId().longValue(), request.products.get(0).ExternalId);
    }

//    @Test
//    public void syncWithProductsToCreate_ResponseUpdatesLocal() throws Exception {
//        List<IProduct> newProducts = new ArrayList<>();
//
//        IProduct newProduct = new DummyProduct();
//
//        newProducts.add(newProduct);
//
//        ProductSync syncer = builder
//                .withProductsToCreate(newProducts)
//                .withLocalProduct()
//                .Build();
//
//        syncer.Sync("666");
//
//        Callback<NewProductsResponse> callback = builder.getCreateCallback();
//
//        NewProductsResponse createResponse = new NewProductsResponse();
//
//        createResponse.newProductResponses = new ArrayList<>();
//
//        NewProductResponse productResponse = new NewProductResponse();
//
//        productResponse.id = 76;
//
//        createResponse.newProductResponses.add(productResponse);
//
//        callback.success(createResponse, new Response("", 200, "", new ArrayList<Header>(), null));
//
//        IProduct productSave = builder.getProductSavedLocally();
//
//        assertEquals(productSave.getRemoteId(), 76);
//    }
}

