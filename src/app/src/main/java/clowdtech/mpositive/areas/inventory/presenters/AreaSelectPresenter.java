package clowdtech.mpositive.areas.inventory.presenters;

import javax.inject.Inject;

import clowdtech.mpositive.areas.inventory.views.IAreaSelectView;
import clowdtech.mpositive.ui.BasePresenter;

public class AreaSelectPresenter extends BasePresenter<IAreaSelectView> {

    @Inject
    public AreaSelectPresenter() {

    }

    public void showProductsView() {
        this.view.getContainer().productViewSelected();
    }

    public void showCategoriesView() {
        this.view.getContainer().categoryViewSelected();
    }
}
