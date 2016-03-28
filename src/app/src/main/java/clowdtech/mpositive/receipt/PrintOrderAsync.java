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
import clowdtech.printer.IPrintController;
import clowdtech.printer.formatinfo.PrintFormatInfoOrder;
import clowdtech.printer.StarIoPrintController;

public class PrintOrderAsync extends AsyncTask<Void, Void, String> {
    private final Context context;
    private String printerType;
    private final PrintFormatInfoOrder formatInfo;
    private final Resources resources;

    public PrintOrderAsync(Context context, String printerType, PrintFormatInfoOrder formatInfo){
        this.context = context;
        this.printerType = printerType;
        this.resources = context.getResources();
        this.formatInfo = formatInfo;
    }

    @Override
    protected String doInBackground(Void... params) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.context);

        String deviceName = prefs.getString(resources.getString(R.string.preference_printer_name_key), resources.getString(R.string.preference_printer_name_default));
        String commType = prefs.getString(resources.getString(R.string.preference_printer_connection_key), resources.getString(R.string.preference_printer_connection_default));
        String retryType = prefs.getString(resources.getString(R.string.preference_printer_retry_key), resources.getString(R.string.preference_printer_retry_default));

        IPrintController controller = new StarIoPrintController(deviceName, Integer.valueOf(commType), Integer.valueOf(retryType));

        try {
            controller.printOrder(this.context, printerType, formatInfo);
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
