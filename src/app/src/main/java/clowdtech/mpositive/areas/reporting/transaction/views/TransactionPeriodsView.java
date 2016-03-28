package clowdtech.mpositive.areas.reporting.transaction.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import clowdtech.mpositive.App;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.reporting.transaction.Container;
import clowdtech.mpositive.areas.reporting.transaction.activities.TransactionsReporting;
import clowdtech.mpositive.areas.reporting.transaction.adapters.TransactionPeriodsArrayAdapter;
import clowdtech.mpositive.areas.reporting.transaction.presenters.TransactionPeriodsPresenter;
import clowdtech.mpositive.areas.reporting.transaction.viewModels.TransactionPeriodsViewModel;
import clowdtech.mpositive.lifecycle.PresentedView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class TransactionPeriodsView extends RelativeLayout implements ITransactionPeriodsView, Presentable, PresentedView {
    private TransactionPeriodsArrayAdapter adapter;

    @Inject
    protected TransactionPeriodsPresenter presenter;

    private Button btnLoadExtra;

    @Bind(R.id.reporting_days_list)
    protected ListView listView;

    @Bind(R.id.loading_indicator)
    protected ProgressBar loading;

    public TransactionPeriodsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) {
            return;
        }

        ((App)context.getApplicationContext()).getReportingComponent().inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.bind(this);

        // TODO: move this into a view

        // button in view ??
        btnLoadExtra = new Button(getContext());
        btnLoadExtra.setText(getContext().getString(R.string.load_more_button_caption));

        // footer in view ??
        listView.addFooterView(btnLoadExtra);

        btnLoadExtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                presenter.morePeriodsSelected();
            }
        });
    }

    @Override
    public void setSelectedPeriod(int position) {
        listView.setItemChecked(position, true);
    }

    @Override
    public void setPeriods(List<TransactionPeriodsViewModel> periodViewModels) {
        adapter = new TransactionPeriodsArrayAdapter(getContext(), periodViewModels);

        listView.setAdapter(adapter);
    }

    @Override
    public void addMorePeriods(List<TransactionPeriodsViewModel> data) {
        adapter.AddMoreViewModels(data);
    }

    @Override
    public void setNoMorePeriodsAvailable() {
        if (listView.getFooterViewsCount() > 0)
            listView.removeFooterView(btnLoadExtra);
    }

    @Override
    public void setLoading() {
        loading.setVisibility(VISIBLE);
    }

    @Override
    public void setLoaded() {
        loading.setVisibility(GONE);
    }

    @Override
    public Container getContainer() {
        return ((TransactionsReporting)getContext()).getContainer();
    }

    @Override
    public Presenter getPresenter() {
        return this.presenter;
    }

    @OnItemClick(R.id.reporting_days_list)
    public void periodItemClick(int position) {
        this.presenter.periodSelected(position);
    }

    @Override
    public void bindView() {
        this.presenter.bindView(this);
    }
}
