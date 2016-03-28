package clowdtech.mpositive.report;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.AsyncTask;

import com.starmicronics.stario.StarIOPortException;

import clowdtech.printer.IPrintController;
import clowdtech.printer.TransactionsReportFormatInfo;

public class PrintTransactionsReportAsync extends AsyncTask<TransactionsReportFormatInfo, Void, String> {
    private Context context;
    private IPrintController printController;

    public PrintTransactionsReportAsync(Context context, IPrintController printController) {
        this.context = context;
        this.printController = printController;
    }

    @Override
    protected String doInBackground(TransactionsReportFormatInfo... params) {

        try {
            printController.printTransactionsReport(this.context, params[0]);
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
