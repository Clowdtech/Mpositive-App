package clowdtech.printer;

import java.math.BigDecimal;
import java.util.List;

public class TransactionsReportFormatInfo {
    private String header;
    private String subHeader;
    private String footer;
    private String[] reportHeaders;
    private List<String[]> reportRows;

    public String AmountCashCaption;
    public String AmountCardCaption;
    public String AmountOtherCaption;
    public String AmountCash;
    public String AmountCard;
    public String AmountOther;
    public String TransactionCountCaption;
    public int TransactionCount;
    public String AverageValue;
    public String AverageCaption;

    public TransactionsReportFormatInfo(String header, String subHeader, String[] reportHeaders, List<String[]> reportRows, String footer) {
        this.header = header;
        this.subHeader = subHeader;
        this.footer = footer;
        this.reportHeaders = reportHeaders;
        this.reportRows = reportRows;
    }

    public String getHeader() {
        return header;
    }

    public String getSubHeader() {
        return subHeader;
    }

    public String getFooter() {
        return footer;
    }

    public String[] getReportHeaders() {
        return reportHeaders;
    }

    public List<String[]> getReportRows() {
        return reportRows;
    }
}
