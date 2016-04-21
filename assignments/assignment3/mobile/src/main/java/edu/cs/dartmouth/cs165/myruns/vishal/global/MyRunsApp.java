package edu.cs.dartmouth.cs165.myruns.vishal.global;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import edu.cs.dartmouth.cs165.myruns.vishal.storage.db.ExerciseEntryDbHelper;

/**
 * Created by Vishal Gaurav
 */
public class MyRunsApp extends Application {
    public static final String TAG = "MyRuns";

    public ExerciseEntryDbHelper mDbHelper = null;

    @Override
    public void onCreate() {
        mDbHelper = new ExerciseEntryDbHelper(getBaseContext());
        mDbHelper.getReadableDatabase();// just to create db on first launch
        super.onCreate();
    }

    /**
     * return DB instance from Context
     * @param context
     * @return
     */
    public synchronized  static ExerciseEntryDbHelper getDb(Context context){
        return ((MyRunsApp)context.getApplicationContext()).mDbHelper;
    }

    public static final String getTag(String appendTag) {
        return TAG + " : " + appendTag;
    }

    public static final void printTrace(String tag, String msg) {
        Log.v(tag, msg);
    }
}
