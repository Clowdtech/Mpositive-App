package clowdtech.mpositive;

import clowdtech.mpositive.tracking.TrackingCategories;

public interface ITracker {
    void trackEvent(TrackingCategories category, String action, String label);

    void trackEvent(TrackingCategories category, String action);
}
