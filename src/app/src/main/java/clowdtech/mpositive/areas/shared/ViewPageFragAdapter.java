package clowdtech.mpositive.areas.shared;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ViewPageFragAdapter extends PagerAdapter {
    private final FragmentManager fragmentManager;
    private final List<ViewPageFrag> frags;
    private FragmentTransaction currentTransaction = null;
    private Fragment currentPrimaryItem = null;

    public ViewPageFragAdapter(FragmentManager fragmentManager, List<ViewPageFrag> frags) {
        this.fragmentManager = fragmentManager;
        this.frags = frags;
    }

    @Override public int getCount() {
        return frags.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return frags.get(position).title;
    }

    @Override public Object instantiateItem(ViewGroup container, int position) {
        if (currentTransaction == null) {
            currentTransaction = fragmentManager.beginTransaction();
        }

        String tag = frags.get(position).tag;
        Fragment fragment = frags.get(position).fragment;
        currentTransaction.add(container.getId(), fragment, tag);
        if (fragment != currentPrimaryItem) {
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);
        }

        return fragment;
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
        if (currentTransaction == null) {
            currentTransaction = fragmentManager.beginTransaction();
        }

        Fragment fragment = (Fragment) object;
        currentTransaction.remove(fragment);
    }

    @Override public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment) object;
        if (fragment != currentPrimaryItem) {
            if (currentPrimaryItem != null) {
                currentPrimaryItem.setMenuVisibility(false);
                currentPrimaryItem.setUserVisibleHint(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            currentPrimaryItem = fragment;
        }
    }

    @Override public void finishUpdate(ViewGroup container) {
        if (currentTransaction != null) {
            currentTransaction.commitAllowingStateLoss();
            currentTransaction = null;
            fragmentManager.executePendingTransactions();
        }
    }

    @Override public boolean isViewFromObject(View view, Object object) {
        return ((Fragment) object).getView() == view;
    }
}

