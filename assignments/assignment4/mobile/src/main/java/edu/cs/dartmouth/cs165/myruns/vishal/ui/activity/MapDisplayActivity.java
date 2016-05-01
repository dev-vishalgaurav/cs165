package edu.cs.dartmouth.cs165.myruns.vishal.ui.activity;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

public class MapDisplayActivity extends BaseActivity implements OnMapReadyCallback, TrackingService.OnTrackingUpdateListener {

    public static final String EXTRA_VIEW_TYPE = "extra_view_type";
    public static final String EXTRA_ENTRY = "extra_entry_id";
    public static final String EXTRA_INPUT_TYPE = "extra_input_type";
    public static final int VIEW_TYPE_READ_ENTRY = 1;
    public static final int VIEW_TYPE_CREATE_ENTRY = 2;

    private GoogleMap mMap;
    private Button mBtnSave = null;
    private Button mBtnCancel = null;
    private TrackingBinder mBinder = null;
    private int viewMode = VIEW_TYPE_CREATE_ENTRY;
    private ExerciseEntry mExerciseEntry = null;
    private int inputType = 1;
    private TextView txtActivityType = null;
    private TextView txtAvgSpeed = null;
    private TextView txtCurrSpeed = null;
    private TextView txtClimb = null;
    private TextView txtCalorie = null;
    private TextView txtDisance = null;

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
        inputType = getIntent().getIntExtra(EXTRA_INPUT_TYPE,inputType);
        if (viewMode == VIEW_TYPE_CREATE_ENTRY) {
            findViewById(R.id.lnrSaveCancel).setVisibility(View.VISIBLE);
            TrackingService.start(this);
            TrackingService.bind(this, mConnection);
        } else if (viewMode == VIEW_TYPE_READ_ENTRY) {
            findViewById(R.id.lnrSaveCancel).setVisibility(View.GONE);
            mExerciseEntry = (ExerciseEntry) getIntent().getSerializableExtra(EXTRA_ENTRY);
        } else {
            showToast(getString(R.string.error));
        }
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
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
        txtDisance = (TextView) findViewById(R.id.txtDistance);
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
        onCancelClicked();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
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
        txtActivityType.setText(getString(R.string.unknown));
        txtCalorie.setText(String.format(getString(R.string.map_label_calorie), "" + mExerciseEntry.getCalorie()));
        txtAvgSpeed.setText(String.format(getString(R.string.map_label_avg_speed), "" + mExerciseEntry.getAvgPace()));
        txtCurrSpeed.setText(String.format(getString(R.string.map_label_cur_speed), "" + mExerciseEntry.getCurrentSpeed()));
        txtClimb.setText(String.format(getString(R.string.map_label_climb), "" + mExerciseEntry.getClimb()));
        txtDisance.setText(String.format(getString(R.string.map_label_distance), "" + mExerciseEntry.getDistance()));
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
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), padding), 1000, null);
        }
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
}
