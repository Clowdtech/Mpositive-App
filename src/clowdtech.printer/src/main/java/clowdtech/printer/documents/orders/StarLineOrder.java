package clowdtech.printer.documents.orders;

import android.content.Context;

import com.starmicronics.stario.StarIOPortException;

import java.util.ArrayList;

import clowdtech.printer.documents.StarLineTransactionBase;
import clowdtech.printer.formatinfo.PrintFormatInfoOrder;

public class StarLineOrder extends StarLineTransactionBase {
    private Context context;
    private String portName;
    private String portSettings;
    private PrintFormatInfoOrder formatInfo;

    public StarLineOrder(Context context, String portName, String portSettings, PrintFormatInfoOrder formatInfo) {
        this.context = context;
        this.portName = portName;
        this.portSettings = portSettings;
        this.formatInfo = formatInfo;
    }

    public void invoke() throws StarIOPortException {
        ArrayList<byte[]> list = new ArrayList<>();

        printHeader(formatInfo, list);

        printLines(formatInfo, list);

        printGrandTotal(formatInfo, list);

        printFooter(formatInfo, list);

        list.add(COMMAND_CUT); // Cut

        sendCommand(context, portName, portSettings, list);
    }
}