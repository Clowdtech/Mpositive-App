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
import clowdtech.mpositive.sync.api.UpdateProductsRequest;
import clowdtech.mpositive.sync.builder.DummyProduct;
import clowdtech.mpositive.sync.builder.ProductSyncBuilder;

import static junit.framework.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ProductSyncUpdateTest {
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

    @Test
    public void syncWithProductsToUpdate_OnlyUpdateAttemptMade() throws Exception {
        List<IProduct> newProducts = new ArrayList<>();

        IProduct newProduct = Mockito.mock(IProduct.class);

        newProducts.add(newProduct);

        ProductSync syncer = builder
                .withProductsToUpdate(newProducts)
                .Build();

        syncer.Sync("666");

        builder.verifyUpdateAttempt();
        builder.verifyNoCreateAttempt();
    }

    @Test
    public void syncWithProductsToUpdate_AttemptCarriesCorrectRequest() throws Exception {
        List<IProduct> newProducts = new ArrayList<>();

        IProduct newProduct = new DummyProduct();

        newProducts.add(newProduct);

        ProductSync syncer = builder
                .withProductsToUpdate(newProducts)
                .Build();

        syncer.Sync("666");

        UpdateProductsRequest request = builder.getUpdateRequest();

        assertEquals(newProduct.getName(), request.updateProductRequests.get(0).name);
        assertEquals(newProduct.getId().longValue(), request.updateProductRequests.get(0).externalId); // shouldn't really need this
        assertEquals(newProduct.getRemoteId(), request.updateProductRequests.get(0).id);
        assertEquals(newProduct.getDeleted(), request.updateProductRequests.get(0).deleted);
    }
}

