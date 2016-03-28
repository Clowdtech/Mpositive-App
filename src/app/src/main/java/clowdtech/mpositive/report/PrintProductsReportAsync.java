package clowdtech.mpositive.report;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.starmicronics.stario.StarIOPortException;

import clowdtech.mpositive.R;
import clowdtech.mpositive.TaskListener;
import clowdtech.printer.IPrintController;
import clowdtech.printer.ProductReportFormatInfo;
import clowdtech.printer.StarIoPrintController;

public class PrintProductsReportAsync extends AsyncTask<Void, Void, String> {
    private final Context context;
    private final ProductReportFormatInfo formatInfo;
    private final TaskListener listener;
    private final Resources resources;

    public PrintProductsReportAsync(Context context, ProductReportFormatInfo formatInfo, TaskListener listener){
        this.context = context;
        this.resources = context.getResources();
        this.formatInfo = formatInfo;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... params) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.context);

        String deviceName = prefs.getString(resources.getString(R.string.preference_printer_name_key), resources.getString(R.string.preference_printer_name_default));
        String commType = prefs.getString(resources.getString(R.string.preference_printer_connection_key), resources.getString(R.string.preference_printer_connection_default));
        String retryType = prefs.getString(resources.getString(R.string.preference_printer_retry_key), resources.getString(R.string.preference_printer_retry_default));

        IPrintController controller = new StarIoPrintController(deviceName, Integer.valueOf(commType), Integer.valueOf(retryType));

        try {
            controller.printProductReport(this.context, formatInfo);
        } catch (StarIOPortException e) {
            e.printStackTrace();

            return e.getMessage();
        }

        return "success";
    }

    @Override
    protected void onPostExecute(String result) {
        if(Activity.class.isInstance(context) && ((Activity) context).isFinishing())
        {
            return;
        }

        boolean success = result.equals("success");

        listener.onFinished(success);

        if (!success) {
            Builder dialog = new Builder(context);
            dialog.setNegativeButton("OK", null);
            AlertDialog alert = dialog.create();
            alert.setTitle("Failure");
            alert.setMessage(result);
            alert.setCancelable(false);
            alert.show();
        }
    }
}
