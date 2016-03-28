package clowdtech.mpositive.areas.inventory.adapters;

import android.content.Context;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import clowdtech.mpositive.areas.inventory.viewModels.ProductViewModel;
import clowdtech.mpositive.areas.shared.InventoryItem;

public class ProductsFilter extends Filter {
    private final Locale locale;
    private List<InventoryItem> data;
    private FilterResponse resultsFiltered;

    public ProductsFilter(Context context, List<InventoryItem> data, FilterResponse resultsFiltered) {
        this.locale = context.getResources().getConfiguration().locale;
        this.data = data;
        this.resultsFiltered = resultsFiltered;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        FilterResults results = new FilterResults();

        if (constraint.length() == 0) {
            results.values = data;
            results.count = data.size();
        } else {
            String filterString = constraint.toString().toLowerCase(locale);

            final List<InventoryItem> list = data;

            int count = list.size();
            final ArrayList<InventoryItem> finalList = new ArrayList<>(count);

            InventoryItem filterableItem;

            for (int i = 0; i < count; i++) {
                filterableItem = list.get(i);
                if (filterableItem.getTitle() != null && filterableItem.getTitle().toLowerCase(locale).contains(filterString)) {
                    finalList.add(filterableItem);
                }
            }

            results.values = finalList;
            results.count = finalList.size();
        }

        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        resultsFiltered.filtered((ArrayList<ProductViewModel>) results.values);
    }
}
