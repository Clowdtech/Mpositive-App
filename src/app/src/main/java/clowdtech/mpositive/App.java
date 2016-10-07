package clowdtech.mpositive;

import android.app.Application;
import android.preference.PreferenceManager;

import com.clowdtech.data.dbflow.ApplicationDatabase;
import com.raizlabs.android.dbflow.config.FlowManager;

import clowdtech.mpositive.R;
import clowdtech.mpositive.easydata.DBAppFuncs;
import clowdtech.mpositive.ioc.components.ActivityComponent;
import clowdtech.mpositive.ioc.components.ApplicationComponent;
import clowdtech.mpositive.ioc.components.CheckoutComponent;
import clowdtech.mpositive.ioc.components.DaggerApplicationComponent;
import clowdtech.mpositive.ioc.components.DaggerReportingComponent;
import clowdtech.mpositive.ioc.components.ReportingComponent;
import clowdtech.mpositive.ioc.modules.ApplicationModule;
import clowdtech.mpositive.ioc.modules.CheckoutModule;
import clowdtech.mpositive.ioc.modules.RepositoryModule;

//import com.google.android.gms.analytics.GoogleAnalytics;
//import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.analytics.Logger;
//import com.google.android.gms.analytics.Tracker;
//import java.util.HashMap;
//
//import clowdtech.mpositive.BuildConfig;
//import clowdtech.mpositive.R;

public class App extends Application { //implements ITracker {
   // private Tracker tracker;
    private ActivityComponent activityComponent;
    private ApplicationComponent applicationComponent;
    private CheckoutComponent checkoutComponent;
    private ReportingComponent reportingComponent;

    DBAppFuncs dbAppFuncs;

    public App() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        FlowManager.init(this);

        FlowManager.getDatabase(ApplicationDatabase.NAME).getWritableDatabase();

        initialisePreferences();

        //tracker = getTracker(App.TrackerName.APP_TRACKER);

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .repositoryModule(new RepositoryModule())
                .checkoutModule(new CheckoutModule())
                .build();

        reportingComponent = DaggerReportingComponent.builder()
                .applicationComponent(applicationComponent)
                .build();

        getApplicationComponent().inject(this); // As of now, LocationManager should be injected into this.
    }

    public synchronized DBAppFuncs getDBAppFunctionsObj() {

        if(dbAppFuncs == null || !dbAppFuncs.isOpen) {
            dbAppFuncs = new DBAppFuncs(this);
        }

        if(!dbAppFuncs.isOpen)
            dbAppFuncs.open();

        return dbAppFuncs;
    }

    private void initialisePreferences() {
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
    }

    public enum TrackerName {
        APP_TRACKER
    }

//    HashMap<TrackerName, Tracker> mTrackers = new HashMap<>();
//
//    public synchronized Tracker getTracker(TrackerName trackerId) {
//        if (!mTrackers.containsKey(trackerId)) {
//
//            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
//
//            analytics.setAppOptOut(BuildConfig.DEBUG);
//            analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
//
//            Tracker t = analytics.newTracker(R.xml.tracker_app);
//            t.enableAdvertisingIdCollection(true);
//            mTrackers.put(trackerId, t);
//        }
//
//        return mTrackers.get(trackerId);
//    }
//
//    @Override
//    public void trackEvent(TrackingCategories category, String action, String label) {
//        this.tracker.send(new HitBuilders.EventBuilder()
//                .setCategory(category.toString())
//                .setAction(action)
//                .setLabel(label)
//                .build());
//    }
//
//    @Override
//    public void trackEvent(TrackingCategories category, String action) {
//        this.tracker.send(new HitBuilders.EventBuilder()
//                .setCategory(category.toString())
//                .setAction(action)
//                .build());
//    }




    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }

    public void setActivityComponent(ActivityComponent activityComponent) {
        this.activityComponent = activityComponent;
    }

    public CheckoutComponent getCheckoutComponent() {
        return checkoutComponent;
    }

    public void setCheckoutComponent(CheckoutComponent checkoutComponent) {
        this.checkoutComponent = checkoutComponent;
    }

    public ReportingComponent getReportingComponent() {
        return reportingComponent;
    }
}
