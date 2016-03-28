package clowdtech.mpositive.areas.preferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.text.TextUtils;

import javax.inject.Inject;

import clowdtech.mpositive.App;
import clowdtech.mpositive.R;
import clowdtech.mpositive.queue.IEventBus;
import clowdtech.mpositive.queue.events.PreferenceChangedEvent;

public class PreferencesFrag extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private PreferenceCategory printerCategory;

    private Preference printerName;
    private Preference printerConnection;
    private Preference printerRetry;

    private PreferenceScreen preferenceScreen;

    @Inject
    IEventBus eventBus;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((App)getActivity().getApplicationContext()).getApplicationComponent().inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);

        setPreferenceSummary(getString(R.string.preference_printer_name_key));

        setPreferenceSummary(getString(R.string.preference_printer_connection_key));

        setPreferenceSummary(getString(R.string.preference_printer_retry_key));

        preferenceScreen = getPreferenceScreen();

        printerName = findPreference(getString(R.string.preference_printer_name_key));
        printerConnection = findPreference(getString(R.string.preference_printer_connection_key));
        printerRetry = findPreference(getString(R.string.preference_printer_retry_key));

        setPrinterOptionsVisibility();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        setPreferenceSummary(key);

        if (key.equals(getString(R.string.preference_printer_integration_key))) {
            setPrinterOptionsVisibility();

            eventBus.post(new PreferenceChangedEvent<>(PreferenceEntry.PrinterIntegration, sharedPreferences.getBoolean(key, getResources().getBoolean(R.bool.preference_printer_integration_default))));
        } else if (key.equals(getString(R.string.preference_printer_cashdrawer_key))) {
            eventBus.post(new PreferenceChangedEvent<>(PreferenceEntry.CashDrawerIntegration, sharedPreferences.getBoolean(key, getResources().getBoolean(R.bool.preference_printer_cashdrawer_default))));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

        super.onPause();
    }

    private void setPrinterOptionsVisibility() {
        printerCategory = (PreferenceCategory) findPreference(getString(R.string.preference_printer_group_key));

        if (!((CheckBoxPreference) findPreference(getString(R.string.preference_printer_integration_key))).isChecked()) {
            printerCategory.removePreference(printerName);
            printerCategory.removePreference(printerConnection);
            printerCategory.removePreference(printerRetry);
        } else {
            printerCategory.addPreference(printerName);
            printerCategory.addPreference(printerConnection);
            printerCategory.addPreference(printerRetry);
        }
    }

    private void setPreferenceSummary(String key) {
        Preference pref = findPreference(key);
        if (pref instanceof ListPreference) {
            // Update display title
            // Write the description for the newly selected preference
            // in the summary field.
            ListPreference listPref = (ListPreference) pref;
            CharSequence listDesc = listPref.getEntry();
            if (!TextUtils.isEmpty(listDesc)) {
                pref.setSummary(listDesc);
            }
        }
    }
}
