package clowdtech.mpositive.areas.till.views;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.clowdtech.data.entities.ITransactionLineProduct;

import org.joda.time.DateTime;

import java.text.NumberFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.till.contracts.IProductLineSelection;

public class ProductLineEditableView implements IReceiptLineView {
    private final NumberFormat currencyFormatter;

    private ITransactionLineProduct model;
    private int maxNameLength;
    private IProductLineSelection<ProductLineEditableView, ITransactionLineProduct> selection;

    public ProductLineEditableView(ITransactionLineProduct line, int maxNameLength, IProductLineSelection<ProductLineEditableView, ITransactionLineProduct> selection) {
        this.model = line;
        this.maxNameLength = maxNameLength;
        this.selection = selection;

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

        @OnClick(R.id.receipt_line_count_decrement)
        public void decrement() {
            selection.decrementLine(ProductLineEditableView.this, model);
        }

        @OnClick(R.id.receipt_line_count_increment)
        public void increment() {
            selection.incrementLine(model);
        }
    }

    @Override
    public View getView(LayoutInflater inflater) {
        View vi = inflater.inflate(R.layout.template_receipt_row_product, null, false);

        ViewHolder holder = new ViewHolder(vi);

        holder.Title.setText(getName());
        holder.Count.setText(String.valueOf(model.getQuantity()));
        holder.Price.setText(currencyFormatter.format(model.getUnitPrice()));

        return vi;
    }

    @NonNull
    private String getName() {
        if (model.getName().length() > maxNameLength) {
            return model.getName().substring(0, maxNameLength - 3) + "...";
        }

        return model.getName();
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

