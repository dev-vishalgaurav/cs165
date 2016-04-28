package com.varunmishra.debugthis.runtracker;

/**
 * Created by Varun on 2/1/16.
 */

import android.support.v4.app.Fragment;

public class LocationListActivity extends SingleFragmentActivity {
    public static final String EXTRA_RUN_ID = "RUN_ID";
    // bug 3 fixed
    public static long runId ;
    @Override
    protected Fragment createFragment() {

        runId = getIntent().getLongExtra(EXTRA_RUN_ID, -1);
        return new LocationListFragment();

    }

}
