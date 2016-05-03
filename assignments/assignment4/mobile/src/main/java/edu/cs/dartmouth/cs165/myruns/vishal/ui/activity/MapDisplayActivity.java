package edu.cs.dartmouth.cs165.myruns.vishal.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import edu.cs.dartmouth.cs165.myruns.vishal.R;
import edu.cs.dartmouth.cs165.myruns.vishal.global.MyRunsApp;
import edu.cs.dartmouth.cs165.myruns.vishal.services.TrackingBinder;
import edu.cs.dartmouth.cs165.myruns.vishal.services.TrackingService;
import edu.cs.dartmouth.cs165.myruns.vishal.storage.db.ExerciseEntry;
import edu.cs.dartmouth.cs165.myruns.vishal.storage.preferences.PreferenceUtils;

public class MapDisplayActivity extends BaseActivity implements OnMapReadyCallback, TrackingService.OnTrackingUpdateListener {

    public static final String EXTRA_VIEW_TYPE = "extra_view_type";
    public static final String EXTRA_ENTRY = "extra_entry_id";
    public static final String EXTRA_INPUT_TYPE = "extra_input_type";
    public static final String EXTRA_ACTIVITY_TYPE = "extra_activity_type";
    public static final int VIEW_TYPE_READ_ENTRY = 1;
    public static final int VIEW_TYPE_CREATE_ENTRY = 2;

    private GoogleMap mMap;
    private Button mBtnSave = null;
    private Button mBtnCancel = null;
    private TrackingBinder mBinder = null;
    private int viewMode = VIEW_TYPE_CREATE_ENTRY;
    private ExerciseEntry mExerciseEntry = null;
    private int inputType = 1;
    private int activityType = 0;
    private TextView txtActivityType = null;
    private TextView txtAvgSpeed = null;
    private TextView txtCurrSpeed = null;
    private TextView txtClimb = null;
    private TextView txtCalorie = null;
    private TextView txtDistance = null;
    private AlertDialog mDeleteConfirmDialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_display);
        initViews();
        enableViewModeFromIntent();
    }

    /**
     * this method will check proper view type from intent and will the service accordingly
     */
    private void enableViewModeFromIntent() {
        viewMode = getIntent().getIntExtra(EXTRA_VIEW_TYPE, VIEW_TYPE_CREATE_ENTRY);
        inputType = getIntent().getIntExtra(EXTRA_INPUT_TYPE, inputType);
        activityType = getIntent().getIntExtra(EXTRA_ACTIVITY_TYPE, activityType);
        if (viewMode == VIEW_TYPE_CREATE_ENTRY) {
            Log.e("VVV", "View mode write ENTRY");
            findViewById(R.id.lnrSaveCancel).setVisibility(View.VISIBLE);
            TrackingService.start(this);
            TrackingService.bind(this, mConnection);
        } else if (viewMode == VIEW_TYPE_READ_ENTRY) {
            Log.e("VVV", "View mode read ENTRY");
            findViewById(R.id.lnrSaveCancel).setVisibility(View.GONE);
            long entryId = getIntent().getLongExtra(EXTRA_ENTRY, -1l);
            Log.e("VVV", "Entry id = " + entryId);
            mExerciseEntry = MyRunsApp.getDb(getBaseContext()).fetchEntryByIndex(entryId);
            if (mExerciseEntry == null) {
                showToast(getString(R.string.error));
                finish();
            }
        } else {
            showToast(getString(R.string.error_invalid_mode));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(viewMode == VIEW_TYPE_READ_ENTRY) {
            getMenuInflater().inflate(R.menu.menu_entry_details, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete: {
                showDiscardConfirmDialog();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Show message upon entry discarding
     */
    private void showDiscardConfirmDialog() {
        initDiscardConfirmDialog();
        mDeleteConfirmDialog.show();
    }

    /**
     * Confirm entry deletion by the user
     */
    private void initDiscardConfirmDialog() {
        Dialog.OnClickListener mOnDialogClickDiscardConfirm = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE: {
                        new DeleteTask().execute(mExerciseEntry.getId());
                    }
                    break;
                    case Dialog.BUTTON_NEGATIVE: {
                    }
                    break;
                }
            }
        };
        if (mDeleteConfirmDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MapDisplayActivity.this);
            builder.setTitle(R.string.dialog_title_delete_confirm);
            builder.setMessage(String.format(getString(R.string.dialog_message_delete_confirm), mExerciseEntry.getId()));
            builder.setPositiveButton(getString(R.string.yes), mOnDialogClickDiscardConfirm);
            builder.setNegativeButton(getString(R.string.no), mOnDialogClickDiscardConfirm);
            mDeleteConfirmDialog = builder.create();
        }
    }

    @Override
    protected void onResume() {
        Log.e("VVV", "Map display onResume");
        super.onResume();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e("VVV", "Map display onMapReady");
        mMap = googleMap;
        updateData();
    }

    private void updateData() {
        if (mExerciseEntry != null) {
            updateTraceDataOnUi();
            updateTraceOnMap();
        }
    }

    /**
     * Main display setup
     */
    private void initViews() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);
        mBtnSave = (Button) findViewById(R.id.btnSave);
        txtActivityType = (TextView) findViewById(R.id.txtActivityType);
        txtAvgSpeed = (TextView) findViewById(R.id.txtAvgSpeed);
        txtCurrSpeed = (TextView) findViewById(R.id.txtCurSpeed);
        txtClimb = (TextView) findViewById(R.id.txtClimb);
        txtCalorie = (TextView) findViewById(R.id.txtCalorie);
        txtDistance = (TextView) findViewById(R.id.txtDistance);
        mBtnCancel.setOnClickListener(mOnClickListener);
        mBtnSave.setOnClickListener(mOnClickListener);

    }

    /**
     * Exit map activity when save clicked
     */
    private void onSaveClicked() {
        stopTracking();
        // save data into DB in a background thread and finish the activity after it.
        new SaveEntryTask().execute();
    }

    private long onSaveDataIntoDB() {
        mExerciseEntry.setInputType(inputType);
        long id = MyRunsApp.getDb(getBaseContext()).insertEntry(mExerciseEntry);
        return id;
    }

    private void stopTracking() {
        if (mBinder != null) {
            mBinder.stopService();
        }
    }

    /**
     * Exit map activity when cancel clicked
     */
    private void onCancelClicked() {
        showToast(getString(R.string.discarded));
        finish();
        stopTracking();
    }

    @Override
    public void onBackPressed() {
        if (viewMode == VIEW_TYPE_CREATE_ENTRY) {
            onCancelClicked();
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unBindService();
    }

    private void unBindService() {
        if (viewMode == VIEW_TYPE_CREATE_ENTRY) {
            try {
                if (mConnection != null) {
                    unbindService(mConnection);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("VVV", "Map :- Tracking service connected");
            mBinder = (TrackingBinder) service;
            mBinder.setOnTrackingUpdateListener(MapDisplayActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("VVV", "Map :- Tracking service disconnected");
        }
    };

    /**
     * Listener for the save and cancel buttons
     */
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnSave: {
                    onSaveClicked();
                }
                break;
                case R.id.btnCancel: {
                    onCancelClicked();
                }
                break;
            }
        }
    };

    @Override
    public void onEntryUpdate(ExerciseEntry entry) {
        Log.e("VVV", "onEntryUpdate");
        mExerciseEntry = entry;
        updateTraceOnMap();
        updateTraceDataOnUi();
    }

    private void updateTraceDataOnUi() {
        if (inputType == 1) { // GPS
            txtActivityType.setText(String.format(getString(R.string.map_label_type), "" + getResources().getStringArray(R.array.activity_type)[activityType]));
        } else {
            txtActivityType.setText(String.format(getString(R.string.map_label_type), "" + getString(R.string.unknown)));
        }
        int unitType = PreferenceUtils.getUnitType(getBaseContext());
        String unit = PreferenceUtils.getDistanceUnit(getBaseContext());
        txtCalorie.setText(String.format(getString(R.string.map_label_calorie), "" + mExerciseEntry.getCalorie()));
        txtAvgSpeed.setText(String.format(getString(R.string.map_label_avg_speed), "" + ExerciseEntry.getDistanceAsPerUnit(mExerciseEntry.getAvgPace(),unitType),unit ));
        txtCurrSpeed.setText(String.format(getString(R.string.map_label_cur_speed), "" + ExerciseEntry.getDistanceAsPerUnit(mExerciseEntry.getCurrentSpeed(),unitType), unit));
        txtClimb.setText(String.format(getString(R.string.map_label_climb), "" + ExerciseEntry.getDistanceAsPerUnit(mExerciseEntry.getClimb(),unitType), unit));
        txtDistance.setText(String.format(getString(R.string.map_label_distance), "" + ExerciseEntry.getDistanceAsPerUnit(mExerciseEntry.getDistance(), unitType), unit));
    }

    private void updateTraceOnMap() {
        ArrayList<LatLng> locationList = mExerciseEntry.getLocationList();
        if (locationList != null && locationList.size() > 0 && mMap != null) {
            Log.e("VVV", "start marker detected size = " + locationList.size());
            LatLng start = locationList.get(0);
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(start).
                    icon(BitmapDescriptorFactory.fromResource(R.drawable.red_dot)).
                    title(getString(R.string.start_location)));
            if (locationList.size() > 1) {
                Log.e("VVV", "end marker detected");
                LatLng end = locationList.get(locationList.size() - 1);
                mMap.addMarker(new MarkerOptions().position(end).
                        icon(BitmapDescriptorFactory.fromResource(R.drawable.green_dot))
                        .title(getString(R.string.end_location)));
                PolylineOptions polyLine = new PolylineOptions();
                polyLine.addAll(locationList);
                mMap.addPolyline(polyLine).setColor(getResources().getColor(android.R.color.black));
            }
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng loc : locationList) {
                builder.include(loc);
            }
            int padding = 100;
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), displaymetrics.widthPixels, displaymetrics.heightPixels, padding), 1000, null);
        }
    }

    /**
     * Delete entry associated with the provided ID
     */
    private boolean onDeleteEntry(long id) {
        if (MyRunsApp.getDb(getBaseContext()).removeEntry(id) > 0) {
            return true;
        }
        return false;
    }

    /**
     * Asynctask for saving entry to database and finish the activity
     */
    private class SaveEntryTask extends AsyncTask<Void, Void, Long> {

        @Override
        protected void onPreExecute() {
            showProgressDialog(getString(R.string.saving_entry), false);
            super.onPreExecute();
        }

        @Override
        protected Long doInBackground(Void... params) {
            return onSaveDataIntoDB();
        }

        @Override
        protected void onPostExecute(Long result) {
            dismissAlertDialog();
            if (result > 0) {
                showToast(getString(R.string.saved) + "#" + result);
            } else {
                showToast(getString(R.string.error));
            }
            finish();
            super.onPostExecute(result);
        }
    }

    /**
     * Entry deletion
     */
    private class DeleteTask extends AsyncTask<Long, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            showProgressDialog(getString(R.string.deleting_message), false);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Long... params) {
            return onDeleteEntry(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            dismissAlertDialog();
            if (result) {
                showToast(getString(R.string.success));
                finish();
            } else {
                showToast(getString(R.string.error_in_delete));
                finish();
            }
            super.onPostExecute(result);
        }
    }
}
