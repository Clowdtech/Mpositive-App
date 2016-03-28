package clowdtech.printer.documents;

import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;

import clowdtech.printer.ReceiptLineInfo;
import clowdtech.printer.formatinfo.IOrderFormat;

public class StarLineTransactionBase extends StarDocument {
    private void printSeparator(ArrayList<byte[]> list) {
        list.add("------------------------------------------------\r\n\r\n".getBytes());
    }

    protected void printHeader(IOrderFormat formatInfo, ArrayList<byte[]> list) {
        list.add(new byte[]{0x1b, 0x1d, 0x61, 0x01}); // Alignment (center)

        // list.add("[If loaded.. Logo1 goes here]\r\n".getBytes());

        // list.add(new byte[]{0x1b, 0x1c, 0x70, 0x01, 0x00, '\r', '\n'}); //Stored Logo Printing

        list.add(String.format("\n%s\r\n", formatInfo.getHeader()).getBytes());
//                list.add("123 Star Road\r\nCity, State 12345\r\n\r\n".getBytes());

        list.add(new byte[]{0x1b, 0x1d, 0x61, 0x00}); // Alignment

        list.add(new byte[]{0x1b, 0x44, 0x02, 0x10, 0x22, 0x00}); // Set horizontal tab

        list.add(String.format("Date: %s", DateTimeFormat.forPattern("dd/MM/yyyy").print(formatInfo.getDate())).getBytes());

        list.add(new byte[]{' ', 0x09, ' '}); // Moving Horizontal Tab

        list.add(String.format("Time:%s\r\n", DateTimeFormat.forPattern("hh:mm a").print(formatInfo.getDate())).getBytes());

        printSeparator(list);
    }

    protected void printLines(IOrderFormat formatInfo, ArrayList<byte[]> list) {
        //char limits
        //total - 48
        //description - 33
        //line count - 5
        //total - 10

        // Notice that we use a unicode representation because that is
        // how Java expresses these bytes as double byte unicode
        // This will TAB to the next horizontal position
        list.add(String.format("%1$-" + 33 + "s%2$-" + 5 + "s%3$-" + 10 + "s", "Description", "", "Total").getBytes());

        for (ReceiptLineInfo line : formatInfo.getLines()) {
            String description = line.getDescription().length() > 33 ? line.getDescription().substring(0, 32) : line.getDescription();

            list.add(String.format("%1$-" + 33 + "s%2$-" + 5 + "s%3$-" + 10 + "s", description, line.getLineCount(), line.getLineCost()).getBytes());
        }

        printSeparator(list);
    }

    protected void printGrandTotal(IOrderFormat formatInfo, ArrayList<byte[]> list) {
        list.add("Total".getBytes());

        // Character expansion
        list.add(new byte[]{0x06, 0x09, 0x1b, 0x69, 0x01, 0x01});

        list.add(String.format("\u0009%s\r\n", formatInfo.getTotal()).getBytes());

        list.add(new byte[]{0x1b, 0x69, 0x00, 0x00}); // Cancel Character Expansion

        printSeparator(list);
    }

    protected void printFooter(IOrderFormat formatInfo, ArrayList<byte[]> list) {
        list.add(new byte[]{0x1b, 0x1d, 0x61, 0x01}); // Alignment (center)

        list.add(String.format("%s\r\n\r\n", formatInfo.getFooter()).getBytes());

        list.add(new byte[]{0x1b, 0x1d, 0x61, 0x00}); // Alignment
    }
}
