package clowdtech.mpositive.areas.till.presenters;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import clowdtech.mpositive.queue.events.RunningReceiptClearedEvent;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class EditableReceiptPresenterTest {
    EditableReceiptPresenterBuilder presenterBuilder;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        presenterBuilder = new EditableReceiptPresenterBuilder();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void printer_integration_enabled_ensure_show_option_on_bind_view() {
        presenterBuilder
                .printerIntegration(true)
                .buildAndBind();

        presenterBuilder.verifyPrinterIntegrationEnabled();
    }

    @Test
    public void printer_integration_disabled_ensure_show_option_on_bind_view() {
        presenterBuilder
                .printerIntegration(false)
                .buildAndBind();

        presenterBuilder.verifyPrinterIntegrationDisabled();
    }

    @Test
    public void clear_order_expect_event_raised() {
        EditableReceiptPresenter presenter = presenterBuilder
                .build();

        presenter.clearRunningReceipt();

        presenterBuilder.verifyEventForOrderClear();
    }

    @Test
    public void order_cleared_expect_view_reset() {
        EditableReceiptPresenter presenter = presenterBuilder
                .buildAndBind();

        presenter.subscribeReceiptCleared(new RunningReceiptClearedEvent());

        presenterBuilder.verifyViewReset(2);
    }

//    @Test
//    public void order_save_expect_view_reset() {
//        EditableReceiptPresenter presenter = presenterBuilder
//                .buildAndBind();
//
//        presenter.saveOrder("dummy ref");
//
//        presenterBuilder.verifyEventForOrderSave();
//    }
}
