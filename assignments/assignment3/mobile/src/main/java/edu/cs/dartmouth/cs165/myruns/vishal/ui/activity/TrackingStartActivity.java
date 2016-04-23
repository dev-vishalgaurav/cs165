/**
 * Created by Vishal Gaurav
 */
package edu.cs.dartmouth.cs165.myruns.vishal.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;

import edu.cs.dartmouth.cs165.myruns.vishal.R;
import edu.cs.dartmouth.cs165.myruns.vishal.global.MyRunsApp;
import edu.cs.dartmouth.cs165.myruns.vishal.storage.db.ExerciseEntry;
import edu.cs.dartmouth.cs165.myruns.vishal.ui.fragments.dialogs.DateDialogFragment;
import edu.cs.dartmouth.cs165.myruns.vishal.ui.fragments.dialogs.InputDialogFragment;
import edu.cs.dartmouth.cs165.myruns.vishal.ui.fragments.dialogs.TimeDialogFragment;

/**
 * Start activity for the manual entry mode
 */
public class TrackingStartActivity extends BaseActivity {

    public static final String EXTRA_INPUT_TYPE = "extra_input_type";
    public static final String EXTRA_ACTIVITY_TYPE = "extra_activity_type";

    private static final String TAG_YEAR = "year";
    private static final String TAG_MONTH = "month";
    private static final String TAG_DAY = "day";
    private static final String TAG_HOURS = "hours";
    private static final String TAG_MIN = "min";

    private static final String TAG_DATE = "date";
    private static final String TAG_TIME = "time";
    private static final String TAG_DURATION = "duration";
    private static final String TAG_DISTANCE = "distance";
    private static final String TAG_CALORIES = "calories";
    private static final String TAG_HEART_RATE = "heart_rate";
    private static final String TAG_COMMENTS = "comments";


    private ListView mLstTrack = null;
    private Button mBtnSave = null;
    private Button mBtnCancel = null;
    private ArrayAdapter<CharSequence> mAdapter = null;
    // declaring dialogs for taking input from user
    private DateDialogFragment mInputDate;
    private TimeDialogFragment mInputTime;
    private InputDialogFragment mInputDuration;
    private InputDialogFragment mInputDistance;
    private InputDialogFragment mInputCalories;
    private InputDialogFragment mInputHeartRate;
    private InputDialogFragment mInputComments;

    private int inputType = 0;
    private int activityType = 0;
    private int year = 0;
    private int month = 0;
    private int day = 0;
    private int hour = 0;
    private int min = 0;
    private int duration = 0;
    private double distance = 0;
    private int calories = 0;
    private int heartRate = 0;
    private String comments = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_start);
        initViews();
        getIntentData();
        if (savedInstanceState != null) {
            updateValues(savedInstanceState);
            updateFragments(savedInstanceState);
        }
    }

    private void getIntentData(){
        if(getIntent()!=null){
            inputType = getIntent().getIntExtra(EXTRA_INPUT_TYPE,inputType);
            activityType = getIntent().getIntExtra(EXTRA_ACTIVITY_TYPE,activityType);
        }
    }
    private void updateValues(Bundle onSavedInstanceState) {
        year = onSavedInstanceState.getInt(TAG_YEAR, year);
        month = onSavedInstanceState.getInt(TAG_MONTH, month);
        day = onSavedInstanceState.getInt(TAG_DAY, day);
        hour = onSavedInstanceState.getInt(TAG_HOURS, hour);
        min = onSavedInstanceState.getInt(TAG_MIN, min);
        duration = onSavedInstanceState.getInt(TAG_DURATION, duration);
        distance = onSavedInstanceState.getDouble(TAG_DISTANCE, distance);
        calories = onSavedInstanceState.getInt(TAG_CALORIES, calories);
        heartRate = onSavedInstanceState.getInt(TAG_HEART_RATE, heartRate);
        comments = onSavedInstanceState.getString(TAG_COMMENTS, comments);
    }

    private void saveValues(Bundle savedInstanceState) {
        savedInstanceState.putInt(TAG_YEAR, year);
        savedInstanceState.putInt(TAG_MONTH, month);
        savedInstanceState.putInt(TAG_DAY, day);
        savedInstanceState.putInt(TAG_HOURS, hour);
        savedInstanceState.putInt(TAG_MIN, min);
        savedInstanceState.putDouble(TAG_DURATION, duration);
        savedInstanceState.putDouble(TAG_DISTANCE, distance);
        savedInstanceState.putDouble(TAG_CALORIES, calories);
        savedInstanceState.putDouble(TAG_HEART_RATE, heartRate);
        savedInstanceState.putString(TAG_COMMENTS, comments);

    }

    @Override
    protected void onSaveInstanceState(Bundle onSavedInstanceState) {
        super.onSaveInstanceState(onSavedInstanceState);
        saveValues(onSavedInstanceState);
    }

    private void updateFragments(Bundle onSavedInstanceState) {
        if (onSavedInstanceState != null) {
            mInputDate = (DateDialogFragment) getSupportFragmentManager().findFragmentByTag(TAG_DATE);
            if (mInputDate != null) mInputDate.setOnDateSelectedListener(mOnDateSelectedListener);
            mInputTime = (TimeDialogFragment) getSupportFragmentManager().findFragmentByTag(TAG_TIME);
            if (mInputTime != null) mInputTime.setOnTimeSelectedListener(mOnTimeSelectedListener);
            mInputDuration = (InputDialogFragment) getSupportFragmentManager().findFragmentByTag(TAG_DURATION);
            if (mInputDuration != null) mInputDuration.setOnTextEnteredListener(mOnTextEntered);
            mInputDistance = (InputDialogFragment) getSupportFragmentManager().findFragmentByTag(TAG_DISTANCE);
            if (mInputDistance != null) mInputDistance.setOnTextEnteredListener(mOnTextEntered);
            mInputCalories = (InputDialogFragment) getSupportFragmentManager().findFragmentByTag(TAG_CALORIES);
            if (mInputCalories != null) mInputCalories.setOnTextEnteredListener(mOnTextEntered);
            mInputHeartRate = (InputDialogFragment) getSupportFragmentManager().findFragmentByTag(TAG_HEART_RATE);
            if (mInputHeartRate != null) mInputHeartRate.setOnTextEnteredListener(mOnTextEntered);
            mInputComments = (InputDialogFragment) getSupportFragmentManager().findFragmentByTag(TAG_COMMENTS);
            if (mInputComments != null) mInputComments.setOnTextEnteredListener(mOnTextEntered);
        }
    }

    /**
     * Main setup
     */
    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);
        mBtnSave = (Button) findViewById(R.id.btnSave);
        mBtnCancel.setOnClickListener(mOnClickListener);
        mBtnSave.setOnClickListener(mOnClickListener);
        mLstTrack = (ListView) findViewById(R.id.lstTrack);
        mAdapter = ArrayAdapter.createFromResource(getBaseContext(), R.array.tract_start, android.R.layout.simple_list_item_1);
        mLstTrack.setAdapter(mAdapter);
        mLstTrack.setOnItemClickListener(mOnItemClickListener);
    }

    private long getDate(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,month);
        cal.set(Calendar.DAY_OF_MONTH,month);
        cal.set(Calendar.HOUR_OF_DAY,hour);
        cal.set(Calendar.MINUTE, min);
        return cal.getTimeInMillis();
    }

    /**
     * Show toast and exit when save clicked
     */
    private void onSaveClicked() {
        ExerciseEntry entry = new ExerciseEntry(-1l,inputType,activityType,getDate(),duration,distance,0,0,calories,0,heartRate,comments);
        long id = MyRunsApp.getDb(getBaseContext()).insertEntry(entry);
        if(id > 0) {
            showToast(getString(R.string.saved) + "#" + id);
        }else{
            showToast(getString(R.string.error));
        }
        finish();
    }

    /**
     * Show toast and exit when cancel clicked
     */
    private void onCancelClicked() {
        showToast(getString(R.string.discarded));
        finish();
    }

    /**
     * Opens appropriate activities when item clicked
     *
     * @param view
     * @param position indicates what item was clicked
     */
    private void onTrackingClicked(View view, int position) {
        switch (position) {
            case 0: {
                if (mInputDate == null) {
                    mInputDate = new DateDialogFragment();
                }
                mInputDate.setOnDateSelectedListener(mOnDateSelectedListener);
                mInputDate.show(getSupportFragmentManager(), TAG_DATE);
            }
            break;
            case 1: {
                if (mInputTime == null) {
                    mInputTime = new TimeDialogFragment();
                }
                mInputTime.setOnTimeSelectedListener(mOnTimeSelectedListener);
                mInputTime.show(getSupportFragmentManager(), TAG_TIME);
            }
            break;
            case 2: {
                if (mInputDuration == null) {
                    mInputDuration = new InputDialogFragment();
                }
                mInputDuration.setOnTextEnteredListener(mOnTextEntered);
                mInputDuration.show(getSupportFragmentManager(), TAG_DURATION, mAdapter.getItem(position).toString(), "",String.valueOf(duration), InputType.TYPE_CLASS_NUMBER, 1);
            }
            break;
            case 3: {
                if (mInputDistance == null) {
                    mInputDistance = new InputDialogFragment();
                }
                mInputDistance.setOnTextEnteredListener(mOnTextEntered);
                mInputDistance.show(getSupportFragmentManager(), TAG_DISTANCE, mAdapter.getItem(position).toString() + " " + getString(R.string.in_miles), "", String.valueOf(distance), InputType.TYPE_CLASS_NUMBER, 1);
            }
            break;
            case 4: {
                if (mInputCalories == null) {
                    mInputCalories = new InputDialogFragment();
                }
                mInputCalories.setOnTextEnteredListener(mOnTextEntered);
                mInputCalories.show(getSupportFragmentManager(), TAG_CALORIES, mAdapter.getItem(position).toString(), "",String.valueOf(calories), InputType.TYPE_CLASS_NUMBER, 1);
            }
            break;
            case 5: {
                if (mInputHeartRate == null) {
                    mInputHeartRate = new InputDialogFragment();
                }
                mInputHeartRate.setOnTextEnteredListener(mOnTextEntered);
                mInputHeartRate.show(getSupportFragmentManager(), TAG_HEART_RATE, mAdapter.getItem(position).toString(), "",String.valueOf(heartRate), InputType.TYPE_CLASS_NUMBER, 1);
            }
            break;
            case 6: {
                if (mInputComments == null) {
                    mInputComments = new InputDialogFragment();
                }
                mInputComments.setOnTextEnteredListener(mOnTextEntered);
                mInputComments.show(getSupportFragmentManager(), TAG_COMMENTS, mAdapter.getItem(position).toString(), getString(R.string.hint_track_comments),String.valueOf(comments), EditorInfo.TYPE_CLASS_TEXT, 1);
            }
            break;
        }
    }

    private DateDialogFragment.OnDateSelectedListener mOnDateSelectedListener = new DateDialogFragment.OnDateSelectedListener() {
        @Override
        public void onDateSelected(String tag, int year, int month, int dayOfMonth) {
            Log.e("VVV", "OnDateSelected :- Tag = " + tag + " year  = " + year + " month = " + month + " day = " + dayOfMonth);
            if (tag != null && !tag.isEmpty()) {
                if (tag.equalsIgnoreCase(TAG_DATE)) {
                    TrackingStartActivity.this.year = year;
                    TrackingStartActivity.this.month = month;
                    TrackingStartActivity.this.day = day;

                }
            }
        }
    };

    private TimeDialogFragment.OnTimeSelectedListener mOnTimeSelectedListener = new TimeDialogFragment.OnTimeSelectedListener() {
        @Override
        public void onTimeSelected(String tag, int hourOfDay, int minute) {
            Log.e("VVV", "onTimeSelected :- Tag = " + tag + " hour  = " + hourOfDay + " minute = " + minute);
            if (tag != null && !tag.isEmpty()) {
                if (tag.equalsIgnoreCase(TAG_TIME)) {
                    TrackingStartActivity.this.hour = hourOfDay;
                    TrackingStartActivity.this.min = minute;
                }
            }
        }
    };

    private double getDoubleValue(String text){
        double result = 0;
        if(text!= null && !text.isEmpty()){
            try{
                result =  Double.parseDouble(text);
            }catch (NumberFormatException ex){
                result =  0;
            }
        }
        return result;
    }
    private int getIntValue(String text){
        int result = 0;
        if(text!= null && !text.isEmpty()){
            try{
                result =  Integer.parseInt(text);
            }catch (NumberFormatException ex){
                result =  0;
            }
        }
        return result;
    }
    private InputDialogFragment.OnTextEntered mOnTextEntered = new InputDialogFragment.OnTextEntered() {
        @Override
        public void onTextEntered(String tag, String text) {
            Log.e("VVV", "OnTextEntered , TAG = " + tag + ", Text = " + text);
            if (tag != null && !tag.isEmpty()) {
                try {
                    if (tag.equalsIgnoreCase(TAG_DURATION)) {
                        duration = getIntValue(text);
                    } else if (tag.equalsIgnoreCase(TAG_DISTANCE)) {
                        distance = getDoubleValue(text);
                    } else if (tag.equalsIgnoreCase(TAG_CALORIES)) {
                        calories = getIntValue(text);
                    } else if (tag.equalsIgnoreCase(TAG_HEART_RATE)) {
                        heartRate = getIntValue(text);
                    } else if (tag.equalsIgnoreCase(TAG_COMMENTS)) {
                        comments = text;
                    }
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    Toast.makeText(getBaseContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onTextCancelled(String tag) {
            Log.e("VVV", "onTextCancelled , TAG = " + tag);
        }
    };

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            onTrackingClicked(view, position);
        }
    };
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

}
