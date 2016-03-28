package clowdtech.mpositive.areas.shared.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import clowdtech.mpositive.App;

public class LinearLayoutDgr extends LinearLayout {
    public LinearLayoutDgr(Context context, AttributeSet attrs) {
        super(context, attrs);

//        ((App) context.getApplicationContext()).getApplicationComponent().inject(this);
    }
}
