package clowdtech.mpositive.areas.shared;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ViewPageAdapter extends PagerAdapter {
    private List<ViewPage> frags;

    public ViewPageAdapter(List<ViewPage> frags) {
        this.frags = frags;
    }

    @Override
    public int getCount() {
        return frags.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return frags.get(position).title;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        View view = frags.get(position).view;

        collection.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }
}
