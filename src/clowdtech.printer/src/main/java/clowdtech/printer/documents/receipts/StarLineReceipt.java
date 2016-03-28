package clowdtech.printer.documents.receipts;

import android.content.Context;

import com.starmicronics.stario.StarIOPortException;

import java.util.ArrayList;

import clowdtech.printer.documents.StarLineTransactionBase;
import clowdtech.printer.formatinfo.IReceiptFormat;
import clowdtech.printer.formatinfo.PrintFormatInfoReceipt;

public class StarLineReceipt extends StarLineTransactionBase {
    private Context context;
    private String portName;
    private String portSettings;
    private PrintFormatInfoReceipt formatInfo;

    public StarLineReceipt(Context context, String portName, String portSettings, PrintFormatInfoReceipt formatInfo) {
        this.context = context;
        this.portName = portName;
        this.portSettings = portSettings;
        this.formatInfo = formatInfo;
    }

    private static void printDocumentIdentifier(IReceiptFormat formatInfo, ArrayList<byte[]> list) {
        list.add(new byte[]{0x1b, 0x45}); // bold

        list.add(String.format("SALE: %s\r\n", formatInfo.getIdentifier()).getBytes());

        list.add(new byte[]{0x1b, 0x46}); // bold off
    }

    private static void printPaymentInfo(IReceiptFormat formatInfo, ArrayList<byte[]> list) {
        list.add(String.format("Charge\r\n%s\r\n", formatInfo.getTotal()).getBytes());


        list.add(String.format("%s\r\n\r\n", formatInfo.getPaymentInfo()).getBytes());
    }

    public void invoke() throws StarIOPortException {
        ArrayList<byte[]> list = new ArrayList<>();

        printHeader(formatInfo, list);

        printDocumentIdentifier(formatInfo, list);

        printLines(formatInfo, list);

        printGrandTotal(formatInfo, list);

        printPaymentInfo(formatInfo, list);

        printFooter(formatInfo, list);

        list.add(COMMAND_CUT); // Cut

        sendCommand(context, portName, portSettings, list);
    }
}
