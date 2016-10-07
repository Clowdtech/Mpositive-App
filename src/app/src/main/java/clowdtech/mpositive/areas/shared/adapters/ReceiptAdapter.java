package clowdtech.mpositive.areas.shared.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import clowdtech.mpositive.areas.till.views.IReceiptLineView;

public class ReceiptAdapter extends ArrayAdapter<IReceiptLineView> {
    private final LayoutInflater inflater;

    public ReceiptAdapter(Context context, List<IReceiptLineView> values) {
        super(context, 0, values);

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IReceiptLineView model = getItem(position);

        return model.getView(inflater);
    }
}