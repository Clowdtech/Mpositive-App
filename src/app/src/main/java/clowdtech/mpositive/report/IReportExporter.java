package clowdtech.mpositive.report;

import java.util.List;

import clowdtech.mpositive.TaskListener;
import clowdtech.mpositive.data.transactions.entities.Receipt;

public interface IReportExporter {
    void printReport(List<Receipt> receipts);

    void printReport(List<Receipt> receipts, TaskListener listener);
}
