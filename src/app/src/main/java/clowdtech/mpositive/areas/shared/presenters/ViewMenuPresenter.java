package clowdtech.mpositive.areas.shared.presenters;

import clowdtech.mpositive.areas.shared.views.IViewMenuView;

public class ViewMenuPresenter {
    private IViewMenuView view;

    public void setView(IViewMenuView view) {
        this.view = view;
    }

    public void setScreenName(String heading) {
        this.view.setScreenName(heading);
    }
}
