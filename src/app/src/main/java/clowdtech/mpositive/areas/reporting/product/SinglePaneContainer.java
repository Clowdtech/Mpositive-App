package clowdtech.mpositive.areas.reporting.product;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import javax.inject.Inject;

import clowdtech.mpositive.App;
import clowdtech.mpositive.areas.reporting.product.presenters.ProductReportPresenter;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class SinglePaneContainer extends RelativeLayout implements Presentable {
    @Inject
    protected ProductReportPresenter presenter;

    public SinglePaneContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }

        ((App) context.getApplicationContext()).getReportingComponent().inject(this);
    }

    @Override
    public Presenter getPresenter() {
        return presenter;
    }
}
