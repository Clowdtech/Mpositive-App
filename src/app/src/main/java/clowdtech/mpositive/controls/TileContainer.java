package clowdtech.mpositive.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class TileContainer extends RelativeLayout {
    private OnClickListener clickListener;

    public TileContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        this.clickListener = l;

        super.setOnClickListener(l);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            if (clickListener != null) {
                clickListener.onClick(this);
            }
        }

        if (event.getActionMasked() == MotionEvent.ACTION_DOWN)
        {
            setAlpha(.5f);
        }
        else
        {
            setAlpha(1f);
        }

        return true;
    }
}
