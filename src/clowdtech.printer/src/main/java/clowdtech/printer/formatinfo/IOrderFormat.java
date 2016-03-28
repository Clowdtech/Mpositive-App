package clowdtech.printer.formatinfo;

import org.joda.time.DateTime;

import java.util.List;

import clowdtech.printer.ReceiptLineInfo;

/**
 * Created by tom on 26/12/15.
 */
public interface IOrderFormat {
    String getHeader();

    String getFooter();

    List<ReceiptLineInfo> getLines();

    DateTime getDate();

    String getTotal();
}
