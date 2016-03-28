package clowdtech.mpositive.areas.reporting.transaction.presenters;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;


@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
public class ReceiptPresenterTest {

    private ReceiptPresenterBuilder presenterBuilder;

    @Captor
    ArgumentCaptor<Long> longCaptor;

    @Before
    public void setUp() throws Exception {
        presenterBuilder = new ReceiptPresenterBuilder();

        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void Repo_ExpectCallForTransaction() throws Exception {
        //arrange
        long expectedTransactionId = 3000;

        ReceiptPresenter presenter = presenterBuilder
                .build();

        //act
        presenter.setData(expectedTransactionId);

        //assert
        presenterBuilder.verifyRepo(1).getTransaction(longCaptor.capture());

        assertEquals(expectedTransactionId, longCaptor.getValue().longValue());
    }
}