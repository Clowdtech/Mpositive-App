package clowdtech.mpositive.areas.reporting.transaction.presenters;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import clowdtech.mpositive.areas.reporting.transaction.viewModels.TransactionPeriodsViewModel;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;


@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
public class TransactionPeriodsTest {

    @Captor
    ArgumentCaptor<String> stringCaptor;

    @Captor
    ArgumentCaptor<List<TransactionPeriodsViewModel>> captor;

    private TransactionPeriodsPresenterBuilder presenterBuilder;

    @Before
    public void setUp() throws Exception {
        presenterBuilder = new TransactionPeriodsPresenterBuilder();

        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void View_ExpectElevenPeriodsSetOnView() throws Exception {
        //arrange
        presenterBuilder.build();

        //assert
        presenterBuilder.verifyView(1).setLoading();
        presenterBuilder.verifyView(1).setPeriods(captor.capture());
        presenterBuilder.verifyView(1).setLoaded();

        Assert.assertEquals(11, captor.getValue().size());
    }

    @Test
    public void View_WithLastTenCaption_ExpectFirstPeriodCaptionSet() throws Exception {
        //arrange
        String expectedLastTenCaption = "Last 10";

        presenterBuilder
                .setTransactionsRecentString(expectedLastTenCaption)
                .build();

        //assert
        presenterBuilder.verifyView().setPeriods(captor.capture());

        Assert.assertEquals(expectedLastTenCaption, captor.getValue().get(0).getPeriodTitle());
    }

    @Test
    public void View_LastTen_ExpectPeriodCorrectlySet() throws Exception {
        //arrange
        long expectedLowerStamp = 2000;
        long expectedUpperStamp = 4000;

        presenterBuilder
                .withLastTenDateRange(expectedLowerStamp, expectedUpperStamp)
                .build();

        //assert
        presenterBuilder.verifyView().setPeriods(captor.capture());

        Assert.assertEquals(expectedLowerStamp, captor.getValue().get(0).getLowerStamp().longValue());
        Assert.assertEquals(expectedUpperStamp, captor.getValue().get(0).getUpperStamp().longValue());
    }

    @Test
    public void View_WithTodayCaption_ExpectSecondPeriodCaptionSet() throws Exception {
        //arrange
        String expectedTodayCaption = "Today";

        presenterBuilder
                .setTransactionsRecentString(expectedTodayCaption)
                .build();

        //assert
        presenterBuilder.verifyView().setPeriods(captor.capture());

        Assert.assertEquals(expectedTodayCaption, captor.getValue().get(1).getPeriodTitle());
    }

    @Test
    public void View_DatePeriodSelected_ExpectViewSelectionUpdate() throws Exception {
        //arrange
        TransactionPeriodsPresenter presenter = presenterBuilder.build();

        //act
        presenter.periodSelected(3);

        //assert
        presenterBuilder.verifyView().setSelectedPeriod(eq(3));
    }

    @Test
    public void Context_DatePeriodSelected_ExpectRequestForGivenDateRange() throws Exception {
        //arrange
        DateTime lowerStamp = DateTime.now().withTimeAtStartOfDay().minusDays(1);
        DateTime upperStamp = DateTime.now().withTimeAtStartOfDay().minusDays(0);

        TransactionPeriodsPresenter presenter = presenterBuilder
                .withLastTenRange(lowerStamp.getMillis(), upperStamp.getMillis())
                .build();

        //act
        presenter.periodSelected(2);

        //assert
        presenterBuilder.verifyContainer().transactionPeriodSelected(stringCaptor.capture(), eq(lowerStamp.getMillis()), eq(upperStamp.getMillis()));

        assertEquals(String.format("Transactions for %s", lowerStamp.toString("d MMMM yyyy")), stringCaptor.getValue());
    }

    @Test
    public void Context_LastTenPeriodSelected_ExpectRequestWithLastTenHeading() throws Exception {
        //arrange
        TransactionPeriodsPresenter presenter = presenterBuilder
                .setTransactionsRecentString("Last Ten")
                .build();

        //act
        presenter.periodSelected(0);

        //assert
        presenterBuilder.verifyContainer().transactionPeriodSelected(stringCaptor.capture(), anyLong(), anyLong());

        assertEquals("Last Ten", stringCaptor.getValue());
    }

    @Test
    public void View_NoOlderTransactionsAvailable_ExpectViewUpdated() throws Exception {
        //arrange
        TransactionPeriodsPresenter presenter = presenterBuilder
                .olderTransactionsAvailable(false)
                .build();
        
        //act
        presenter.morePeriodsSelected();

        //assert
        presenterBuilder.verifyView(1).setNoMorePeriodsAvailable();
    }

    @Test
    public void View_OlderTransactionsAvailable_ExpectViewUpdatedWithTenItems() throws Exception {
        //arrange
        TransactionPeriodsPresenter presenter = presenterBuilder
                .olderTransactionsAvailable(true)
                .build();

        //act
        presenter.morePeriodsSelected();

        //assert
        presenterBuilder.verifyView(1).addMorePeriods(captor.capture());

        Assert.assertEquals(10, captor.getValue().size());
    }
}