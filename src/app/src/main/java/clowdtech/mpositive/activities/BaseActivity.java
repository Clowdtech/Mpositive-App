package clowdtech.mpositive.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import clowdtech.mpositive.App;
import clowdtech.mpositive.ioc.components.DaggerCheckoutComponent;
import clowdtech.mpositive.ioc.modules.ActivityModule;
import clowdtech.mpositive.tracking.TrackingCategories;

public class BaseActivity extends FragmentActivity {
    protected Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get a Tracker (should auto-report)
        App application = (App) getApplication();

        tracker = application.getTracker(App.TrackerName.APP_TRACKER);

        application.setCheckoutComponent(DaggerCheckoutComponent.builder()
                .applicationComponent(application.getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build());
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Get an Analytics tracker to report app starts and uncaught exceptions etc.
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Stop the analytics tracking
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    protected void noActionBar() {
        ActionBar ab = getActionBar();

        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(false);
            ab.setHomeButtonEnabled(false);
            ab.setDisplayShowHomeEnabled(false);
        }
    }

    protected void trackScreenView(String screenName) {
        // Set screen name.
        tracker.setScreenName(screenName);

        // Send a screen view.
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    protected void trackEvent(TrackingCategories category, String action, String label) {
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category.toString())
                .setAction(action)
                .setLabel(label)
                .build());
    }
}
