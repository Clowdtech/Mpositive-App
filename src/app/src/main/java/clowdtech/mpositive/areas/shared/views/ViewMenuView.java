package clowdtech.mpositive.areas.shared.views;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.shared.presenters.ViewMenuPresenter;

public class ViewMenuView extends Toolbar implements IViewMenuView {
    private ViewMenuPresenter presenter;

    private TextView screenNameText;

    private OnClickListener onLogoClicked;

    private ViewGroup viewMenuContainer;

    public ViewMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) {
            return;
        }

        this.presenter = new ViewMenuPresenter();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

//        this.presenter.setView(this);
//
//        findViewById(R.id.btn_nav_drawer).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onLogoClicked.onClick(v);
//            }
//        });
//
//        this.screenNameText = (TextView) findViewById(R.id.screen_name);
//
//        viewMenuContainer = (ViewGroup) findViewById(R.id.view_menu_container);
    }
    public void setOnLogoClicked(OnClickListener onLogoClicked) {
        this.onLogoClicked = onLogoClicked;
    }

    public void setViewMenu(View view) {
//        viewMenuContainer.removeAllViews();
//
//        viewMenuContainer.addView(view, 0);
    }

    public void clearViewMenu() {
//        viewMenuContainer.removeAllViews();
    }

    public void hideViewMenu() {
//        viewMenuContainer.setVisibility(INVISIBLE);
    }

    public void showViewMenu() {
//        viewMenuContainer.setVisibility(VISIBLE);
    }

    public void setHeading(String heading) {
//        this.presenter.setScreenName(heading);
    }

    @Override
    public void setScreenName(String heading) {
//        this.screenNameText.setText(heading);
    }
}

