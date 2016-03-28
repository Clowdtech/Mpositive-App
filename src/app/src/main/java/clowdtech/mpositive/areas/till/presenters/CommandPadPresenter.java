package clowdtech.mpositive.areas.till.presenters;

import java.math.BigDecimal;
import java.text.NumberFormat;

import javax.inject.Inject;

import clowdtech.mpositive.areas.till.views.CommandPadView;
import clowdtech.mpositive.queue.IEventBus;
import clowdtech.mpositive.queue.events.RunningReceiptAddManualEntryEvent;
import clowdtech.mpositive.ui.BasePresenter;

public class CommandPadPresenter extends BasePresenter<CommandPadView> {
    private final NumberFormat currencyFormatter;

    private BigDecimal currentValue;

    private IEventBus eventBus;

    @Inject
    public CommandPadPresenter(IEventBus eventBus) {
        this.eventBus = eventBus;

        currencyFormatter = NumberFormat.getCurrencyInstance();

        currentValue = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_UNNECESSARY);
    }

    @Override
    public void bindView(CommandPadView view) {
        super.bindView(view);

        this.view.setSubTotal(this.currencyFormatter.format(currentValue)); // current value of control leaking out?!

        eventBus.register(this);
    }

    @Override
    public void unbindView() {
        super.unbindView();

        eventBus.unregister(this);
    }

    public void commitSubTotal() {
        if (currentValue.equals(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_UNNECESSARY)))
            return;

        String note = this.view.getNote();

        eventBus.post(new RunningReceiptAddManualEntryEvent(note, currentValue));

        // untidy logic here - duplicated in the numeric entry control that also deals with zero resets
        currentValue = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_UNNECESSARY);

        this.view.setNote("");
        this.view.setSubTotal(this.currencyFormatter.format(0.00));
        this.view.resetNumericEntry();
    }

    public void entryValueChanged(BigDecimal entry) {
        this.currentValue = entry;

        this.view.setSubTotal(this.currencyFormatter.format(entry));
    }
}
