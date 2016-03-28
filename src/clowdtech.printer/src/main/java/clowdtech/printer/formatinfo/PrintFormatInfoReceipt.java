package clowdtech.printer.formatinfo;

import org.joda.time.DateTime;

import java.util.List;

import clowdtech.printer.ReceiptLineInfo;

public class PrintFormatInfoReceipt extends PrintFormatInfoOrder implements IReceiptFormat {
    private long invoiceNumber;
    private String paymentInfo;

    public PrintFormatInfoReceipt(String header, DateTime created, List<ReceiptLineInfo> lines, String footer, long invoiceNumber, String paymentInfo, String total) {
        super(header, created, lines, footer, total);

        this.invoiceNumber = invoiceNumber;
        this.paymentInfo = paymentInfo;
    }

    @Override
    public String getPaymentInfo() {
        return paymentInfo;
    }

    @Override
    public String getIdentifier() {
        return String.valueOf(this.invoiceNumber);
    }
}
