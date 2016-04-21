package edu.cs.dartmouth.cs165.myruns.vishal.ui.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.support.v13.app.FragmentPagerAdapter;

import edu.cs.dartmouth.cs165.myruns.vishal.R;
import edu.cs.dartmouth.cs165.myruns.vishal.ui.activity.HomeActivity;
import edu.cs.dartmouth.cs165.myruns.vishal.ui.fragments.HistoryFragment;
import edu.cs.dartmouth.cs165.myruns.vishal.ui.fragments.SettingsFragment;
import edu.cs.dartmouth.cs165.myruns.vishal.ui.fragments.StartFragment;

/**
 * Created by Vishal Gaurav
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class HomeTabAdapter extends FragmentPagerAdapter {

    public interface OnFragmentInteractionListener{
        void onFragmentInteraction(Uri uri);
    }

    private Context mContext;
    private HistoryFragment.OnItemSelectedListener onItemSelectedListener = null;

    public HomeTabAdapter(FragmentManager fm, Context context, HistoryFragment.OnItemSelectedListener onItemSelectedListener) {
        super(fm);
        this.mContext = context;
        this.onItemSelectedListener = onItemSelectedListener;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                return new StartFragment();
            case 1:
                return HistoryFragment.newInstance(onItemSelectedListener);
            case 2:
                return new SettingsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return HomeActivity.COUNT_TABS_HOME_SCREEN;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.tab_label_start);
            case 1:
                return mContext.getString(R.string.tab_label_history);
            case 2:
                return mContext.getString(R.string.tab_label_settings);
        }
        return null;
    }
}
