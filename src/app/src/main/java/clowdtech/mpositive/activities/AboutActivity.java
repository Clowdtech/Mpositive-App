package clowdtech.mpositive.activities;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import clowdtech.mpositive.R;
import clowdtech.mpositive.tracking.TrackingConstants;

public class AboutActivity extends BaseActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_activity_about);

        // Set a toolbar to replace the action category.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView dbVersion = (TextView)findViewById(R.id.about_database_version);
        TextView appVersion = (TextView)findViewById(R.id.about_app_version);

        ApplicationInfo ai = null;
        PackageInfo pInfo = null;

        try {
            ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (ai != null) {
            Bundle bundle = ai.metaData;

            int activeAndroidDbVersion = bundle.getInt("AA_DB_VERSION");

            dbVersion.setText(String.valueOf(activeAndroidDbVersion));
        }

        if (pInfo != null) {
            appVersion.setText(pInfo.versionName + "." + String.valueOf(pInfo.versionCode));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        trackScreenView(TrackingConstants.ScreenNames.About);
    }
}
