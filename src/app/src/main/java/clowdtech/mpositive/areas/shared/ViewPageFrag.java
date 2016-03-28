package clowdtech.mpositive.areas.shared;

import android.support.v4.app.Fragment;

public class ViewPageFrag {
    public Fragment fragment;
    public String tag;
    public String title;

    public ViewPageFrag(Fragment fragment, String title, String tag) {
        this.fragment = fragment;
        this.title = title;
        this.tag = tag;
    }
}
