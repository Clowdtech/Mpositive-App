package clowdtech.mpositive.areas.till.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.math.BigDecimal;
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
import clowdtech.mpositive.areas.till.presenters.TenderPresenter;
import clowdtech.mpositive.data.transactions.entities.ReceiptPayment;
import clowdtech.mpositive.ioc.components.ActivityComponent;
import clowdtech.mpositive.ioc.components.ApplicationComponent;
import clowdtech.mpositive.lifecycle.PresentedView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class TenderViewSingle extends RelativeLayout implements TenderView, PresentedView, Presentable {

    private RecordPaymentViewImpl paymentView;
    private ReadOnlyReceiptView roReceiptView;
    private ViewPager viewPager;

    @Bind(R.id.receipt_total)
    protected TextView tillTotal;

    @Inject
    protected TenderPresenter presenter;

    public TenderViewSingle(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) {
            return;
        }

        ActivityComponent activityComponent = ((App) context.getApplicationContext()).getActivityComponent();
        ApplicationComponent applicationComponent = ((App) context.getApplicationContext()).getApplicationComponent();

        presenter = new TenderPresenter(applicationComponent.runningReceipt(), applicationComponent.transactionsRepository());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.bind(this);

        paymentView = (RecordPaymentViewImpl) View.inflate(getContext(), R.layout.till_payment_method, null);
        roReceiptView = (ReadOnlyReceiptView) View.inflate(getContext(), R.layout.shared_receipt_read_only, null);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        initialiseTwoPagePager(viewPager);

        if (isInEditMode()) {
            return;
        }

//        this.presenter.bindView(this);
    }

    private void initialiseTwoPagePager(ViewPager pager) {
        List<ViewPage> viewPages = new ArrayList<>();

        viewPages.add(new ViewPage(roReceiptView, getContext().getString(R.string.till_sections_receipt_header)));
        viewPages.add(new ViewPage(paymentView, getContext().getString(R.string.till_sections_payments_header)));

        initialisePager(pager, viewPages);
    }

    private void initialisePager(ViewPager pager, List<ViewPage> frags) {
        ViewPageAdapter pageAdapter = new ViewPageAdapter(frags);

        pager.setAdapter(pageAdapter);

        pager.setCurrentItem(1);
    }

    @Override
    public boolean isPaymentChoiceInview() {
        return viewPager.getCurrentItem() == 1;
    }

    @Override
    public void setPaymentValue(BigDecimal paymentDefault) {
        this.paymentView.setDefaultValue(paymentDefault);
    }

    @Override
    public ReceiptPayment getPaymentValue() {
        return this.paymentView.getPayment();
    }

    @Override
    public void setReadOnlyReceiptItems(ArrayList<IReceiptLineView> receiptLineViewModels) {
        this.roReceiptView.setData(receiptLineViewModels);
    }

    @Override
    public void displayPaymentComplete(long receiptId) {
        ((TillActivity)getContext()).getContainer().navigateToPaymentComplete(receiptId);
    }

    @Override
    public void setTotal(String total) {
        tillTotal.setText(total);
    }

    @Override
    public void unbindViews() {
        paymentView.getPresenter().unbindView();
        roReceiptView.getPresenter().unbindView();
    }

    @Override
    public void bindViews() {
        paymentView.bindView();
        roReceiptView.bindView();
    }

    public boolean isBackHandled() {
        return this.presenter.isBackHandled();
    }

    @OnClick(R.id.completeTransaction)
    public void completePayment() {
        presenter.completePayment();
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
