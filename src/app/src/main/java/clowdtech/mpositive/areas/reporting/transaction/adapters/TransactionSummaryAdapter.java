package clowdtech.mpositive.areas.reporting.transaction.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.reporting.transaction.viewModels.TransactionListItemViewModel;

public class TransactionSummaryAdapter extends BaseAdapter {
    private List<TransactionListItemViewModel> _originalData;
    private final LayoutInflater _inflater;
    private int _unselectedTextColour;
    private int _refundedTextColour;

    public TransactionSummaryAdapter(Context context, List<TransactionListItemViewModel> _originalData) {
        this._originalData = _originalData;

        _inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Resources _resources = context.getResources();

        _refundedTextColour = Color.RED;
        _unselectedTextColour = _resources.getColor(R.color.font_primary);
    }

    public int getCount() {
        return _originalData.size();
    }

    public Object getItem(int position) {
        return _originalData.get(position);
    }

    public long getItemId(int position) {
        return _originalData.get(position).getId();
    }

    public static class ViewHolder {
        public TextView text;
        public TextView total;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            vi = _inflater.inflate(R.layout.reporting_transaction_row_template, parent, false);

            holder = new ViewHolder();
            holder.text = (TextView) vi.findViewById(R.id.reporting_row_text);
            holder.total = (TextView) vi.findViewById(R.id.reporting_row_value);

            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        TransactionListItemViewModel sku = _originalData.get(position);
        setViewStyle(holder, sku.isRefunded());

        holder.text.setText(sku.getTitle());
        holder.total.setText(sku.getInfo());

        return vi;
    }

    private void setViewStyle(ViewHolder holder, Boolean refunded) {
        // set style of unselected but refunded
        if (refunded) {
            holder.text.setTextColor(_refundedTextColour);
            holder.total.setTextColor(_refundedTextColour);

            return;
        }

        // set style of unselected
        holder.text.setTextColor(_unselectedTextColour);
        holder.total.setTextColor(_unselectedTextColour);
    }
}

