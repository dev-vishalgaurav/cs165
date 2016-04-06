package edu.cs.dartmouth.cs165.myruns.vishal.global;

import android.app.Application;
import android.util.Log;

/**
 * Created by Vishal Gaurav
 */
public class MyRunsApp extends Application {
    public static final String TAG = "MyRuns";

    @Override
    public void onCreate() {

        super.onCreate();
    }

    public static final String getTag(String appendTag) {
        return TAG + " : " + appendTag;
    }

    public static final void printTrace(String tag, String msg) {
        Log.v(tag, msg);
    }
}
