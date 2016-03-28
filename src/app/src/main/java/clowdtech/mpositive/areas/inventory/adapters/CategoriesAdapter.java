package clowdtech.mpositive.areas.inventory.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.List;

import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.inventory.viewModels.CategoryViewModel;
import clowdtech.mpositive.areas.inventory.views.ICategorySelection;

public class CategoriesAdapter extends BaseAdapter implements Filterable {
    private final CategoriesFilter filter;

    private final int defaultTextColour;
    private final int defaultBackgroundColour;

    private List<CategoryViewModel> data;

    private final LayoutInflater inflater;

    private int viewResource;
    private final ICategorySelection selectListener;

    public CategoriesAdapter(Context context, List<CategoryViewModel> data, int viewResource, ICategorySelection selectListener) {
        this.data = data;
        this.viewResource = viewResource;
        this.selectListener = selectListener;

        this.filter = new CategoriesFilter(context, data, new FilterResponse<CategoryViewModel>() {
            @Override
            public void filtered(List<CategoryViewModel> data) {
                CategoriesAdapter.this.data = data;

                notifyDataSetChanged();
            }
        });

        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.defaultTextColour = context.getResources().getColor(R.color.tile_category_foreground_default);
        this.defaultBackgroundColour = context.getResources().getColor(R.color.tile_category_background_default);
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

    @Override
    public Filter getFilter() {
        return filter;
    }

    public static class ViewHolder {
        public TextView title;
        public View container;
        public CategoryViewModel category;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;

        CategoryViewModel category = data.get(position);

        if (convertView == null) {
            view = inflater.inflate(this.viewResource, parent, false);

            holder = new ViewHolder();

            holder.title = (TextView) view.findViewById(R.id.tile_title);
            holder.container = view.findViewById(R.id.item_container);

            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectListener.categorySelected(holder.category);
                }
            });

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.category = category;

        holder.title.setText(category.getName());

        if (category.getForeground() != null) {
            holder.title.setTextColor(category.getForeground());
        } else {
            holder.title.setTextColor(defaultTextColour);
        }

        if (category.getBackground() != null) {
            holder.container.setBackgroundColor(category.getBackground());
        } else {
            holder.container.setBackgroundColor(defaultBackgroundColour);
        }

        return view;
    }
}