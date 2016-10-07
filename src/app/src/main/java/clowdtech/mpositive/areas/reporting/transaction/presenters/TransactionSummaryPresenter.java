package clowdtech.mpositive.areas.reporting.transaction.presenters;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.clowdtech.data.DateTimeHelper;
import com.clowdtech.data.entities.ITransactionLineManual;
import com.clowdtech.data.entities.ITransactionLineProduct;
import com.clowdtech.data.entities.ITransaction;
import com.clowdtech.data.entities.ITransactionNoSale;
import com.clowdtech.data.entities.PaymentTypes;
import com.clowdtech.data.repository.ITransactionsRepository;
import com.squareup.otto.Subscribe;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.reporting.transaction.IShareController;
import clowdtech.mpositive.areas.reporting.transaction.captions.CaptionsTransactionSummary;
import clowdtech.mpositive.areas.reporting.transaction.viewModels.TransactionListItemViewModel;
import clowdtech.mpositive.areas.reporting.transaction.views.ITransactionSummaryView;
import clowdtech.mpositive.data.StockUnit;
import clowdtech.mpositive.data.lines.EntryLineManual;
import clowdtech.mpositive.data.lines.EntryLineProduct;
import clowdtech.mpositive.data.transactions.entities.Receipt;
import clowdtech.mpositive.data.transactions.entities.ReceiptPayment;
import clowdtech.mpositive.queue.IEventBus;
import clowdtech.mpositive.queue.events.ReceiptRefundEvent;
import clowdtech.mpositive.report.PrintTransactionsReportAsync;
import clowdtech.mpositive.ui.BasePresenter;
import clowdtech.printer.IPrintController;
import clowdtech.printer.TransactionsReportFormatInfo;

public class TransactionSummaryPresenter extends BasePresenter<ITransactionSummaryView> implements ITransactionSummaryPresenter {
    private IPrintController printController;
    private ITransactionsRepository transactionRepo;
    private IShareController shareController;

    private final NumberFormat currencyFormatter;

    private CaptionsTransactionSummary captions;
    private IEventBus eventBus;

    private List<Receipt> transactions;
    private long lowerStamp;
    private long upperStamp;

    @Inject
    public TransactionSummaryPresenter(CaptionsTransactionSummary captions, IEventBus eventBus, ITransactionsRepository transactionRepo, IPrintController printController, IShareController shareController) {
        this.captions = captions;
        this.eventBus = eventBus;
        this.transactionRepo = transactionRepo;
        this.printController = printController;
        this.shareController = shareController;

        this.currencyFormatter = NumberFormat.getCurrencyInstance();
    }

    @Override
    public void bindView(ITransactionSummaryView view) {
        super.bindView(view);

        eventBus.register(this);
    }

    @Override
    public void unbindView() {
        super.unbindView();

        eventBus.unregister(this);
    }

    @Override
    public void setData(String header, long lowerStamp, long upperStamp) {
        this.lowerStamp = lowerStamp;
        this.upperStamp = upperStamp;

        this.view.setHeading(header);

        List<ITransaction> transactions = transactionRepo.getTransactionsBetween(lowerStamp, upperStamp);

        this.transactions = new ArrayList<>();

        for (ITransaction trans : transactions) {
            Receipt receipt = buildReceipt(trans);

            this.transactions.add(receipt);
        }

        regenerateTransactions(lowerStamp, upperStamp);
    }

    @Subscribe
    public void subscribeReceiptRefunded(ReceiptRefundEvent event) {
        for (Receipt receipt : this.transactions) {
            if (receipt.getId() != event.getId()) {
                continue;
            }

            receipt.setRefunded();

            break;
        }

        regenerateTransactions(lowerStamp, upperStamp);
    }

    private void regenerateTransactions(long lowerStamp, long upperStamp) {
        ArrayList<TransactionListItemViewModel> lines = getTransactionList(this.transactions);

        List<ITransactionNoSale> noSales = transactionRepo.getNoSalesBetween(lowerStamp, upperStamp);

        for (ITransactionNoSale noSale : noSales) {
            String title = getTitle(noSale.getCreatedDate());

            lines.add(new TransactionListItemViewModel(noSale.getCreatedDate(), title, "No Sale", 0, false));
        }

        Collections.sort(lines, new Comparator<TransactionListItemViewModel>() {
            @Override
            public int compare(TransactionListItemViewModel lhs, TransactionListItemViewModel rhs) {
                if (lhs.getSort().getMillis() < rhs.getSort().getMillis()) {
                    return 1;
                }

                if (lhs.getSort().getMillis() > rhs.getSort().getMillis()) {
                    return -1;
                }

                return 0;
            }
        });

        this.view.setItems(lines);
    }

    @Override
    public void shareTransactions() {
        List<ITransactionNoSale> noSalesBetween = transactionRepo.getNoSalesBetween(lowerStamp, upperStamp);

        TransactionsReportFormatInfo formatInfo = getFormatInfo(transactions, noSalesBetween);

        StringBuilder csvExport = new StringBuilder();

        csvExport.append("\"");
        csvExport.append(formatInfo.getHeader());
        csvExport.append("\"");
        csvExport.append("\r\n");

        csvExport.append("\"");
        csvExport.append(formatInfo.getSubHeader());
        csvExport.append("\"");
        csvExport.append("\r\n");
        csvExport.append("\r\n");

        String[] reportHeaders = formatInfo.getReportHeaders();

        String headersString = delimitStringByComma(reportHeaders);

        csvExport.append(headersString);
        csvExport.append("\r\n");

        for (String[] row : formatInfo.getReportRows()) {
            String transactionString = delimitStringByComma(row);

            csvExport.append(transactionString);
            csvExport.append("\r\n");
        }

        csvExport.append("\r\n");

        csvExport.append(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\r\n",
                formatInfo.AmountCardCaption,
                formatInfo.AmountCashCaption,
                formatInfo.AmountOtherCaption,
                formatInfo.TransactionCountCaption,
                formatInfo.AverageCaption));

        csvExport.append(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                formatInfo.AmountCard,
                formatInfo.AmountCash,
                formatInfo.AmountOther,
                formatInfo.TransactionCount,
                formatInfo.AverageValue));

        this.shareController.shareTransactionsReport(this.view.getContext(), formatInfo.getSubHeader().replace(" ", ""), csvExport.toString());
    }

    private String delimitStringByComma(String[] reportHeaders) {
        String headerString = "";

        for (int i = 0; i < reportHeaders.length; i++) {
            if (i == reportHeaders.length - 1) {
                headerString += String.format("\"%s\"", reportHeaders[i]);
            } else {
                headerString += String.format("\"%s\",", reportHeaders[i]);
            }
        }

        return headerString;
    }

    @Override
    public void transactionSelected(int position, long id) {
        if (id == 0) {
            return;
        }

        this.view.setSelectedTransaction(position);

        this.view.getContainer().transactionSelected(id);
    }

    @Override
    public void printTransactions() {
        List<ITransactionNoSale> noSalesBetween = transactionRepo.getNoSalesBetween(lowerStamp, upperStamp);

        TransactionsReportFormatInfo formatInfo = getFormatInfo(transactions, noSalesBetween);

        PrintTransactionsReportAsync task = new PrintTransactionsReportAsync(this.view.getContext(), this.printController);

        task.execute(formatInfo);
    }

    private ArrayList<TransactionListItemViewModel> getTransactionList(List<Receipt> receipts) {
        int transactionCount = 0;

        BigDecimal cardTotal = new BigDecimal(0.00);
        BigDecimal cashTotal = new BigDecimal(0.00);
        BigDecimal otherTotal = new BigDecimal(0.00);

        ArrayList<TransactionListItemViewModel> lines = new ArrayList<>();

        for (Receipt receipt : receipts) {
            DateTime receiptDate = receipt.getCreated();

            String title = getTitle(receiptDate);

            String info = NumberFormat.getCurrencyInstance().format(receipt.getTotal());

            lines.add(new TransactionListItemViewModel(receiptDate, title, info, receipt.getId(), receipt.isRefunded()));

            if (receipt.isRefunded()) {
                continue;
            }

            transactionCount++;

            switch (receipt.getPayment().getType()) {
                case Card:
                    cardTotal = cardTotal.add(receipt.getTotal());
                    break;
                case Cash:
                    cashTotal = cashTotal.add(receipt.getTotal());
                    break;
                case Other:
                    otherTotal = otherTotal.add(receipt.getTotal());
                    break;
            }
        }

        double transactionValue = cashTotal.add(cardTotal).add(otherTotal).doubleValue();

        double averageTransaction = transactionValue == 0 || transactionCount == 0 ? 0 : transactionValue / transactionCount;

        this.view.setAverageTransaction(String.format("(avg. %s)", NumberFormat.getCurrencyInstance().format(averageTransaction)));

        String caption = transactionCount == 1
                ? captions.getSingleCountCaption()
                : captions.getMultipleCountCaption();

        this.view.setTransactionTotal(String.format(caption, transactionCount));

        this.view.setCardTotal(currencyFormatter.format(cardTotal));
        this.view.setCashTotal(currencyFormatter.format(cashTotal));
        this.view.setOtherTotal(currencyFormatter.format(otherTotal));

        boolean exportEnabled = receipts.size() > 0;

        this.view.setPrintEnabled(exportEnabled);
        this.view.setShareEnabled(exportEnabled);

        return lines;
    }

    private String getTitle(DateTime createdDate) {
        String title;

        DateTime todaysDay = DateTime.now().withTimeAtStartOfDay();

        if (todaysDay.equals(createdDate.withTimeAtStartOfDay())) {
            title = String.format("%s %s", "Today", DateTimeHelper.getFriendlyTimeString(createdDate));
        } else {
            title = DateTimeHelper.getFriendlyDateAndTimeString(createdDate);
        }

        return title;
    }

    // TODO: repeated code..
    private Receipt buildReceipt(ITransaction transaction) {
        ArrayList<ITransactionLineProduct> productLines = new ArrayList<>();
        for (ITransactionLineProduct ptl : transaction.productLines()) {
            StockUnit sku = new StockUnit(ptl.getProductId(), ptl.getName(), ptl.getUnitPrice());
            EntryLineProduct pl = new EntryLineProduct(sku, ptl.getDateCreated(), ptl.getQuantity());
            productLines.add(pl);
        }

        ArrayList<ITransactionLineManual> manualLines = new ArrayList<>();
        for (ITransactionLineManual mtl : transaction.manualLines()) {
            EntryLineManual ml = new EntryLineManual(mtl.getName(), mtl.getPrice(), mtl.getDateCreated());
            manualLines.add(ml);
        }

        ReceiptPayment receiptPayment = new ReceiptPayment(
                PaymentTypes.values()[transaction.getPayment().getPaymentType().getValue()],
                transaction.getPayment().getAmount());

        return new Receipt(
                transaction.getId(),
                transaction.getCreatedDate(),
                receiptPayment,
                productLines,
                manualLines,
                transaction.getRefunded());
    }

    private TransactionsReportFormatInfo getFormatInfo(List<Receipt> receipts, List<ITransactionNoSale> noSales) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.view.getContext());

        String[] headers = new String[]{"TransId", "Date", "PType", "Total"};

        ArrayList<String[]> rows = new ArrayList<>();

        Map<PaymentTypes, BigDecimal> typeTotals = new HashMap<>();

        int transactionCount = 0;

        final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss");

        for (Receipt receipt : receipts) {
            PaymentTypes paymentType = receipt.getPayment().getType();
            BigDecimal total = receipt.getTotal();

            String[] values = new String[4];
            values[0] = String.valueOf(receipt.getId());
            values[1] = dateTimeFormatter.print(receipt.getCreated());
            values[2] = String.valueOf(paymentType);
            values[3] = receipt.isRefunded() ? String.format("(%.2f)", total) : String.format("%.2f", total);
            rows.add(values);

            if (receipt.isRefunded()) {
                continue;
            } else {
                transactionCount++;
            }

            BigDecimal currentValue;

            if (typeTotals.containsKey(paymentType)) {
                currentValue = typeTotals.get(paymentType);

                typeTotals.remove(paymentType);
            } else {
                currentValue = BigDecimal.ZERO;
            }

            currentValue = currentValue.add(total);

            typeTotals.put(paymentType, currentValue);
        }

        // ouch
        for (ITransactionNoSale noSale : noSales) {
            String[] values = new String[4];
            values[0] = "No Sale";
            values[1] = dateTimeFormatter.print(noSale.getCreatedDate());
            values[2] = "";
            values[3] = "";
            rows.add(values);
        }

        Collections.sort(rows, new Comparator<String[]>() {
            @Override
            public int compare(String[] lhs, String[] rhs) {
                DateTime left = dateTimeFormatter.parseDateTime(lhs[1]);
                DateTime right = dateTimeFormatter.parseDateTime(rhs[1]);

                if (left.isBefore(right)) {
                    return 1;
                }

                if (left.isAfter(right)) {
                    return -1;
                }

                return 0;
            }
        });

        String dateRange;

        DateTimeFormatter periodFormat = DateTimeFormat.forPattern("YYYY-MM-dd");

        DateTime start = dateTimeFormatter.parseDateTime(rows.get(0)[1]);

        if (rows.size() > 1) {
            DateTime end = dateTimeFormatter.parseDateTime(rows.get(rows.size() - 1)[1]);

            if (start.isEqual(end)) {
                dateRange = periodFormat.print(start);
            } else {
                dateRange = String.format("%s - %s", periodFormat.print(start), periodFormat.print(end));
            }
        } else {
            dateRange = periodFormat.print(start);
        }

        String subHeader = dateRange;
        String footer = prefs.getString(view.getContext().getString(R.string.preference_receipt_footer), "");

        TransactionsReportFormatInfo formatInfo = new TransactionsReportFormatInfo(captions.getHeader(), subHeader, headers, rows, footer);

        BigDecimal cashTotal = typeTotals.get(PaymentTypes.Cash) == null ? new BigDecimal("0.00") : typeTotals.get(PaymentTypes.Cash);
        BigDecimal cardTotal = typeTotals.get(PaymentTypes.Card) == null ? new BigDecimal("0.00") : typeTotals.get(PaymentTypes.Card);
        BigDecimal otherTotal = typeTotals.get(PaymentTypes.Other) == null ? new BigDecimal("0.00") : typeTotals.get(PaymentTypes.Other);

        formatInfo.TransactionCountCaption = "Count";
        formatInfo.TransactionCount = transactionCount;
        formatInfo.AmountCashCaption = captions.getTenderCash();
        formatInfo.AmountCash = String.format("%.2f", cashTotal);
        formatInfo.AmountCardCaption = captions.getTenderCard();
        formatInfo.AmountCard = String.format("%.2f", cardTotal);
        formatInfo.AmountOtherCaption = captions.getTenderOther();
        formatInfo.AmountOther = String.format("%.2f", otherTotal);
        formatInfo.AverageCaption = "Average";

        double transactionValue = cashTotal.add(cardTotal).add(otherTotal).doubleValue();

        formatInfo.AverageValue = String.format("%.2f", transactionValue == 0 || transactionCount == 0 ? 0 : transactionValue / transactionCount);

        return formatInfo;
    }
}
