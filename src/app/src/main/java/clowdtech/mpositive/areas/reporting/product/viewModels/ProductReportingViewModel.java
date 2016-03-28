package clowdtech.mpositive.areas.reporting.product.viewModels;

import com.clowdtech.data.DateTimeHelper;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductReportingViewModel {
    private final long timestamp;

    private Map<Long, ProductReportingItemViewModel> productReports = new HashMap<>();

    public ProductReportingViewModel(long timestamp) {
        this.timestamp = timestamp;
    }

    public void addReportingItem(String name, long id, int quantity, BigDecimal lineValue) {
        BigDecimal totalValue = lineValue.multiply(new BigDecimal(quantity));

        if(productReports.containsKey(id)) {
            productReports.get(id).Quantity += quantity;
            productReports.get(id).TotalValue = productReports.get(id).TotalValue.add(totalValue);
        }
        else {
            ProductReportingItemViewModel newViewModel = new ProductReportingItemViewModel();
            newViewModel.Id = id;
            newViewModel.Name = name;
            newViewModel.Quantity = quantity;
            newViewModel.TotalValue = totalValue;
            productReports.put(id, newViewModel);
        }
    }

    public List<ProductReportingItemViewModel> getViewModels() {
        return new ArrayList<>(productReports.values());
    }

    public String getHeader() {
        return DateTimeHelper.getFriendlyDateString(new DateTime(timestamp));
    }
}
