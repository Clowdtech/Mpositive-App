package clowdtech.mpositive.receipt;

import android.support.annotation.NonNull;

import com.clowdtech.data.entities.IOrderLineManual;
import com.clowdtech.data.entities.ITransactionLineManual;
import com.clowdtech.data.entities.ITransactionLineProduct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import clowdtech.mpositive.areas.shared.models.ReceiptLinePrintModel;
import clowdtech.mpositive.areas.shared.models.ReceiptLineViewModel;
import clowdtech.mpositive.data.transactions.entities.Receipt;

public class ReceiptLineViewFactory {

    public static List<ReceiptLinePrintModel> getCondensedProductItems(List<ITransactionLineProduct> productLines) {
        ArrayList<ReceiptLinePrintModel> viewModels = new ArrayList<>();

        Collection<ReceiptLinePrintModel> productModels = getPrintProductModels(productLines);

        viewModels.addAll(productModels);

        return viewModels;
    }

    public static List<ReceiptLinePrintModel> getCondensedManualItems(List<ITransactionLineManual> manualLines) {
        ArrayList<ReceiptLinePrintModel> viewModels = new ArrayList<>();

        Collection<ReceiptLinePrintModel> printModels = getPrintManualModels(manualLines);

        viewModels.addAll(printModels);

        return viewModels;
    }

    public static List<ReceiptLineViewModel> getCondensedViewReceipt(Collection<ITransactionLineProduct> productLines, List<ITransactionLineManual> manualLines, int productView, int manualView) {
        ArrayList<ReceiptLineViewModel> viewModels = new ArrayList<>();

        Collection<ReceiptLineViewModel> productModels = getProductModels(productLines, productView);

        viewModels.addAll(productModels);

        Collection<ReceiptLineViewModel> manualModels = getManualModels(manualLines, manualView);

        viewModels.addAll(manualModels);

        Collections.sort(viewModels, new ReceiptLineModelComparer());

        return viewModels;
    }

    @NonNull
    private static Collection<ReceiptLineViewModel> getProductModels(Collection<ITransactionLineProduct> productRoll, int productViewId) {
        Map<ITransactionLineProduct, ReceiptLineViewModel> productModels = new HashMap<>();

        for (ITransactionLineProduct line : productRoll) {
            if (productModels.containsKey(line)) {
                ReceiptLineViewModel current = productModels.get(line);

                current.incrementCount();
            } else {
                productModels.put(line, new ReceiptLineViewModel(line.getDateCreated(), line.getName(), productViewId));
            }
        }

        return productModels.values();
    }

    @NonNull
    private static Collection<ReceiptLineViewModel> getManualModels(List<ITransactionLineManual> manualRoll, int manualViewId) {
        Collection<clowdtech.mpositive.areas.shared.models.ReceiptLineViewModel> lineViewModels = new ArrayList<>();

        for (ITransactionLineManual line : manualRoll) {
            lineViewModels.add(new ReceiptLineViewModel(line.getDateCreated(), line.getName(), manualViewId));
        }

        return lineViewModels;
    }

    private static class ReceiptLineModelComparer implements Comparator<ReceiptLineViewModel> {
        @Override
        public int compare(ReceiptLineViewModel lhs, ReceiptLineViewModel rhs) {
            return lhs.getAdded().isBefore(rhs.getAdded()) ? -1
                    : lhs.getAdded().isAfter(rhs.getAdded()) ? 1
                    : 0;
        }
    }

    public static List<ReceiptLinePrintModel> getCondensedOrderLines(List<ITransactionLineProduct> productLines, List<IOrderLineManual> manualEntries) {
        return condenseOrderLines(productLines, manualEntries);
    }

    private static List<ReceiptLinePrintModel> condenseOrderLines(List<ITransactionLineProduct> productLines, List<IOrderLineManual> manualEntries) {
        ArrayList<ReceiptLinePrintModel> viewModels = new ArrayList<>();

        Collection<ReceiptLinePrintModel> productModels = getPrintProductModels(productLines);

        viewModels.addAll(productModels);

        Collection<ReceiptLinePrintModel> lineViewModels = new ArrayList<>();

        for (IOrderLineManual line : manualEntries) {
            lineViewModels.add(new ReceiptLinePrintModel(line.getCreatedDate(), line.getName(), String.valueOf(line.getPrice()), 1));
        }

        viewModels.addAll(lineViewModels);

        Collections.sort(viewModels, new ReceiptLinePrintComparer());

        return viewModels;
    }

    public static List<ReceiptLinePrintModel> getCondensedPrintReceipt(Receipt receipt) {
        return condenseLines(receipt.getProductRoll(), receipt.getManualRoll());
    }

    private static List<ReceiptLinePrintModel> condenseLines(List<ITransactionLineProduct> productLines, List<ITransactionLineManual> manualEntries) {
        ArrayList<ReceiptLinePrintModel> viewModels = new ArrayList<>();

        Collection<ReceiptLinePrintModel> productModels = getPrintProductModels(productLines);

        viewModels.addAll(productModels);

        Collection<ReceiptLinePrintModel> manualModels = getPrintManualModels(manualEntries);

        viewModels.addAll(manualModels);

        Collections.sort(viewModels, new ReceiptLinePrintComparer());

        return viewModels;
    }

    @NonNull
    private static Collection<ReceiptLinePrintModel> getPrintProductModels(List<ITransactionLineProduct> productRoll) {
        Map<ITransactionLineProduct, ReceiptLinePrintModel> productModels = new HashMap<>();

        for (ITransactionLineProduct line : productRoll) {
            //TODO: pretty awful, 2 different interpretations of receipt lines, broken out in storage, grouped in checkout
            if (productModels.containsKey(line)) {
                ReceiptLinePrintModel current = productModels.get(line);

                current.incrementCount();
            } else {
                productModels.put(line, new ReceiptLinePrintModel(line.getDateCreated(), line.getName(), getPrice(line), line.getQuantity()));
            }
        }

        return productModels.values();
    }

    private static String getPrice(ITransactionLineProduct line) {
        return String.valueOf(line.getUnitPrice().multiply(BigDecimal.valueOf(line.getQuantity())));
    }

    @NonNull
    private static Collection<ReceiptLinePrintModel> getPrintManualModels(List<ITransactionLineManual> manualRoll) {
        Collection<ReceiptLinePrintModel> lineViewModels = new ArrayList<>();

        for (ITransactionLineManual line : manualRoll) {
            lineViewModels.add(new ReceiptLinePrintModel(line.getDateCreated(), line.getName(), String.valueOf(line.getPrice()), 1));
        }

        return lineViewModels;
    }

    private static class ReceiptLinePrintComparer implements Comparator<ReceiptLinePrintModel> {
        @Override
        public int compare(ReceiptLinePrintModel lhs, ReceiptLinePrintModel rhs) {
            return lhs.getAdded().isBefore(rhs.getAdded()) ? -1
                    : lhs.getAdded().isAfter(rhs.getAdded()) ? 1
                    : 0;
        }
    }
}
