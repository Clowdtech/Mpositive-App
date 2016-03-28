package clowdtech.printer;

public class ReceiptLineInfo {
    private final int lineCount;
    private final String description;
    private final String lineCost;

    public ReceiptLineInfo(int lineCount, String description, String lineCost) {
        this.lineCount = lineCount;
        this.description = description;
        this.lineCost = lineCost;
    }

    public String getLineCount() {
        return String.valueOf(lineCount);
    }

    public String getDescription() {
        return description;
    }

    public String getLineCost() {
        return lineCost;
    }
}
