package clowdtech.mpositive.areas.inventory.presenters;

import javax.inject.Inject;

import clowdtech.mpositive.ITracker;
import clowdtech.mpositive.areas.inventory.views.IAreaSelectView;
import clowdtech.mpositive.ui.BasePresenter;
import clowdtech.mpositive.tracking.TrackingCategories;

public class AreaSelectPresenter extends BasePresenter<IAreaSelectView> {
    private final ITracker tracker;

    @Inject
    public AreaSelectPresenter(ITracker tracker) {
        this.tracker = tracker;
    }

    public void showProductsView() {
        this.view.getContainer().productViewSelected();

        this.tracker.trackEvent(TrackingCategories.InventoryManagement, "Show Products");
    }

    public void showCategoriesView() {
        this.view.getContainer().categoryViewSelected();

        this.tracker.trackEvent(TrackingCategories.InventoryManagement, "Show Categories");
    }
}
