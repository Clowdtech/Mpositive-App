package clowdtech.printer.documents.orders;

import android.content.Context;

import com.starmicronics.stario.StarIOPortException;

import java.util.ArrayList;

import clowdtech.printer.RasterDocument;
import clowdtech.printer.documents.StarLineTransactionBase;
import clowdtech.printer.documents.StarRasterTransactionBase;
import clowdtech.printer.formatinfo.PrintFormatInfoOrder;

public class StarRasterOrder extends StarRasterTransactionBase {
    private Context context;
    private String portName;
    private String portSettings;
    private PrintFormatInfoOrder formatInfo;

    public StarRasterOrder(Context context, String portName, String portSettings, PrintFormatInfoOrder formatInfo) {
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

        printLines(formatInfo, list);

        printGrandTotal(formatInfo, list);

        printFooter(formatInfo, list);

        list.add(rasterDoc.EndDocumentCommandData());

        list.add(COMMAND_CUT); // Cut

        sendCommand(context, portName, portSettings, list);
    }
}