package clowdtech.mpositive.areas.till.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

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
import clowdtech.mpositive.areas.shared.views.ReadOnlyReceiptView;
import clowdtech.mpositive.areas.till.activities.TillActivity;
import clowdtech.mpositive.areas.till.presenters.PaymentCompletePresenter;
import clowdtech.mpositive.ioc.components.CheckoutComponent;
import clowdtech.mpositive.lifecycle.PresentedView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class PaymentCompleteSingleView extends RelativeLayout implements IPaymentCompleteView, PresentedView, Presentable {

    private TransactionCompleteViewImpl completeView;
    private ReadOnlyReceiptView roReceiptView;

    @Inject
    protected PaymentCompletePresenter presenter;

    @Bind(R.id.tender_print_container)
    protected View printReceipt;

    public PaymentCompleteSingleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) {
            return;
        }

        CheckoutComponent checkoutComponent = ((App) context.getApplicationContext()).getCheckoutComponent();

        checkoutComponent.inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.bind(this);

        completeView = (TransactionCompleteViewImpl) View.inflate(getContext(), R.layout.till_complete_message, null);
        roReceiptView = (ReadOnlyReceiptView) View.inflate(getContext(), R.layout.shared_receipt_read_only, null);

        initialiseTwoPagePager();
    }

    private void initialiseTwoPagePager() {
        List<ViewPage> viewPages = new ArrayList<>();

        viewPages.add(new ViewPage(roReceiptView, getContext().getString(R.string.till_sections_receipt_header)));
        viewPages.add(new ViewPage(completeView, getContext().getString(R.string.till_sections_complete_header)));

        initialisePager(viewPages);
    }

    private void initialisePager(List<ViewPage> frags) {
        ViewPageAdapter pageAdapter = new ViewPageAdapter(frags);

        ViewPager pager = ButterKnife.findById(this, R.id.viewPager);

        pager.setAdapter(pageAdapter);

        pager.setCurrentItem(1);
    }

    public void initialise(long receiptId) {
        this.presenter.setItem(receiptId);
    }

    public boolean isBackHandled() {
        return this.presenter.isBackHandled();
    }

    public void setReadOnlyReceiptItems(ArrayList<IReceiptLineView> receiptLineViewModels) {
        this.roReceiptView.setData(receiptLineViewModels);
    }

    @Override
    public void completeCheckout() {
        ((TillActivity)getContext()).getContainer().navigateToCheckout();
    }

    @Override
    public void unbindViews() {
        roReceiptView.getPresenter().unbindView();
        completeView.getPresenter().unbindView();
    }

    @Override
    public void bindViews() {
        roReceiptView.bindView();
        completeView.bindView();
    }

    @Override
    public void showPrintOption() {
        printReceipt.setVisibility(VISIBLE);
    }

    @Override
    public void hidePrintOption() {
        printReceipt.setVisibility(GONE);
    }

    @Override
    public void setPaymentValue(String total) {
    }

    @OnClick(R.id.tender_print_receipt)
    public void printReceipt() {
        this.presenter.printReceipt();
    }

    @OnClick(R.id.tender_no_receipt)
    public void noReceiptClick() {
        this.presenter.noReceipt();
    }

    @Override
    public void bindView() {
        presenter.bindView(this);
    }

    @Override
    public Presenter getPresenter() {
        return this.presenter;
    }
}
