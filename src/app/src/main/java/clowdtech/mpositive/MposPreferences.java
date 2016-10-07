package clowdtech.mpositive;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import clowdtech.mpositive.R;


public class MposPreferences implements ISharedPreferences {
    private final String printCommandType;
    private final String header;
    private final String footer;
    private final String deviceName;
    private boolean cashDrawerIntegrated;
    private boolean printerIntegrated;
    private int printerCommunicationType;
    private int printerRetry;

    private Context context;

    public MposPreferences(Context context, Resources resources) {
        this.context = context;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        printCommandType = preferences.getString(resources.getString(R.string.preference_printer_command_key), "");
        header = preferences.getString(resources.getString(R.string.preference_receipt_header), "");
        footer = preferences.getString(resources.getString(R.string.preference_receipt_footer), "");
        deviceName = preferences.getString(resources.getString(R.string.preference_printer_name_key), resources.getString(R.string.preference_printer_name_default));
        printerCommunicationType = Integer.valueOf(preferences.getString(resources.getString(R.string.preference_printer_connection_key), resources.getString(R.string.preference_printer_connection_default)));
        printerRetry = Integer.valueOf(preferences.getString(resources.getString(R.string.preference_printer_retry_key), resources.getString(R.string.preference_printer_retry_default)));

        setCashDrawerIntegration(preferences);

        setPrinterIntegration(preferences);
    }

    @Override
    public boolean getCashDrawerIntegrated() {
        return cashDrawerIntegrated;
    }

    @Override
    public boolean getPrinterIntegration() {
        return printerIntegrated;
    }

    @Override
    public String getPrinterCommandType() {
        return printCommandType;
    }

    @Override
    public String getPrinterHeader() {
        return header;
    }

    @Override
    public String getPrinterFooter() {
        return footer;
    }

    @Override
    public String getPrinterName() {
        return deviceName;
    }

    @Override
    public int getPrinterCommunicationType() {
        return printerCommunicationType;
    }

    @Override
    public int getRetry() {
        return printerRetry;
    }

    public void setCashDrawerIntegrated(boolean cashDrawerIntegrated) {
        this.cashDrawerIntegrated = cashDrawerIntegrated;
    }

    public void setPrinterIntegrated(boolean printerIntegrated) {
        this.printerIntegrated = printerIntegrated;
    }

    private void setCashDrawerIntegration(SharedPreferences prefs) {
        String key = context.getString(R.string.preference_printer_cashdrawer_key);

        boolean defaultValue = context.getResources().getBoolean(R.bool.preference_printer_cashdrawer_default);

        setCashDrawerIntegrated(prefs.getBoolean(key, defaultValue));
    }

    private void setPrinterIntegration(SharedPreferences prefs) {
        String key = context.getString(R.string.preference_printer_integration_key);

        boolean defaultValue = context.getResources().getBoolean(R.bool.preference_printer_integration_default);

        setPrinterIntegrated(prefs.getBoolean(key, defaultValue));
    }
}
