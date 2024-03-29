package edu.cs.dartmouth.cs165.myruns.vishal.storage.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.android.gms.maps.model.LatLng;
import com.google.api.client.json.Json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import edu.cs.dartmouth.cs165.myruns.vishal.R;
import edu.cs.dartmouth.cs165.myruns.vishal.storage.preferences.PreferenceUtils;
import edu.cs.dartmouth.cs165.myruns.vishal.utils.DateTimeUtils;


/**
 * Created by Vishal Gaurav
 */
public class ExerciseEntry implements Serializable {

    public static final String VALUES_JSON_DATA = "json_data";

    private Long id;
    private int mInputType;        // Manual, GPS or automatic
    private int mActivityType;     // Running, cycling etc.
    private long mDateTime;    // When does this entry happen
    private int mDuration;         // Exercise duration in seconds
    private double mDistance;      // Distance traveled. Either in meters or feet.
    private double mAvgPace;       // Average pace
    private double mAvgSpeed;      // Average speed
    private int mCalorie;          // Calories burnt
    private double mClimb;         // Climb. Either in meters or feet.
    private int mHeartRate;        // Heart rate
    private String mComment;       // Comments
    private ArrayList<LatLng> mLocationList = new ArrayList<>(); // Location list
    private double currentSpeed = 0.0;

    private int[] activityVotes = {0,0,0,0};

    public ExerciseEntry(Cursor cursor) {
        id = cursor.getLong(cursor.getColumnIndexOrThrow(DBConstants.ExerciseEntryColumns._ID));
        mInputType = cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.ExerciseEntryColumns.INPUT_TYPE));
        mActivityType = cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.ExerciseEntryColumns.ACTIVITY_TYPE));
        mDateTime = cursor.getLong(cursor.getColumnIndexOrThrow(DBConstants.ExerciseEntryColumns.DATE_TIME));
        mDuration = cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.ExerciseEntryColumns.DURATION));
        mDistance = cursor.getDouble(cursor.getColumnIndexOrThrow(DBConstants.ExerciseEntryColumns.DISTANCE));
        mAvgPace = cursor.getDouble(cursor.getColumnIndexOrThrow(DBConstants.ExerciseEntryColumns.AVG_PACE));
        mAvgSpeed = cursor.getDouble(cursor.getColumnIndexOrThrow(DBConstants.ExerciseEntryColumns.AVG_SPEED));
        mCalorie = cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.ExerciseEntryColumns.CALORIE));
        mClimb = cursor.getDouble(cursor.getColumnIndexOrThrow(DBConstants.ExerciseEntryColumns.CLIMB));
        mHeartRate = cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.ExerciseEntryColumns.HEART_RATE));
        mComment = cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.ExerciseEntryColumns.COMMENT));
        mLocationList = getListFromBlob(cursor.getBlob(cursor.getColumnIndexOrThrow(DBConstants.ExerciseEntryColumns.GPS_DATA)));
    }

    public ExerciseEntry(Long id, int mInputType, int mActivityType, long mDateTime, int mDuration, double mDistance, double mAvgPace, double mAvgSpeed, int mCalorie, double mClimb, int mHeartRate, String mComment) {
        this.id = id;
        this.mInputType = mInputType;
        this.mActivityType = mActivityType;
        this.mDateTime = mDateTime;
        this.mDuration = mDuration;
        this.mDistance = mDistance;
        this.mAvgPace = mAvgPace;
        this.mAvgSpeed = mAvgSpeed;
        this.mCalorie = mCalorie;
        this.mClimb = mClimb;
        this.mHeartRate = mHeartRate;
        this.mComment = mComment;
    }

    /**
     * Getter and setter methods
     */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getInputType() {
        return mInputType;
    }

    public void setInputType(int mInputType) {
        this.mInputType = mInputType;
    }

    public int getActivityType() {
        return mActivityType;
    }

    public void setActivityType(int mActivityType) {
        this.mActivityType = mActivityType;
    }

    public long getDateTime() {
        return mDateTime;
    }

    public void setDateTime(long mDateTime) {
        this.mDateTime = mDateTime;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    public double getDistance() {
        return mDistance;
    }

    public double getDistance(int unitType) {
        if (unitType == 2) {// miles\
            return getDistance();
        } else {
            return getDistance() * 1.6; // convert to KMs
        }
    }

    public void setDistance(double mDistance) {
        this.mDistance = mDistance;
    }

    public double getAvgPace() {
        return mAvgPace;
    }

    public void setAvgPace(double mAvgPace) {
        this.mAvgPace = mAvgPace;
    }

    public double getAvgSpeed() {
        return mAvgSpeed;
    }

    public void setAvgSpeed(double mAvgSpeed) {
        this.mAvgSpeed = mAvgSpeed;
    }

    public int getCalorie() {
        return mCalorie;
    }

    public void setCalorie(int mCalorie) {
        this.mCalorie = mCalorie;
    }

    public double getClimb() {
        return mClimb;
    }

    public void setClimb(double mClimb) {
        this.mClimb = mClimb;
    }

    public int getHeartRate() {
        return mHeartRate;
    }

    public void setHeartRate(int mHeartRate) {
        this.mHeartRate = mHeartRate;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String mComment) {
        this.mComment = mComment;
    }

    public ArrayList<LatLng> getLocationList() {
        return mLocationList;
    }

    public void setLocationList(ArrayList<LatLng> mLocationList) {
        this.mLocationList = mLocationList;
    }

    public double getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(double currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public void updateVote(int label){
        activityVotes[label]++;
    }

    public int getMaxVotedActivity(){
        int maxIndex = activityVotes.length - 1 ; // others
        for(int i = 0 ; i < activityVotes.length -1 ; i++){
            if(activityVotes[maxIndex] <= activityVotes[i]){
                maxIndex = i;
            }
        }
        return  maxIndex;
    }
    public void resetVotes(){
        for(int i =0 ; i < activityVotes.length ; i++){
            activityVotes[i] = 0;
        }
    }
    /**
     * Get formatted string for an entry
     */
    public String getFormattedString(Context context, int unitType) {
        return context.getResources().getStringArray(R.array.input_type)[mInputType] + ":" + getProperActivityType(context) +
                ", " + DateTimeUtils.getFormattedDate(mDateTime, DateTimeUtils.EXCERCISE_ENTRY_FORMAT) + " " + getFormattedDouble(getDistance(unitType)) + " " + PreferenceUtils.getDistanceUnit(context) + " , " + getDurationString(context, getDuration());
    }

    private String getDurationString(Context context, int seconds) {
        //Log.e("VVV", "getDurationString = " + seconds);
        int hours = seconds / 3600;
        int mins = (seconds / 60) - (hours * 60);
        int sec = seconds - ((hours * 3600) + (mins * 60));
        StringBuilder sb = new StringBuilder();
        if (hours > 0) {
            sb.append(hours + " " + context.getString(R.string.hours) + " ");
            sb.append(mins + " " + context.getString(R.string.mins) + " ");
            sb.append(sec + " " + context.getString(R.string.secs));
        } else if (mins > 0) {
            sb.append(mins + " " + context.getString(R.string.mins) + " ");
            sb.append(sec + " " + context.getString(R.string.secs));
        } else {
            sb.append(sec + " " + context.getString(R.string.secs));
        }
        return sb.toString();
    }
    private String getFormattedDouble(double d){
        return String.format("%.2f", d);
    }
    /**
     * returns the list from byte array for location list
     *
     * @return
     */
    private ArrayList<LatLng> getListFromBlob(byte[] data) {
        ArrayList<LatLng> result = new ArrayList<>();
        if (data != null) {
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
                ArrayList<String> list = (ArrayList<String>) ois.readObject();
                if (list != null)
                    for (String values : list) {
                        result.add(getLatLngFromString(values));
                    }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.ExerciseEntryColumns.INPUT_TYPE, mInputType);
        cv.put(DBConstants.ExerciseEntryColumns.ACTIVITY_TYPE, mActivityType);
        cv.put(DBConstants.ExerciseEntryColumns.DATE_TIME, mDateTime);
        cv.put(DBConstants.ExerciseEntryColumns.DURATION, mDuration);
        cv.put(DBConstants.ExerciseEntryColumns.DISTANCE, mDistance);
        cv.put(DBConstants.ExerciseEntryColumns.AVG_PACE, mAvgPace);
        cv.put(DBConstants.ExerciseEntryColumns.AVG_SPEED, mAvgSpeed);
        cv.put(DBConstants.ExerciseEntryColumns.CALORIE, mCalorie);
        cv.put(DBConstants.ExerciseEntryColumns.CLIMB, mClimb);
        cv.put(DBConstants.ExerciseEntryColumns.HEART_RATE, mHeartRate);
        cv.put(DBConstants.ExerciseEntryColumns.COMMENT, mComment);
        cv.put(DBConstants.ExerciseEntryColumns.GPS_DATA, getGpsBlob());
        return cv;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject cv = new JSONObject();
        cv.put(DBConstants.ExerciseEntryColumns._ID, id);
        cv.put(DBConstants.ExerciseEntryColumns.INPUT_TYPE, mInputType);
        cv.put(DBConstants.ExerciseEntryColumns.ACTIVITY_TYPE, mActivityType);
        cv.put(DBConstants.ExerciseEntryColumns.DATE_TIME, mDateTime);
        cv.put(DBConstants.ExerciseEntryColumns.DURATION, mDuration);
        cv.put(DBConstants.ExerciseEntryColumns.DISTANCE, mDistance);
        cv.put(DBConstants.ExerciseEntryColumns.AVG_PACE, mAvgPace);
        cv.put(DBConstants.ExerciseEntryColumns.AVG_SPEED, mAvgSpeed);
        cv.put(DBConstants.ExerciseEntryColumns.CALORIE, mCalorie);
        cv.put(DBConstants.ExerciseEntryColumns.CLIMB, mClimb);
        cv.put(DBConstants.ExerciseEntryColumns.HEART_RATE, mHeartRate);
        cv.put(DBConstants.ExerciseEntryColumns.COMMENT, mComment);
        return cv;
    }

    private String getLatLngString(LatLng loc) {
        return "" + loc.latitude + "," + loc.longitude;
    }

    private LatLng getLatLngFromString(String string) {
        String[] values = string.split(",");
        LatLng result = new LatLng(Double.valueOf(values[0]), Double.valueOf(values[1]));
        return result;
    }

    /**
     * returns the byte array for location list
     *
     * @return
     */
    private byte[] getGpsBlob() {

        byte[] result = null;
        ArrayList<String> values = new ArrayList<>();
        for (LatLng latLng : mLocationList) {
            values.add(getLatLngString(latLng));
        }
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = null;
            oos = new ObjectOutputStream(bos);
            // mArrayList is the ArrayList you want to store
            oos.writeObject(values);
            result = bos.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static double getDistanceAsPerUnit(double distanceInMiles, int unitType) {
        if (unitType == 2) {// miles\
            return distanceInMiles;
        } else {
            return distanceInMiles * 1.6; // convert to KMs
        }
    }

    public static ArrayList<ExerciseEntry> getListFromCursorList(Cursor cursor) {
        ArrayList<ExerciseEntry> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            result.add(new ExerciseEntry(cursor));
        }
        return result;
    }
    public static JSONObject getEntriesInJson(Cursor cursor) throws JSONException{
        JSONArray jArray = new JSONArray();
        while (cursor.moveToNext()) {
            ExerciseEntry entry = new ExerciseEntry(cursor);
            if(entry != null){
                jArray.put(entry.toJson());
            }
        }
        JSONObject json = new JSONObject();
        json.put(VALUES_JSON_DATA,jArray);
        return json;
    }
    public String getProperActivityType(Context context) {
        return context.getResources().getStringArray(R.array.activity_type)[mActivityType];
    }
}
