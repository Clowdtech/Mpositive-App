package clowdtech.printer.documents.receipts;

import android.content.Context;
import android.graphics.Typeface;

import com.starmicronics.stario.StarIOPortException;

import java.util.ArrayList;

import clowdtech.printer.RasterDocument;
import clowdtech.printer.documents.StarRasterTransactionBase;
import clowdtech.printer.formatinfo.PrintFormatInfoReceipt;

public class StarRasterReceipt extends StarRasterTransactionBase {
    private Context context;
    private String portName;
    private String portSettings;
    private PrintFormatInfoReceipt formatInfo;

    public StarRasterReceipt(Context context, String portName, String portSettings, PrintFormatInfoReceipt formatInfo) {
        this.context = context;
        this.portName = portName;
        this.portSettings = portSettings;
        this.formatInfo = formatInfo;
    }

    public void invoke() throws StarIOPortException {
        ArrayList<byte[]> list = new ArrayList<>();

        RasterDocument rasterDoc = new RasterDocument(RasterDocument.RasSpeed.Medium, RasterDocument.RasPageEndMode.FeedAndFullCut, RasterDocument.RasPageEndMode.FeedAndFullCut, RasterDocument.RasTopMargin.Standard, 0, 0, 0);

        list.add(rasterDoc.BeginDocumentCommandData());

        printHeader(formatInfo, list);

        printDocumentIdentifier(list);

        printLines(formatInfo, list);

        printGrandTotal(formatInfo, list);

        printPaymentInfo(list);

        printFooter(formatInfo, list);

        list.add(rasterDoc.EndDocumentCommandData());

        list.add(COMMAND_CUT);

        sendCommand(context, portName, portSettings, list);
    }

    private void printDocumentIdentifier(ArrayList<byte[]> list) {
        list.add(createRasterCommand(String.format("SALE: %s", formatInfo.getIdentifier()), 13, Typeface.BOLD));
    }

    private void printPaymentInfo(ArrayList<byte[]> list) {
        list.add(createRasterCommand(String.format("Charge: %s", formatInfo.getTotal()), 13, 0));


        list.add(createRasterCommand(String.format("%s\r\n", formatInfo.getPaymentInfo()), 13, 0));
    }
}
