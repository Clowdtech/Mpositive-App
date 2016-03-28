package clowdtech.mpositive.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import clowdtech.mpositive.App;
import clowdtech.mpositive.ioc.components.DaggerCheckoutComponent;
import clowdtech.mpositive.ioc.modules.ActivityModule;
import clowdtech.mpositive.tracking.TrackingCategories;

public class BaseActionBarActivity extends ActionBarActivity {
    private Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get a Tracker (should auto-report)
        tracker = ((App) getApplication()).getTracker(App.TrackerName.APP_TRACKER);

        App application = (App) getApplication();

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

    protected void trackScreenView(String screenName) {
        // Set screen name.
        tracker.setScreenName(screenName);

        // Send a screen view.
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void trackEvent(TrackingCategories category, String action, String label) {
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category.toString())
                .setAction(action)
                .setLabel(label)
                .build());
    }
}
