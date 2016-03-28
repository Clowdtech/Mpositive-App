package clowdtech.mpositive.areas.till.views;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.clowdtech.data.entities.IOrderLineManual;
import com.clowdtech.data.entities.ITransactionLineManual;

import org.joda.time.DateTime;

import java.text.NumberFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.till.contracts.IManualLineSelection;

public class ManualLineEditableView implements IReceiptLineView {

    private final NumberFormat currencyFormatter;

    private IOrderLineManual model;

    private Integer maxNameLength;
    private IManualLineSelection<ManualLineEditableView, ITransactionLineManual> selection;

    public ManualLineEditableView(IOrderLineManual model, Integer maxNameLength, IManualLineSelection<ManualLineEditableView, ITransactionLineManual> selection) {
        this.model = model;
        this.maxNameLength = maxNameLength;
        this.selection = selection;

        this.currencyFormatter = NumberFormat.getCurrencyInstance();
    }

    protected class ViewHolder {
        @Bind(R.id.receipt_title)
        public TextView Title;

        @Bind(R.id.receipt_sku_price)
        public TextView Price;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.receipt_delete_line)
        public void deleteLine() {
            selection.removeLine(ManualLineEditableView.this, model);
        }
    }

    @Override
    public View getView(LayoutInflater inflater) {
        View vi = inflater.inflate(R.layout.template_receipt_row_manual, null, false);

        //TODO: check this implementation can be adapted to the viewholder pattern
        ViewHolder holder = new ViewHolder(vi);

        holder.Title.setText(getName());
        holder.Price.setText(currencyFormatter.format(model.getPrice()));

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
        return model.getCreatedDate();
    }

    @Override
    public Long getIdentifier() {
        return null;
    }
}

