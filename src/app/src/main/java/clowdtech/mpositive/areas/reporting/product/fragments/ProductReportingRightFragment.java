package clowdtech.mpositive.areas.reporting.product.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import clowdtech.mpositive.R;
import clowdtech.mpositive.TaskListener;
import clowdtech.mpositive.areas.reporting.product.adapters.ProductReportingArrayAdapter;
import clowdtech.mpositive.areas.reporting.product.viewModels.ProductReportingItemViewModel;
import clowdtech.mpositive.report.ProductReportPrinter;
import clowdtech.mpositive.ui.fragments.BaseFragment;

public class ProductReportingRightFragment extends BaseFragment {
    private List<ProductReportingItemViewModel> viewModel;

    private String reportHeading;

    public static ProductReportingRightFragment newInstance() {
        return new ProductReportingRightFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_reporting_rightside_fragment, container, false);

        ListView _listView = (ListView) view.findViewById(R.id.product_reporting_list);

        _listView.setClickable(false);

        TextView header = (TextView)view.findViewById(R.id.report_products_heading);

        header.setText(reportHeading);

        ProductReportingArrayAdapter _adapter = new ProductReportingArrayAdapter(getActivity(), viewModel);

        _listView.setAdapter(_adapter);

        Button exportBtn = (Button) view.findViewById(R.id.reporting_print);

        if (this.viewModel.size() > 0) {
            exportBtn.setEnabled(true);
            exportBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    new ProductReportPrinter(getActivity()).printReport(reportHeading, viewModel, new TaskListener() {
                        @Override
                        public void onFinished(boolean success) {
                        }
                    });
                }
            });
        } else {
            exportBtn.setEnabled(false);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //trackScreenView(TrackingConstants.ScreenNames.ReportProductSummary);
    }

    public void setHeading(String reportHeading) {
        this.reportHeading = reportHeading;
    }

    public void setViewModel(List<ProductReportingItemViewModel> viewModel) {
        this.viewModel = viewModel;
    }
}
