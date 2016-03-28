package clowdtech.mpositive.areas.reporting.transaction;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import org.joda.time.DateTime;

import java.io.File;

public class ShareController implements IShareController {
    @Override
    public void shareTransactionsReport(Context context, String period, String csvReport) {
        String to = "";
        String subject = "MPOSitive - Transactions Report";
        String message = "See attached for a transactional summary of " + period;

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("application/csv");
        String directoryPath = Environment.getExternalStorageDirectory() + File.separator + "MPOSitive" + File.separator + "TransactionReport";

        File directory = new File(directoryPath);

        DeleteRecursive(directory);

        if (!directory.exists())
            directory.mkdirs();

        String temp_path = String.format("%s/TransactionReport-%s_%s.csv", directoryPath, period, DateTime.now().getMillis());
        File file = new File(temp_path);


        GenerateCsv.generateCsvFile(file, csvReport);

        i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        i.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT, message);

        context.startActivity(Intent.createChooser(i, "E-mail"));
    }

    void DeleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                DeleteRecursive(child);

        fileOrDirectory.delete();
    }
}
