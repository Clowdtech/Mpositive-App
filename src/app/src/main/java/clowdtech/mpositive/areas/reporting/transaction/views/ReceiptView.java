package clowdtech.mpositive.areas.reporting.transaction.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import clowdtech.mpositive.App;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.reporting.transaction.presenters.ReceiptPresenter;
import clowdtech.mpositive.areas.shared.views.ReadOnlyReceiptView;
import clowdtech.mpositive.areas.till.views.IReceiptLineView;
import clowdtech.mpositive.lifecycle.PresentedView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class ReceiptView extends RelativeLayout implements IReceiptView, Presentable, PresentedView {

    @Inject
    protected ReceiptPresenter presenter;

    private TextView totalHeading;
//    private ProgressBar emailProgress;
    private ProgressBar printProgress;
    private Button refundButton;
    private Button printReceipt;
    private TextView lineCountText;
    private TextView totalInfo;
    private TextView paidInfo;
    private TextView paidLabel;
    private TextView changeInfo;
    private ReadOnlyReceiptView receiptLines;

    public ReceiptView(Context context, AttributeSet attrs) {
        super(context, attrs);

        ((App)context.getApplicationContext()).getReportingComponent().inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        totalHeading = (TextView) findViewById(R.id.receipt_total_value);

        receiptLines = (ReadOnlyReceiptView) findViewById(R.id.receipt_lines);

        lineCountText = (TextView) findViewById(R.id.receipt_info_number_items);

        totalInfo = (TextView) findViewById(R.id.receipt_total_second_value);

        paidInfo = (TextView) findViewById(R.id.receipt_tender_amount);

        paidLabel = (TextView) findViewById(R.id.receipt_payment_type);

        changeInfo = (TextView) findViewById(R.id.receipt_change_due);

//        emailProgress = (ProgressBar) view.findViewById(R.id.reporting_email_progress);
//
//

//
//        emailReceipt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                emailReceipt(_transactionId);
//            }
//        });

        refundButton = (Button) findViewById(R.id.receipt_refund);

        refundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.refundReceipt();
            }
        });

        printProgress = (ProgressBar) findViewById(R.id.reporting_print_progress);

        printReceipt = (Button) findViewById(R.id.reporting_print);

        printReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.printReceipt();
            }
        });
    }

    public void setData(long receiptId) {
        this.presenter.setData(receiptId);
    }

    @Override
    public void resetRefunded() {
        refundButton.setEnabled(true);
        refundButton.setText(getResources().getString(R.string.captions_receipt_refund));
        totalHeading.setTextColor(getResources().getColor(R.color.font_primary));
    }

    @Override
    public void setRefunded() {
        refundButton.setEnabled(false);
        refundButton.setText(getResources().getString(R.string.captions_receipt_refunded));
        totalHeading.setTextColor(Color.RED);
    }

    @Override
    public void setTotalHeading(String receiptTotal) {
        this.totalHeading.setText(receiptTotal);
    }

    @Override
    public void setLineCount(String lineCount) {
        this.lineCountText.setText(lineCount);
    }

    @Override
    public void setTotalInfo(String totalValue) {
        this.totalInfo.setText(totalValue);
    }

    @Override
    public void setPaidInfo(String receiptPaid) {
        this.paidInfo.setText(receiptPaid);
    }

    @Override
    public void setPaidLabel(String paidLabel) {
        this.paidLabel.setText(paidLabel);
    }

    @Override
    public void setChangeInfo(String receiptChange) {
        this.changeInfo.setText(receiptChange);
    }

    @Override
    public void setPrinting() { // split up?!
        this.printReceipt.setText("");
        this.printProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPrinted() {
        this.printProgress.setVisibility(View.GONE);
        this.printReceipt.setText(getResources().getString(R.string.captions_tender_print_receipt));
    }

    @Override
    public void setLines(ArrayList<IReceiptLineView> lineModels) {
        this.receiptLines.setData(lineModels);
    }

    @Override
    public void bindViews() {
        receiptLines.bindView();
    }

    @Override
    public void unbindViews() {
        receiptLines.getPresenter().unbindView();
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
