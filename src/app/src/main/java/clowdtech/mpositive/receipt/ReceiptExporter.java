package clowdtech.mpositive.receipt;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import clowdtech.mpositive.ISharedPreferences;
import clowdtech.mpositive.R;
import clowdtech.mpositive.TaskListener;
import clowdtech.mpositive.areas.shared.models.ReceiptLinePrintModel;
import clowdtech.mpositive.data.transactions.entities.Receipt;
import clowdtech.mpositive.data.transactions.entities.ReceiptPayment;
import clowdtech.printer.ReceiptLineInfo;
import clowdtech.printer.formatinfo.PrintFormatInfoReceipt;

public class ReceiptExporter implements IReceiptExporter {
    private final Context context;
    private final String footer;
    private final String header;
    private final String printCommandType;

    public ReceiptExporter(Context context, ISharedPreferences preferences) {
        this.context = context;

        printCommandType = preferences.getPrinterCommandType();
        header = preferences.getPrinterHeader();
        footer = preferences.getPrinterFooter();
    }

    @Override
    public void printReceipt(Receipt receipt, TaskListener listener) {
        List<ReceiptLineInfo> lines = new ArrayList<>();

        List<ReceiptLinePrintModel> items = ReceiptLineViewFactory.getCondensedPrintReceipt(receipt);

        for (ReceiptLinePrintModel line : items) {
            lines.add(getInfoLine(line));
        }

        String payment = getPaymentInfo(receipt.getPayment(), receipt.getChange());

        String total = String.valueOf(receipt.getTotal());

        PrintFormatInfoReceipt formatInfo = new PrintFormatInfoReceipt(header, receipt.getCreated(), lines, footer, receipt.getId(), payment, total);

        print(formatInfo, listener);
    }

    private void print(PrintFormatInfoReceipt formatInfo, TaskListener listener) {
        PrintReceiptAsync task = new PrintReceiptAsync(this.context, printCommandType, formatInfo, listener);

        task.execute();
    }

    private String getPaymentInfo(ReceiptPayment payment, BigDecimal change) {
        switch (payment.getType()) {
            case Cash:
                return String.format("CASH: %s, CHANGE: %s", payment.getAmountPaid(), change);
            case Card:
                return String.format("CARD: %s", payment.getAmountPaid());
            case Other:
                return String.format("OTHER: %s", payment.getAmountPaid());
        }

        return "";
    }

    private ReceiptLineInfo getInfoLine(ReceiptLinePrintModel line) {
        return new ReceiptLineInfo(line.getLineCount(), line.getTitle(), line.getSubTitle());
    }
}

