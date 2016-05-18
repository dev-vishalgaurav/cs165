package edu.cs.dartmouth.cs165.myruns.vishal.backend.data;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import org.json.simple.JSONObject;

/**
 * Created by Vishal Gaurav on 5/17/16.
 */
@Entity
public class ExerciseEntry {

    public interface ExerciseEntryColumns {

        public static final String _ID = "_id"; // BASE COLUMNS
        public static final String INPUT_TYPE = "input_type";
        public static final String ACTIVITY_TYPE = "activity_type";
        public static final String DATE_TIME = "date_time";
        public static final String DURATION = "duration";
        public static final String DISTANCE = "distance";
        public static final String AVG_PACE = "avg_pace";
        public static final String AVG_SPEED = "avg_speed";
        public static final String CALORIE = "calories";
        public static final String CLIMB = "climb";
        public static final String HEART_RATE = "heart_rate";
        public static final String COMMENT = "comment";
    }

    public static final String EXERCISE_ENTRY_PARENT_ENTITY_NAME = "EntryParent";
    public static final String EXERCISE_ENTRY_PARENT_KEY_NAME = "EntryParent";
    public static final String EXERCISE_ENTRY_ENTITY_NAME = "Entry";

    @Id
    public Long _id;

    @Index
    public String id;
    public int mInputType;        // Manual, GPS or automatic
    public int mActivityType;     // Running, cycling etc.
    public long mDateTime;    // When does this entry happen
    public int mDuration;         // Exercise duration in seconds
    public double mDistance;      // Distance traveled. Either in meters or feet.
    public double mAvgPace;       // Average pace
    public double mAvgSpeed;      // Average speed
    public int mCalorie;          // Calories burnt
    public double mClimb;         // Climb. Either in meters or feet.
    public int mHeartRate;        // Heart rate
    public String mComment;       // Comments

    public ExerciseEntry(JSONObject obj){
        this.id = "" + Long.parseLong(obj.get(ExerciseEntryColumns._ID).toString());
        this.mInputType = Integer.parseInt(obj.get(ExerciseEntryColumns.INPUT_TYPE).toString());
        this.mActivityType = Integer.parseInt(obj.get(ExerciseEntryColumns.ACTIVITY_TYPE).toString());
        this.mDateTime = Long.parseLong(obj.get(ExerciseEntryColumns.DATE_TIME).toString());
        this.mDuration = Integer.parseInt(obj.get(ExerciseEntryColumns.DURATION).toString());
        this.mDistance = Double.parseDouble(obj.get(ExerciseEntryColumns.DISTANCE).toString());
        this.mAvgPace = Double.parseDouble(obj.get(ExerciseEntryColumns.AVG_PACE).toString());
        this.mAvgSpeed = Double.parseDouble(obj.get(ExerciseEntryColumns.AVG_SPEED).toString());
        this.mCalorie = Integer.parseInt(obj.get(ExerciseEntryColumns.CALORIE).toString());
        this.mClimb = Double.parseDouble(obj.get(ExerciseEntryColumns.CLIMB).toString());
        this.mHeartRate = Integer.parseInt(obj.get(ExerciseEntryColumns.HEART_RATE).toString());
        this.mComment = (String) obj.get(ExerciseEntryColumns.COMMENT);;
    }

    public ExerciseEntry(String id, int mInputType, int mActivityType, long mDateTime, int mDuration, double mDistance, double mAvgPace, double mAvgSpeed, int mCalorie, double mClimb, int mHeartRate, String mComment) {
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
}
