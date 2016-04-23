/**
 * Created by Vishal Gaurav
 */
package edu.cs.dartmouth.cs165.myruns.vishal.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import edu.cs.dartmouth.cs165.myruns.vishal.R;
import edu.cs.dartmouth.cs165.myruns.vishal.ui.adapters.HomeTabAdapter;
import edu.cs.dartmouth.cs165.myruns.vishal.ui.fragments.HistoryFragment;


/**
 * Common parent for most of the activities of this app.
 * Should not be used as an activity to display layouts.
 */
public class HomeActivity extends BaseActivity implements HistoryFragment.OnItemSelectedListener {
    public static final int COUNT_TABS_HOME_SCREEN = 3;
    private ViewPager mVpContents = null;
    private HomeTabAdapter mTabAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mVpContents = (ViewPager) findViewById(R.id.vpContainer);
        mTabAdapter = new HomeTabAdapter(getFragmentManager(), getBaseContext());
        mVpContents.setAdapter(mTabAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mVpContents);
    }

    @Override
    public void onItemSelected(long rowid) {
        Log.e("VVV", "onItemSelected, ID = " + rowid);
        Intent intent = new Intent(getBaseContext(), EntryDetailActivity.class);
        intent.putExtra(EntryDetailActivity.EXTRA_ENTRY_ID, rowid);
        startActivity(intent);
    }
}
