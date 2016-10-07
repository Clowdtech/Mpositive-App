package clowdtech.mpositive.areas.reporting.transaction.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import clowdtech.mpositive.App;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.reporting.transaction.activities.TransactionsReporting;
import clowdtech.mpositive.areas.reporting.transaction.adapters.TransactionSummaryAdapter;
import clowdtech.mpositive.areas.reporting.transaction.Container;
import clowdtech.mpositive.areas.reporting.transaction.presenters.TransactionSummaryPresenter;
import clowdtech.mpositive.areas.reporting.transaction.viewModels.TransactionListItemViewModel;
import clowdtech.mpositive.lifecycle.PresentedView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class TransactionSummaryView extends RelativeLayout implements ITransactionSummaryView, ITransactionSummaryComponent, Presentable, PresentedView {

    private TextView reportHeading;
    private ListView listView;
    private TextView averageText;
    private TextView cardText;
    private TextView cashText;
    private TextView otherText;
    private TextView totalTransText;
    private Button exportBtn;
    private Button shareBtn;

    @Inject
    protected TransactionSummaryPresenter presenter;

    public TransactionSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) {
            return;
        }

        ((App)context.getApplicationContext()).getReportingComponent().inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        listView = (ListView) findViewById(R.id.frag_reporting_receipts_items_list);

        reportHeading = (TextView) findViewById(R.id.frag_reporting_receipts_header);

        averageText = (TextView) findViewById(R.id.transaction_summary_average_value);
        cardText = (TextView) findViewById(R.id.frag_reporting_receipts_card_txt);
        cashText = (TextView) findViewById(R.id.frag_reporting_receipts_cash_txt);
        otherText = (TextView) findViewById(R.id.frag_reporting_receipts_other_txt);
        totalTransText = (TextView) findViewById(R.id.frag_reporting_receipts_total_txt);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.transactionSelected(position, id);
            }
        });

        exportBtn = (Button) findViewById(R.id.reporting_print);

        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                presenter.printTransactions();
            }
        });

        shareBtn = (Button) findViewById(R.id.reporting_share);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                presenter.shareTransactions();
            }
        });
    }

    @Override
    public void setHeading(String reportHeading) {
        this.reportHeading.setText(reportHeading);
    }

    @Override
    public void setItems(List<TransactionListItemViewModel> transactions) {
        TransactionSummaryAdapter adapter = new TransactionSummaryAdapter(getContext(), transactions);

        this.listView.setAdapter(adapter);
    }

    @Override
    public void setCardTotal(String total) {
        this.cardText.setText(total);
    }

    @Override
    public void setCashTotal(String total) {
        this.cashText.setText(total);
    }

    @Override
    public void setOtherTotal(String total) {
        this.otherText.setText(total);
    }

    @Override
    public void setTransactionTotal(String total) {
        this.totalTransText.setText(total);
    }

    @Override
    public void setPrintEnabled(boolean enabled) {
        exportBtn.setEnabled(enabled);
    }

    @Override
    public void setSelectedTransaction(int position) {
        listView.setItemChecked(position, true);
    }

    @Override
    public void setAverageTransaction(String averageTransactionValue) {
        this.averageText.setText(averageTransactionValue);
    }

    @Override
    public void setShareEnabled(Boolean enabled) {
        shareBtn.setEnabled(enabled);
    }

    @Override
    public Container getContainer() {
        return ((TransactionsReporting)getContext()).getContainer();
    }

    @Override
    public void setData(String header, long lowerStamp, long upperStamp) {
        this.presenter.setData(header, lowerStamp, upperStamp);
    }

    @Override
    public Presenter getPresenter() {
        return this.presenter;
    }

    @Override
    public void bindView() {
        this.presenter.bindView(this);
    }
}
