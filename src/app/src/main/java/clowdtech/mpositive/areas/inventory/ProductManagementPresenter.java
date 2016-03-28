package clowdtech.mpositive.areas.inventory;

import javax.inject.Inject;

import clowdtech.mpositive.ui.BasePresenter;

public class ProductManagementPresenter extends BasePresenter<ProductManagementView> {
    @Inject
    public ProductManagementPresenter() {

    }

    @Override
    public void bindView(ProductManagementView view) {
        super.bindView(view);

        this.view.bindViews();
    }

    @Override
    public void unbindView() {
        this.view.unbindViews();

        super.unbindView();
    }
}
