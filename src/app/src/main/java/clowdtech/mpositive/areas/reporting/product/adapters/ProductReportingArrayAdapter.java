package clowdtech.mpositive.areas.reporting.product.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.reporting.product.viewModels.ProductReportingItemViewModel;

public class ProductReportingArrayAdapter  extends BaseAdapter {
    private final List<ProductReportingItemViewModel> viewModel;
    private final LayoutInflater _inflater;
    private final NumberFormat currencyFormatter;

    public ProductReportingArrayAdapter(Context context, List<ProductReportingItemViewModel> viewModel) {
        this.viewModel = viewModel;
        _inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        currencyFormatter = NumberFormat.getCurrencyInstance();
    }

    @Override
    public int getCount() {

        return viewModel.size();
    }

    @Override
    public Object getItem(int position) {

        return viewModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return viewModel.get(position).Id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {
            vi = _inflater.inflate(R.layout.product_reporting_row_template, parent, false);
            holder = new ViewHolder(vi);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        ProductReportingItemViewModel viewModel = this.viewModel.get(position);
        holder.nameText.setText(viewModel.Name);
        holder.quantityText.setText(String.format("%s (%s)", Integer.toString(viewModel.Quantity), currencyFormatter.format(viewModel.TotalValue)));

        return vi;
    }

    public static class ViewHolder {
        @Bind(R.id.product_reporting_row_name)
        public TextView nameText;

        @Bind(R.id.product_reporting_row_quantity)
        public TextView quantityText;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
