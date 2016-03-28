package clowdtech.printer;

import android.content.Context;

import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.stario.StarPrinterStatus;

import java.util.ArrayList;
import java.util.List;

import clowdtech.printer.documents.orders.StarLineOrder;
import clowdtech.printer.documents.orders.StarRasterOrder;
import clowdtech.printer.documents.receipts.StarLineReceipt;
import clowdtech.printer.documents.receipts.StarRasterReceipt;
import clowdtech.printer.formatinfo.PrintFormatInfoOrder;
import clowdtech.printer.formatinfo.PrintFormatInfoReceipt;

public class PrinterFunctions {
    public static final byte[] COMMAND_CUT = new byte[]{0x1b, 0x64, 0x02};
    public static final byte[] COMMAND_POP = new byte[]{0x07};

    public static void OpenCashDrawer(Context context, String portName, String portSettings)
            throws StarIOPortException {
        ArrayList<byte[]> commands = new ArrayList<>();

        commands.add(COMMAND_POP);

        sendCommand(context, portName, portSettings, commands);
    }

    //TODO: introduce ioc and have the correct implementation (based on preferences) injected
    public static void PrintReceipt(Context context, String portName, String portSettings, String commandType, String strPrintArea, PrintFormatInfoReceipt formatInfo)
            throws StarIOPortException {

        // THE WAY THE RECEIPT IS FORMATTED SHOULD BE "QUITE CLOSE" TO THAT OF ANY OTHER ELECTRONIC FORMAT

        if (commandType.equals("Line") || strPrintArea.equals("4inch (112mm)")) {
            new StarLineReceipt(context, portName, portSettings, formatInfo).invoke();
        } else if (commandType.equals("Raster")) {
            new StarRasterReceipt(context, portName, portSettings, formatInfo).invoke();
        }
    }

    public static void PrintOrder(Context context, String portName, String portSettings, String commandType, String strPrintArea, PrintFormatInfoOrder formatInfo)
            throws StarIOPortException {
        if (commandType.equals("Line") || strPrintArea.equals("4inch (112mm)")) {
            new StarLineOrder(context, portName, portSettings, formatInfo).invoke();
        } else if (commandType.equals("Raster")) {
            new StarRasterOrder(context, portName, portSettings, formatInfo).invoke();
        }
    }


    public static void PrintTransactionsReport(Context context, String portName, String portSettings, TransactionsReportFormatInfo formatInfo) throws StarIOPortException {
        ArrayList<byte[]> list = new ArrayList<>();

        list.add(new byte[]{0x1b, 0x1d, 0x61, 0x01}); // Alignment (center)

        list.add(String.format("\n%s\r\n", formatInfo.getHeader()).getBytes());

        list.add(String.format("\n%s\r\n\r\n", formatInfo.getSubHeader()).getBytes());

        list.add(new byte[]{0x1b, 0x1d, 0x61, 0x00}); // Alignment

        list.add(new byte[]{0x1b, 0x44, 0x02, 0x10, 0x22, 0x00}); // Set horizontal tab


        // table headers

        list.add(new byte[]{0x1b, 0x45}); // bold

        int charLimit = 48 / formatInfo.getReportHeaders().length;

        for (String header : formatInfo.getReportHeaders()) {
            String description = header.length() > charLimit ? header.substring(0, charLimit - 1) : header;

            list.add(String.format("%1$-" + charLimit + "s", description).getBytes());
        }

        list.add("-----------------------------------------------\r\n".getBytes());

        list.add(new byte[]{0x1b, 0x46}); // bold off


        // table contents

        for (String[] rowItems : formatInfo.getReportRows()) {
            for (String item : rowItems) {
                String description = item.length() > charLimit ? item.substring(0, charLimit - 1) : item;

                list.add(String.format("%1$-" + charLimit + "s", description).getBytes());
            }
        }

        // summary

        list.add("\r\n\r\n".getBytes());

        list.add(String.format("%s: %s (avg. %s)\r\n", formatInfo.TransactionCountCaption, formatInfo.TransactionCount, formatInfo.AverageValue).getBytes());

        printLeftAndRightAligned(list, formatInfo.AmountCardCaption, formatInfo.AmountCard);

        printLeftAndRightAligned(list, formatInfo.AmountCashCaption, formatInfo.AmountCash);

        printLeftAndRightAligned(list, formatInfo.AmountOtherCaption, formatInfo.AmountOther);

        // footer

        list.add(new byte[]{0x1b, 0x1d, 0x61, 0x01}); // Alignment (center)

        list.add(String.format("\r\n\r\n%s\r\n\r\n", formatInfo.getFooter()).getBytes());

        list.add(new byte[]{0x1b, 0x1d, 0x61, 0x00}); // Alignment

        list.add(COMMAND_CUT); // Cut

        sendCommand(context, portName, portSettings, list);
    }

    public static void PrintProductReport(Context context, String portName, String portSettings, ProductReportFormatInfo formatInfo) throws StarIOPortException {
        ArrayList<byte[]> list = new ArrayList<>();

        list.add(new byte[]{0x1b, 0x1d, 0x61, 0x01}); // Alignment (center)

        list.add(String.format("\n%s\r\n\r\n", formatInfo.getHeader()).getBytes());

        list.add(new byte[]{0x1b, 0x1d, 0x61, 0x00}); // Alignment

        list.add(new byte[]{0x1b, 0x44, 0x02, 0x10, 0x22, 0x00}); // Set horizontal tab


        // table headers

        list.add(new byte[]{0x1b, 0x45}); // bold

        int charLimit = 48 / formatInfo.getReportHeaders().length;

        for (String header : formatInfo.getReportHeaders()) {
            String description = header.length() > charLimit ? header.substring(0, charLimit - 1) : header;

            list.add(String.format("%1$-" + charLimit + "s", description).getBytes());
        }

        list.add("-----------------------------------------------\r\n".getBytes());

        list.add(new byte[]{0x1b, 0x46}); // bold off


        // table contents

        for (String[] rowItems : formatInfo.getReportRows()) {
            for (String item : rowItems) {
                String description = item.length() > charLimit ? item.substring(0, charLimit - 1) : item;

                list.add(String.format("%1$-" + charLimit + "s", description).getBytes());
            }
        }


        // footer

        list.add(new byte[]{0x1b, 0x1d, 0x61, 0x01}); // Alignment (center)

        list.add(String.format("\r\n\r\n%s\r\n\r\n", formatInfo.getFooter()).getBytes());

        list.add(new byte[]{0x1b, 0x1d, 0x61, 0x00}); // Alignment

        list.add(COMMAND_CUT); // Cut

        sendCommand(context, portName, portSettings, list);
    }

    private static void printLeftAndRightAligned(ArrayList<byte[]> list, String leftAligned, String rightAligned) {
        list.add(new byte[]{0x1b, 0x1d, 0x61, 0x00}); // Alignment

        list.add(new byte[]{0x1b, 0x44, 0x02, 0x10, 0x22, 0x00}); // Set horizontal tab

        list.add(leftAligned.getBytes());

        list.add(new byte[]{' ', 0x09, ' '}); // Moving Horizontal Tab

        list.add(String.format("%s\r\n", rightAligned).getBytes());
    }

    private static byte[] convertFromListByteArrayTobyteArray(List<byte[]> ByteArray) {
        int dataLength = 0;
        for (int i = 0; i < ByteArray.size(); i++) {
            dataLength += ByteArray.get(i).length;
        }

        int distPosition = 0;
        byte[] byteArray = new byte[dataLength];
        for (int i = 0; i < ByteArray.size(); i++) {
            System.arraycopy(ByteArray.get(i), 0, byteArray, distPosition, ByteArray.get(i).length);
            distPosition += ByteArray.get(i).length;
        }

        return byteArray;
    }

    private static void sendCommand(Context context, String portName, String portSettings, ArrayList<byte[]> byteList) throws StarIOPortException {
        StarIOPort port = null;
        try {
            /*
             * using StarIOPort3.1.jar (support USB Port) Android OS Version: upper 2.2
			 */
            port = StarIOPort.getPort(portName, portSettings, 10000, context);
            /*
             * using StarIOPort.jar Android OS Version: under 2.1 port = StarIOPort.getPort(portName, portSettings, 10000);
			 */
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }

			/*
			 * Using Begin / End Checked Block method When sending large amounts of raster data,
			 * adjust the value in the timeout in the "StarIOPort.getPort" in order to prevent
			 * "timeout" of the "endCheckedBlock method" while a printing.
			 *
			 * If receipt print is success but timeout error occurs(Show message which is "There
			 * was no response of the printer within the timeout period." ), need to change value
			 * of timeout more longer in "StarIOPort.getPort" method.
			 * (e.g.) 10000 -> 30000
			 */
            StarPrinterStatus status = port.beginCheckedBlock();

            if (status.offline) {
                throw new StarIOPortException("A printer is offline");
            }

            byte[] commandToSendToPrinter = convertFromListByteArrayTobyteArray(byteList);
            port.writePort(commandToSendToPrinter, 0, commandToSendToPrinter.length);

            port.setEndCheckedBlockTimeoutMillis(30000);// Change the timeout time of endCheckedBlock method.
            status = port.endCheckedBlock();

            if (status.coverOpen) {
                throw new StarIOPortException("Printer cover is open");
            } else if (status.receiptPaperEmpty) {
                throw new StarIOPortException("Receipt paper is empty");
            } else if (status.offline) {
                throw new StarIOPortException("Printer is offline");
            }
//		} catch (StarIOPortException e) {
//			Builder dialog = new Builder(context);
//			dialog.setNegativeButton("OK", null);
//			AlertDialog alert = dialog.create();
//			alert.setTitle("Failure");
//			alert.setMessage(e.getMessage());
//			alert.setCancelable(false);
//			alert.show();
        } finally {
            if (port != null) {
                try {
                    StarIOPort.releasePort(port);
                } catch (StarIOPortException ignored) {
                }
            }
        }
    }
}