package clowdtech.mpositive.report;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import clowdtech.mpositive.R;
import clowdtech.mpositive.TaskListener;
import clowdtech.mpositive.areas.reporting.product.viewModels.ProductReportingItemViewModel;
import clowdtech.printer.ProductReportFormatInfo;

public class ProductReportPrinter {
    private final Context context;

    public ProductReportPrinter(Context context) {
        this.context = context;
    }

    public void printReport(String reportHeading, List<ProductReportingItemViewModel> data, TaskListener listener) {
        Resources resources = this.context.getResources();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.context);

        String[] headers = new String[] { resources.getString(R.string.reporting_products_col_grouping), resources.getString(R.string.reporting_products_col_figures) };

        ArrayList<String[]> rows = new ArrayList<>();

        for (ProductReportingItemViewModel model : data) {
            String[] values = new String[2];
            values[0] = model.Name;
            values[1] = String.format("%s (%s)", Integer.toString(model.Quantity), model.TotalValue);
            rows.add(values);
        }

        String footer = prefs.getString(resources.getString(R.string.preference_receipt_footer), "");

        ProductReportFormatInfo formatInfo = new ProductReportFormatInfo(reportHeading, headers, rows, footer);

        PrintProductsReportAsync task = new PrintProductsReportAsync(this.context, formatInfo, listener);

        task.execute();
    }
}

