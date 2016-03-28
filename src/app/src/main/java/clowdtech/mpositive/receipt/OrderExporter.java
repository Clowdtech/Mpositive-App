package clowdtech.mpositive.receipt;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.shared.models.ReceiptLinePrintModel;
import clowdtech.mpositive.data.RunningOrder;
import clowdtech.printer.formatinfo.PrintFormatInfoOrder;
import clowdtech.printer.ReceiptLineInfo;

public class OrderExporter implements IOrderExporter {
    private RunningOrder runningOrder;
    private String footer;
    private String header;
    private String printCommandType;

    public OrderExporter(RunningOrder runningOrder) {
        this.runningOrder = runningOrder;
    }

    @Override
    public void printOrder(Context context) {
        //TODO: try and remove this from the method, inject in constructor stops injection on singletons (presenters)
        loadPreferences(context);

        List<ReceiptLineInfo> lines = new ArrayList<>();

        List<ReceiptLinePrintModel> items = ReceiptLineViewFactory.getCondensedOrderLines(runningOrder.getProductLines(), runningOrder.getManualEntries());

        for (ReceiptLinePrintModel line : items) {
            lines.add(getInfoLine(line));
        }

        String totalString = String.valueOf(runningOrder.getReceiptTotal());

        PrintFormatInfoOrder formatInfo = new PrintFormatInfoOrder(header, DateTime.now(), lines, footer, totalString);

        PrintOrderAsync task = new PrintOrderAsync(context, printCommandType, formatInfo);

        task.execute();
    }

    private void loadPreferences(Context context) {
        Resources resources = context.getResources();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        printCommandType = prefs.getString(resources.getString(R.string.preference_printer_command_key), "");
        header = prefs.getString(resources.getString(R.string.preference_receipt_header), "");
        footer = prefs.getString(resources.getString(R.string.preference_receipt_footer), "");
    }

    private ReceiptLineInfo getInfoLine(ReceiptLinePrintModel line) {
        return new ReceiptLineInfo(line.getLineCount(), line.getTitle(), line.getSubTitle());
    }
}

