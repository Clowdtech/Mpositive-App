package clowdtech.mpositive.areas.inventory.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clowdtech.data.entities.IProduct;
import com.clowdtech.data.entities.IProductTile;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import clowdtech.mpositive.R;

public class ProductsViewAdapter extends BaseAdapter {
    private List<IProduct> data;

    private final LayoutInflater inflater;

    private final int defaultTextColour;
    private final int defaultBackgroundColour;

    public ProductsViewAdapter(Context context, List<IProduct> data) {
        this.data = data;

        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.defaultTextColour = context.getResources().getColor(R.color.tile_product_foreground_default);
        this.defaultBackgroundColour = context.getResources().getColor(R.color.tile_product_background_default);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return data.get(position).getId();
    }

    public static class ViewHolder {
        @Bind(R.id.tile_subtitle) public TextView price;
        @Bind(R.id.tile_title) public TextView title;
        @Bind(R.id.item_container) public CardView container;

        public IProduct product;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.inventory_tile_product, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        IProduct product = data.get(position);

        holder.product = product;

        // horrible - would like a better way of coupling the view+adapter to inject them

        holder.price.setText(String.valueOf(product.getPrice()));

        holder.title.setText(product.getName());

        int foreground;
        int background;

        IProductTile tile = product.getTile();

        if (tile != null) {
            foreground = Color.parseColor(tile.getForeground());
            background = Color.parseColor(tile.getBackground());
        } else {
            foreground = defaultTextColour;
            background = defaultBackgroundColour;
        }

        holder.title.setTextColor(foreground);

        if (holder.price != null)
        {
            holder.price.setTextColor(foreground);
        }

        holder.container.setCardBackgroundColor(background);

        return view;
    }
}