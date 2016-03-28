package clowdtech.mpositive.controls; /// change this package to yourselves.

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.PagerTabStrip;
import android.util.AttributeSet;

import clowdtech.mpositive.R;

public class ClowdPagerTabStrip extends PagerTabStrip
{
    public ClowdPagerTabStrip(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ClowdPagerTabStrip);
        setTabIndicatorColor(a.getColor(R.styleable.ClowdPagerTabStrip_indicatorColor, Color.MAGENTA));
        a.recycle();
    }

}