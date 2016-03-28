package clowdtech.printer.formatinfo;

import org.joda.time.DateTime;

import java.util.List;

import clowdtech.printer.ReceiptLineInfo;

public class PrintFormatInfoOrder implements IOrderFormat {
    private final String header;
    private DateTime date;
    private List<ReceiptLineInfo> lines;
    private final String footer;
    private String total;

    public PrintFormatInfoOrder(String header, DateTime date, List<ReceiptLineInfo> lines, String footer, String total) {
        this.header = header;
        this.date = date;
        this.lines = lines;
        this.footer = footer;
        this.total = total;
    }

    @Override
    public String getHeader() {
        return header;
    }

    @Override
    public String getFooter() {
        return footer;
    }

    @Override
    public List<ReceiptLineInfo> getLines() {
        return lines;
    }

    @Override
    public DateTime getDate() {
        return date;
    }

    @Override
    public String getTotal() {
        return total;
    }
}
