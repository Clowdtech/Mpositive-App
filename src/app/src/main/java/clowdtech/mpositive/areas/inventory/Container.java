package clowdtech.mpositive.areas.inventory;

import android.view.View;

import com.clowdtech.data.entities.IProduct;
import com.clowdtech.data.entities.Category;

import clowdtech.mpositive.areas.shared.views.ViewMenuView;

public interface Container {
    void initialise(ViewMenuView customActionBar);



    void productSelected(IProduct product);

    void productSaved();

    void productDeleted();



    void categoryEdit(Category category);

    void categorySaved();



    boolean onBackPressed();

    void navToHomeView();


    void setViewMenu(View view);

    void clearViewMenu();

    void productViewSelected();

    void categoryViewSelected();


    void bindView();
}
