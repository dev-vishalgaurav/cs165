/**
 * Created by Vishal Gaurav
 */
package edu.cs.dartmouth.cs165.myruns.vishal.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONException;
import org.json.JSONObject;

import edu.cs.dartmouth.cs165.myruns.vishal.R;
import edu.cs.dartmouth.cs165.myruns.vishal.global.MyRunsApp;
import edu.cs.dartmouth.cs165.myruns.vishal.services.MyRunsSyncService;
import edu.cs.dartmouth.cs165.myruns.vishal.services.RegistrationIntentService;
import edu.cs.dartmouth.cs165.myruns.vishal.storage.db.ExerciseEntry;
import edu.cs.dartmouth.cs165.myruns.vishal.storage.preferences.PreferenceUtils;
import edu.cs.dartmouth.cs165.myruns.vishal.ui.adapters.HomeTabAdapter;
import edu.cs.dartmouth.cs165.myruns.vishal.ui.fragments.HistoryFragment;


/**
 * Common parent for most of the activities of this app.
 * Should not be used as an activity to display layouts.
 */
public class HomeActivity extends BaseActivity implements HistoryFragment.OnItemSelectedListener, HomeTabAdapter.OnFragmentInteractionListener {
    public static final int COUNT_TABS_HOME_SCREEN = 3;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private ViewPager mVpContents = null;
    private HomeTabAdapter mTabAdapter = null;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private BroadcastReceiver mSyncBroadcastReceiver;
    private boolean isReceiverRegistered;
    private boolean isSyncReceiverRegistered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("VVV","HomeActivity :- onCreate");
        setContentView(R.layout.activity_home);
        initViews();
        initGCM();
        printEntryJson();
    }
    private void printEntryJson(){
        try {
            JSONObject json = ExerciseEntry.getEntriesInJson(MyRunsApp.getDb(getBaseContext()).fetchAllEntryCursor());
            Log.e("VVV",json.toString());
        }catch (JSONException ex){
            Log.e("VVV","Error in getting json :- " + ex.getMessage());
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        registerReceiver();
        registerSyncReceiver();
    }

    @Override
    protected void onPause(){
        super.onPause();
        unRegisterReceiver();
        unRegisterSyncReceiver();
    }

    private void registerSyncReceiver(){
        if(!isSyncReceiverRegistered) {
            mSyncBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getIntExtra(MyRunsSyncService.EXTRA_RESULT, Activity.RESULT_CANCELED) == Activity.RESULT_OK) {
                        showToast("SYNC successful");
                    } else {
                        showToast("SYNC Error");
                    }
                }
            };
            LocalBroadcastManager.getInstance(this).registerReceiver(mSyncBroadcastReceiver, new IntentFilter(MyRunsSyncService.BROADCAST_SYNC_COMPLETE));
            isSyncReceiverRegistered = true;
        }
    }
    private void unRegisterSyncReceiver(){
        if(isSyncReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mSyncBroadcastReceiver);
            isSyncReceiverRegistered = false;
        }
    }
    private void unRegisterReceiver(){
        if(isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
            isReceiverRegistered = false;
        }
    }

    private void initGCM(){
        boolean isRegistrationNeeded = PreferenceUtils.getBoolean(getBaseContext(),PreferenceUtils.PREF_SENT_TOKEN_TO_SERVER,false);
        if(!isRegistrationNeeded) {
            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    boolean sentToken = PreferenceUtils.getBoolean(getBaseContext(), PreferenceUtils.PREF_SENT_TOKEN_TO_SERVER, false);
                    if (sentToken) {
                        showToast("GCM registration done");
                    } else {
                        showToast("GCM registration ERROR");
                    }
                }
            };
            // Registering BroadcastReceiver
            registerReceiver();

            if (checkPlayServices()) {
                // Start IntentService to register this application with GCM.
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }
        }
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
            intent.putExtra(MapDisplayActivity.EXTRA_ENTRY, entry.getId());
            intent.putExtra(MapDisplayActivity.EXTRA_VIEW_TYPE,MapDisplayActivity.VIEW_TYPE_READ_ENTRY);
            intent.putExtra(MapDisplayActivity.EXTRA_INPUT_TYPE,entry.getInputType());
            intent.putExtra(MapDisplayActivity.EXTRA_ACTIVITY_TYPE,entry.getActivityType());
            startActivity(intent);
        }
    }
    @Override
    public void onSyncClicked(){
        MyRunsSyncService.start(getBaseContext());
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
    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(PreferenceUtils.PREF_REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("VVV", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
