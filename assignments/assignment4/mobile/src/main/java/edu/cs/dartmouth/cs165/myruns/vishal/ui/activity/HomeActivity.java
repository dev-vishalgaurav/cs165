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
import edu.cs.dartmouth.cs165.myruns.vishal.storage.db.ExerciseEntry;
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
        Log.e("VVV","HomeActivity :- onCreate");
        setContentView(R.layout.activity_home);
        initViews();
    }

    /**
     * Main display setup
     */
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
    public void onItemSelected(ExerciseEntry entry) {
        Log.e("VVV", "onItemSelected, ID = " + entry.getId());
        if(entry.getInputType() == 0) {
            Intent intent = new Intent(getBaseContext(), EntryDetailActivity.class);
            intent.putExtra(EntryDetailActivity.EXTRA_ENTRY_ID, entry.getId());
            startActivity(intent);
        }else{
            Intent intent = new Intent(getBaseContext(), MapDisplayActivity.class);
            intent.putExtra(MapDisplayActivity.EXTRA_ENTRY, entry);
            intent.putExtra(MapDisplayActivity.EXTRA_VIEW_TYPE,MapDisplayActivity.VIEW_TYPE_READ_ENTRY);
            intent.putExtra(MapDisplayActivity.EXTRA_INPUT_TYPE,entry.getInputType());
            intent.putExtra(MapDisplayActivity.EXTRA_ACTIVITY_TYPE,entry.getActivityType());
            startActivity(intent);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.e("VVV","HomeActivity :- onNewIntent");
        super.onNewIntent(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.e("VVV","HomeActivity :- onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }
}
