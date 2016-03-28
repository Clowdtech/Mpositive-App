package clowdtech.mpositive.receipt;

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
import clowdtech.printer.StarIoPrintController;
import clowdtech.printer.formatinfo.PrintFormatInfoReceipt;

public class PrintReceiptAsync extends AsyncTask<Void, Void, String> {
    private final Context context;
    private String printerType;
    private final PrintFormatInfoReceipt formatInfo;
    private final TaskListener listener;
    private final Resources resources;

    public PrintReceiptAsync(Context context, String printerType, PrintFormatInfoReceipt formatInfo, TaskListener listener){
        this.context = context;
        this.printerType = printerType;
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
            controller.printReceipt(this.context, printerType, formatInfo);
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

        if (listener != null)
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
