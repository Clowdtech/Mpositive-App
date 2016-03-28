package clowdtech.mpositive.areas.inventory.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clowdtech.data.entities.Category;
import com.clowdtech.data.entities.CategoryTile;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import clowdtech.mpositive.R;

public class CategoriesViewAdapter extends BaseAdapter {
    private List<Category> data;

    private final LayoutInflater inflater;

    private final int defaultTextColour;
    private final int defaultBackgroundColour;

    public CategoriesViewAdapter(Context context, List<Category> data) {
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

        public Category category;

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
            view = inflater.inflate(R.layout.inventory_tile_category, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        Category category = data.get(position);

        holder.category = category;

        // horrible hack attack - would like a better way of coupling the view+adapter to inject them

        holder.price.setText("");

        holder.title.setText(category.getName());

        CategoryTile tile = category.getTile();

        if (tile != null) {
            if (tile.getForeground() != null) {
                holder.title.setTextColor(Color.parseColor(tile.getForeground()));
            } else {
                holder.title.setTextColor(defaultTextColour);
            }

            if (tile.getBackground() != null) {
                holder.container.setCardBackgroundColor(Color.parseColor(tile.getBackground()));
            } else {
                holder.container.setCardBackgroundColor(defaultBackgroundColour);
            }
        }

        return view;
    }
}