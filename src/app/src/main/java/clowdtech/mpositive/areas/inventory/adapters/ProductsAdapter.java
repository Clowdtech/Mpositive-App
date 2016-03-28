package clowdtech.mpositive.areas.inventory.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.inventory.views.IProductSelection;
import clowdtech.mpositive.areas.shared.InventoryItem;

public class ProductsAdapter extends BaseAdapter implements Filterable {
    private List<InventoryItem> data;

    private int layoutResId;
    private final IProductSelection selectListener;

    private final LayoutInflater inflater;

    private Filter filter;

    private final int defaultTextColour;
    private final int defaultBackgroundColour;
    private final int defaultBorderColour;

    public ProductsAdapter(Context context, ArrayList<InventoryItem> data, int layoutResId, IProductSelection selectListener) {
        this.data = data;
        this.layoutResId = layoutResId;
        this.selectListener = selectListener;

        this.filter = new ProductsFilter(context, data, new FilterResponse<InventoryItem>() {
            @Override
            public void filtered(List<InventoryItem> data) {
                ProductsAdapter.this.data = data;

                notifyDataSetChanged();
            }
        });

        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.defaultTextColour = context.getResources().getColor(R.color.tile_product_foreground_default);
        this.defaultBackgroundColour = context.getResources().getColor(R.color.tile_product_background_default);
        this.defaultBorderColour = context.getResources().getColor(R.color.view_group_border);
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

    @Override
    public Filter getFilter() {
        return filter;
    }

    public static class ViewHolder {
        public TextView price;
        public TextView title;
        public InventoryItem product;
        public ViewGroup container;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;

        InventoryItem product = data.get(position);

        if (convertView == null) {
            view = inflater.inflate(layoutResId, parent, false);

            holder = new ViewHolder();

            holder.container = (ViewGroup) view.findViewById(R.id.item_container);
            holder.price = (TextView) view.findViewById(R.id.tile_subtitle);
            holder.title = (TextView) view.findViewById(R.id.tile_title);

            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewHolder viewHolder = (ViewHolder) v.getTag();

                    selectListener.productSelected(viewHolder.product);
                }
            });

            holder.container.setTag(holder); // messy all triggered by the tile animation
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.product = product;

        // horrible hack attack - would like a better way of coupling the view+adapter to inject them

        if (holder.price != null)
            holder.price.setText(product.getSubTitle());

        holder.title.setText(product.getTitle());

        if (product.getForeground() != null) {
            holder.title.setTextColor(product.getForeground());

            if (holder.price != null)
                holder.price.setTextColor(product.getForeground());
        } else {
            holder.title.setTextColor(defaultTextColour);

            if (holder.price != null)
                holder.price.setTextColor(defaultTextColour);
        }

        if (product.getBackground() != null) {
            holder.container.setBackgroundColor(product.getBackground());
        } else {
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(defaultBackgroundColour);
            gd.setCornerRadius(5);
            gd.setStroke(2, this.defaultBorderColour);

            holder.container.setBackgroundDrawable(gd);
        }

        return view;
    }
}