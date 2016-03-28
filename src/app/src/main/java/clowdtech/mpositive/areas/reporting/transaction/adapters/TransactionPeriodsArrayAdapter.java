package clowdtech.mpositive.areas.reporting.transaction.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.reporting.transaction.viewModels.TransactionPeriodsViewModel;

public class TransactionPeriodsArrayAdapter extends BaseAdapter {

    private List<TransactionPeriodsViewModel> periodViewModels;
    private final LayoutInflater _inflater;

    public TransactionPeriodsArrayAdapter(Context context, List<TransactionPeriodsViewModel> periodViewModels) {
        this.periodViewModels = periodViewModels;

        _inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void AddMoreViewModels(List<TransactionPeriodsViewModel> moreViewModels) {
        periodViewModels.addAll(moreViewModels);
        notifyDataSetChanged();
    }


    public static class ViewHolder {
        public TextView text;
        public TransactionPeriodsViewModel model;
    }

    @Override
    public int getCount() {
        return periodViewModels.size();
    }

    @Override
    public Object getItem(int position) {
        return periodViewModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {
            vi = _inflater.inflate(R.layout.template_reporting_day_row, parent, false);
            holder = new ViewHolder();
            holder.text = (TextView) vi.findViewById(R.id.reporting_day_row_text);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.model = periodViewModels.get(position);

        holder.text.setText(holder.model.getPeriodTitle());

        return vi;
    }
}

