package edu.cs.dartmouth.cs165.myruns.vishal.storage.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by Vishal Gaurav
 */
public class ExerciseEntry {

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

    public ExerciseEntry(Cursor cursor){
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
    private ArrayList<LatLng> getListFromBlob(byte[] data){
        ArrayList<LatLng> result = new ArrayList<>();
        if(data!=null) {
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
                ArrayList<LatLng> list = (ArrayList<LatLng>) ois.readObject();
                if (list != null)
                    result.addAll(list);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return  result;
    }
    public ContentValues toContentValues(){
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.ExerciseEntryColumns.INPUT_TYPE,mInputType);
        cv.put(DBConstants.ExerciseEntryColumns.ACTIVITY_TYPE,mInputType);
        cv.put(DBConstants.ExerciseEntryColumns.DATE_TIME,mDateTime);
        cv.put(DBConstants.ExerciseEntryColumns.DURATION,mDuration);
        cv.put(DBConstants.ExerciseEntryColumns.DISTANCE,mDistance);
        cv.put(DBConstants.ExerciseEntryColumns.AVG_PACE,mAvgPace);
        cv.put(DBConstants.ExerciseEntryColumns.AVG_SPEED,mAvgSpeed);
        cv.put(DBConstants.ExerciseEntryColumns.CALORIE,mCalorie);
        cv.put(DBConstants.ExerciseEntryColumns.CLIMB,mClimb);
        cv.put(DBConstants.ExerciseEntryColumns.HEART_RATE,mHeartRate);
        cv.put(DBConstants.ExerciseEntryColumns.COMMENT,mComment);
        cv.put(DBConstants.ExerciseEntryColumns.GPS_DATA,getGpsBlob());
        return  cv;
    }
    private byte[] getGpsBlob(){
        byte[] result = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = null;
            oos = new ObjectOutputStream(bos);
            // mArrayList is the ArrayList you want to store
            oos.writeObject(mLocationList);
            result = bos.toByteArray();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }
}
