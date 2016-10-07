package clowdtech.mpositive.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
    //protected Tracker tracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get a Tracker (should auto-report)
        //tracker = ((App) getActivity().getApplication()).getTracker(App.TrackerName.APP_TRACKER);
    }

//    protected void trackScreenView(String screenName) {
//        // Set screen name.
//        tracker.setScreenName(screenName);
//
//        // Send a screen view.
//        tracker.send(new HitBuilders.ScreenViewBuilder().build());
//    }
//
//    protected void trackEvent(TrackingCategories category, String action, String label) {
//        tracker.send(new HitBuilders.EventBuilder()
//                .setCategory(category.toString())
//                .setAction(action)
//                .setLabel(label)
//                .build());
//    }
}

