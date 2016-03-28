package clowdtech.printer.formatinfo;

import org.joda.time.DateTime;

import java.util.List;

import clowdtech.printer.ReceiptLineInfo;

public interface IReceiptFormat extends IOrderFormat {

    String getPaymentInfo();

    String getIdentifier();
}
