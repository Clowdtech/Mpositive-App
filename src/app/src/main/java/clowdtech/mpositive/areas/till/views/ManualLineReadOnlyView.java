package clowdtech.mpositive.areas.till.views;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.text.NumberFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import clowdtech.mpositive.R;

public class ManualLineReadOnlyView implements IReceiptLineView {
    private final DateTime dateCreated;
    private final String name;
    private final String price;

    public ManualLineReadOnlyView(DateTime dateCreated, String name, BigDecimal price) {
        this.dateCreated = dateCreated;
        this.name = name;
        this.price = NumberFormat.getCurrencyInstance().format(price);
    }

    protected class ViewHolder {
        @Bind(R.id.receipt_title)
        public TextView Title;

        @Bind(R.id.receipt_sku_price)
        public TextView Price;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public View getView(LayoutInflater inflater) {
        View vi = inflater.inflate(R.layout.template_receipt_ro_manual, null, false);

        ViewHolder holder = new ViewHolder(vi);

        holder.Title.setText(this.name);
        holder.Price.setText(this.price);

        return vi;
    }

    @Override
    public DateTime getSort() {
        return this.dateCreated;
    }

    @Override
    public Long getIdentifier() {
        return null;
    }
}
