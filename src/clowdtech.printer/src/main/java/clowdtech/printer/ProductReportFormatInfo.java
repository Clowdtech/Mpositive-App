package clowdtech.printer;

import java.util.List;

public class ProductReportFormatInfo {
    private String header;
    private String footer;
    private String[] reportHeaders;
    private List<String[]> reportRows;

    public ProductReportFormatInfo(String header, String[] reportHeaders, List<String[]> reportRows, String footer) {
        this.header = header;
        this.footer = footer;
        this.reportHeaders = reportHeaders;
        this.reportRows = reportRows;
    }

    public String getHeader() {
        return header;
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
