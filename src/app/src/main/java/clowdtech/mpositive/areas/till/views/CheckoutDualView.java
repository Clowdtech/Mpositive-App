package clowdtech.mpositive.areas.till.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import clowdtech.mpositive.App;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.shared.ViewPage;
import clowdtech.mpositive.areas.shared.ViewPageAdapter;
import clowdtech.mpositive.areas.till.activities.TillActivity;
import clowdtech.mpositive.areas.till.presenters.CheckoutPresenter;
import clowdtech.mpositive.lifecycle.PresentedView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class CheckoutDualView extends LinearLayout implements CheckoutView, PresentedView, Presentable {

    private InventoryViewImpl inventoryView;
    private CommandPadView padView;

    @Bind(R.id.goToCharge)
    protected Button goToCharge;

    @Bind(R.id.viewPager)
    protected ViewPager viewPager;

    @Bind(R.id.receipt_editable)
    protected EditableReceiptViewImpl receiptView;

    @Inject
    protected CheckoutPresenter presenter;

    public CheckoutDualView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) {
            return;
        }

        ((App) context.getApplicationContext()).getApplicationComponent().inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.bind(this);

        inventoryView = (InventoryViewImpl) View.inflate(getContext(), R.layout.till_checkout_inventory, null);
        padView = (CommandPadView) View.inflate(getContext(), R.layout.till_checkout_command_pad, null);

        initialisePager(viewPager);
    }

    private void initialisePager(ViewPager pager) {
        List<ViewPage> viewPages = new ArrayList<>();

        viewPages.add(new ViewPage(padView, getContext().getString(R.string.till_sections_commands_header)));
        viewPages.add(new ViewPage(inventoryView, getContext().getString(R.string.till_sections_products_header)));

        initialisePager(pager, viewPages);
    }

    private void initialisePager(ViewPager pager, List<ViewPage> frags) {
        ViewPageAdapter pageAdapter = new ViewPageAdapter(frags);

        pager.setAdapter(pageAdapter);

        pager.setCurrentItem(1);
    }

    public void reset() {
        this.presenter.reset();
    }

    @Override
    public void setChargeText(String displayText) {
        goToCharge.setText(displayText);
    }

    @Override
    public void setChargeEnabled(boolean enabled) {
        goToCharge.setEnabled(enabled);
    }

    @Override
    public boolean inventoryHandledBack() {
        return viewPager.getCurrentItem() == 1 && inventoryView.onBackPressed();
    }

    @Override
    public void resetEditableReceipt() {
        receiptView.reset();
    }

    @Override
    public void setOrderReference(String reference) {

    }

    @Override
    public void displayOrderReference() {

    }

    @Override
    public void unbindViews() {
        receiptView.getPresenter().unbindView();
        inventoryView.getPresenter().unbindView();
        padView.getPresenter().unbindView();
    }

    @Override
    public void bindViews() {
        receiptView.bindView();
        inventoryView.bindView();
        padView.bindView();
    }

    public boolean isBackHandled() {
        return this.presenter.isBackHandled();
    }

    @OnClick(R.id.goToCharge)
    public void paymentChoice() {
        presenter.chargeRequested();

        ((TillActivity)getContext()).getContainer().navigateToPaymentChoice();
    }

    @Override
    public void bindView() {
        this.presenter.bindView(this);
    }

    @Override
    public Presenter getPresenter() {
        return this.presenter;
    }
}
