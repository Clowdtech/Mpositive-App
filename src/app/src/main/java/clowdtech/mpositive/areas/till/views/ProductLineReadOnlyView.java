package clowdtech.mpositive.areas.till.views;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.clowdtech.data.entities.ITransactionLineProduct;

import org.joda.time.DateTime;

import java.text.NumberFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import clowdtech.mpositive.R;

public class ProductLineReadOnlyView implements IReceiptLineView {
    private final NumberFormat currencyFormatter;

    private ITransactionLineProduct model;

    public ProductLineReadOnlyView(ITransactionLineProduct line) {
        this.model = line;

        this.currencyFormatter = NumberFormat.getCurrencyInstance();
    }

    protected class ViewHolder {
        @Bind(R.id.receipt_title)
        public TextView Title;

        @Bind(R.id.receipt_sku_count)
        public TextView Count;

        @Bind(R.id.receipt_sku_price)
        public TextView Price;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public View getView(LayoutInflater inflater) {
        View vi = inflater.inflate(R.layout.template_receipt_ro_product, null, false);

        ViewHolder holder = new ViewHolder(vi);

        holder.Title.setText(model.getName());
        holder.Count.setText(String.valueOf(model.getQuantity()));
        holder.Price.setText(currencyFormatter.format(model.getUnitPrice()));

        return vi;
    }

    @Override
    public DateTime getSort() {
        return model.getDateCreated();
    }

    @Override
    public Long getIdentifier() {
        return model.getProductId();
    }
}
