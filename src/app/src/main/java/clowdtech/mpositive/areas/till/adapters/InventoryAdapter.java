package clowdtech.mpositive.areas.till.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
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
import clowdtech.mpositive.areas.inventory.viewModels.ProductViewModel;
import clowdtech.mpositive.areas.shared.InventoryItem;
import clowdtech.mpositive.areas.till.viewModels.CategoryTileViewModel;

public class InventoryAdapter extends BaseAdapter {
    private final NumberFormat currencyFormatter;

    private List<InventoryItem> data;

    private final LayoutInflater inflater;
    private int tileCharacterLimit = 15;

    public InventoryAdapter(Context context, List<InventoryItem> data) {
        this.data = data;

        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.currencyFormatter = NumberFormat.getCurrencyInstance();
        this.tileCharacterLimit = context.getResources().getInteger(R.integer.inventory_tile_max_character);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return data.get(position).getItemId();
    }

    public class ViewHolder {
        @Bind(R.id.tile_title)
        public TextView name;

        @Bind(R.id.tile_subtitle)
        public TextView subtitle;

        @Bind(R.id.item_container)
        public CardView background;

        public InventoryItem model;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;

        InventoryItem model = data.get(position);

        if (isBackItem(model)) {
            vi = inflater.inflate(R.layout.inventory_tile_back, parent, false);

            return vi;
        } else if (convertView == null || currentViewForDifferentType(convertView, model)) {

            if (model instanceof ProductViewModel) {
                vi = inflater.inflate(R.layout.inventory_tile_product, parent, false);
            } else {
                vi = inflater.inflate(R.layout.inventory_tile_category, parent, false);
            }

            holder = new ViewHolder(vi);

            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.model = model;

        holder.name.setText(getTitle(holder));

        if (holder.model instanceof ProductViewModel) {
            ProductViewModel prodMod = (ProductViewModel) holder.model;

            holder.subtitle.setText(currencyFormatter.format(prodMod.getPrice()));

            int textColour = prodMod.getForeground();

            holder.name.setTextColor(textColour);
            holder.subtitle.setTextColor(textColour);

            holder.background.setCardBackgroundColor(prodMod.getBackground());

        } else {
            holder.subtitle.setText("");

            CategoryTileViewModel vm = (CategoryTileViewModel) holder.model;

            holder.name.setTextColor(vm.getForeground());
            holder.background.setCardBackgroundColor(vm.getBackground());
        }

        return vi;
    }

    private boolean isBackItem(InventoryItem model) {
        return model.getItemId() == 0;
    }

    private String getTitle(ViewHolder holder) {
        String title = holder.model.getTitle();

        if (title.length() > tileCharacterLimit) {
            return title.substring(0, tileCharacterLimit - 3) + "...";
        }

        return title;
    }

    private boolean currentViewForDifferentType(View convertView, InventoryItem model) {
        Object tag = convertView.getTag();

        if (tag == null) {
            return true;
        }

        boolean currentViewProduct = ((ViewHolder) tag).model instanceof ProductViewModel;

        boolean currentModelProduct = model instanceof ProductViewModel;

        return currentViewProduct && !currentModelProduct || !currentViewProduct && currentModelProduct;
    }
}