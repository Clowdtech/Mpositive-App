package clowdtech.printer;

import android.content.Context;

import com.starmicronics.stario.StarIOPortException;

import clowdtech.printer.formatinfo.PrintFormatInfoOrder;
import clowdtech.printer.formatinfo.PrintFormatInfoReceipt;

public interface IPrintController { //TODO: there should be no StarIOPortExceptions thrown on the interface
    void openDrawer(Context context) throws StarIOPortException;

    void printReceipt(Context context, String printerType, PrintFormatInfoReceipt formatInfo) throws StarIOPortException;

    void printTransactionsReport(Context context, TransactionsReportFormatInfo formatInfo) throws StarIOPortException;

    void printProductReport(Context context, ProductReportFormatInfo formatInfo) throws StarIOPortException;

    void printOrder(Context context, String printerType, PrintFormatInfoOrder formatInfo) throws StarIOPortException;
}
