package clowdtech.mpositive.areas.till.presenters;

import android.content.Context;
import android.content.res.Resources;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import clowdtech.mpositive.ISharedPreferences;
import clowdtech.mpositive.ITracker;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.till.views.EditableReceiptView;
import clowdtech.mpositive.areas.till.views.IReceiptLineView;
import clowdtech.mpositive.data.RunningOrder;
import clowdtech.mpositive.queue.IEventBus;
import clowdtech.mpositive.queue.events.OrderClearEvent;
import clowdtech.mpositive.queue.events.OrderSaveEvent;
import clowdtech.mpositive.receipt.IOrderExporter;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class EditableReceiptPresenterBuilder {
    @Mock
    RunningOrder mockRunningOrder;

    @Mock
    IEventBus mockEventBus;

    @Mock
    ITracker mockTracker;

    @Mock
    IOrderExporter mockOrderExporter;

    @Mock
    ISharedPreferences mockSharedPreferences;

    @Mock
    EditableReceiptView mockView;

    @Mock
    Context mockContext;

    @Mock
    Resources mockResources;

    @Captor
    ArgumentCaptor<ArrayList<IReceiptLineView>> lineItemsCaptor;

    public EditableReceiptPresenterBuilder() {
        MockitoAnnotations.initMocks(this);

        doReturn(mockContext).when(mockView).getContext();
        doReturn(mockResources).when(mockContext).getResources();
        doReturn(20).when(mockResources).getInteger(R.integer.receipt_max_character);
    }

    public EditableReceiptPresenter build() {
        return new EditableReceiptPresenter(mockRunningOrder, mockEventBus, mockTracker, mockOrderExporter, mockSharedPreferences);
    }

    public EditableReceiptPresenter buildAndBind() {
        EditableReceiptPresenter presenter = build();

        presenter.bindView(mockView);

        return presenter;
    }

    public EditableReceiptPresenterBuilder printerIntegration(boolean enabled) {
        doReturn(enabled).when(mockSharedPreferences).getPrinterIntegration();

        return this;
    }

    public void verifyPrinterIntegrationEnabled() {
        verify(mockView, times(1)).showPrintOrder();
    }

    public void verifyPrinterIntegrationDisabled() {
        verify(mockView, times(1)).hidePrintOrder();
    }

    public void verifyEventForOrderClear() {
        verify(mockEventBus, times(1)).post(any(OrderClearEvent.class));
    }

    public void verifyViewReset(int times) {
        verify(mockView, times(times)).setReceiptLines(anyListOf(IReceiptLineView.class));
    }

    public void verifyEventForOrderSave() {
        verify(mockEventBus, times(1)).post(any(OrderSaveEvent.class));
    }
}
