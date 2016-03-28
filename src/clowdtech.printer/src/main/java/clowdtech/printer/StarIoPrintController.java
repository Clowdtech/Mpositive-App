package clowdtech.printer;

import android.content.Context;

import com.starmicronics.stario.PortInfo;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import clowdtech.printer.formatinfo.PrintFormatInfoOrder;
import clowdtech.printer.formatinfo.PrintFormatInfoReceipt;

public class StarIoPrintController implements IPrintController {
    private final String portName;
    private final String portSettings;

    public StarIoPrintController(String portName, int commType, int retry){
        this.portName = portName;

        if (portName.toLowerCase().contains("usb")) {
            portSettings = "";
        } else {
            portSettings = getPortSettingsOption(commType, retry);
        }
    }

    public static Map<String, String> getAvailablePrinters() {

        Map<String, String> map = new HashMap<>();

        List<PortInfo> BTPortList;

        final ArrayList<PortInfo> arrayDiscovery;

        arrayDiscovery = new ArrayList<>();

        try {
            BTPortList = StarIOPort.searchPrinter("BT:");

            for (PortInfo portInfo : BTPortList) {
                arrayDiscovery.add(portInfo);
            }

            for (PortInfo discovery : arrayDiscovery) {
                if (discovery.getMacAddress().equals("")) {
                    continue;
                }

                map.put(discovery.getMacAddress(), discovery.getPortName());
            }
        } catch (StarIOPortException e) {
            e.printStackTrace();
        }

        return map;
    }

    @Override
    public void openDrawer(Context context) throws StarIOPortException {
        PrinterFunctions.OpenCashDrawer(context, portName, portSettings);
    }

    public void printReceipt(Context context, String printerType, PrintFormatInfoReceipt formatInfo) throws StarIOPortException {
        PrinterFunctions.PrintReceipt(context, portName, portSettings, printerType, "3inch (80mm)", formatInfo);
    }

    @Override
    public void printOrder(Context context, String printerType, PrintFormatInfoOrder formatInfo) throws StarIOPortException {
        PrinterFunctions.PrintOrder(context, portName, portSettings, printerType, "3inch (80mm)", formatInfo);
    }

    @Override
    public void printTransactionsReport(Context context, TransactionsReportFormatInfo formatInfo) throws StarIOPortException {
        PrinterFunctions.PrintTransactionsReport(context, portName, portSettings, formatInfo);
    }

    @Override
    public void printProductReport(Context context, ProductReportFormatInfo formatInfo) throws StarIOPortException {
        PrinterFunctions.PrintProductReport(context, portName, portSettings, formatInfo);
    }

    private static String getPortSettingsOption(int commType, int retry) {
        String portSettings = "";

        portSettings += getBluetoothCommunicationType(commType); // Bluetooth option of "portSettings" must be last.
        portSettings += getBluetoothRetrySettings(retry);

        return portSettings;
    }

    private static String getBluetoothCommunicationType(int commType) {
        String portSettings = "";

        switch (commType) {
            case 0:
                portSettings = "";
                break;
            case 1:
                portSettings = ";p";
                break;
        }

        return portSettings;
    }

    private static String getBluetoothRetrySettings(int retry) {
        String retrySetting = "";

        switch (retry) {
            case 0:
                retrySetting = "";
                break;
            case 1:
                retrySetting = ";l";
                break;
        }

        return retrySetting;
    }
}

