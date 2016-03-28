package clowdtech.mpositive.areas.settings;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;

import java.util.HashMap;
import java.util.Map;

import clowdtech.printer.StarIoPrintController;

public class PrintersPreference extends ListPreference {
    public PrintersPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        Map<String, String> printers = new HashMap<>();

        printers.put("USB", "USB:");

        printers.putAll(StarIoPrintController.getAvailablePrinters());

        int printerCount = printers.size();

        CharSequence[] entries = new CharSequence[printerCount];
        CharSequence[] entryValues = new CharSequence[printerCount];

        int i = 0;
        for (String printer : printers.values()) {
            entries[i] = printer;
            entryValues[i] = printer;

            i++;
        }

        setEntries(entries);
        setEntryValues(entryValues);
    }
}
