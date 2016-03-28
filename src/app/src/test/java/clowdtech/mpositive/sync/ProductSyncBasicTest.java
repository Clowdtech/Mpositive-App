package clowdtech.mpositive.sync;

import com.clowdtech.data.repository.ISyncRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import clowdtech.mpositive.sync.api.ISyncProductApiService;
import clowdtech.mpositive.sync.builder.ProductSyncBuilder;

@RunWith(MockitoJUnitRunner.class)
public class ProductSyncBasicTest {
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
    public void syncNullAccount_NoSyncAttemptMade() throws Exception {
        ProductSync syncer = builder.Build();

        syncer.Sync(null);

        builder.verifyNoSyncServiceAttempts();
    }

    @Test
    public void syncBlankAccount_NoSyncAttemptMade() throws Exception {
        ProductSync syncer = builder.Build();

        syncer.Sync("");

        builder.verifyNoSyncServiceAttempts();
    }

    @Test
    public void syncWithZeroProducts_NoSyncAttemptMade() throws Exception {
        // indicate quite a length of setup, perhaps sync times should be injected
        ProductSync syncer = builder.Build();

        syncer.Sync("666");

        builder.verifyNoSyncServiceAttempts();
    }
}

