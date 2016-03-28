package clowdtech.mpositive.areas.inventory.adapters;

import java.util.List;

public interface FilterResponse<TData> {
    void filtered(List<TData> data);
}
