package edu.cs.dartmouth.cs165.myruns.vishal.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import edu.cs.dartmouth.cs165.myruns.vishal.storage.db.ExerciseEntry;

public class TrackingService extends Service {

    public interface OnTrackingUpdateListener {

    }

    private OnTrackingUpdateListener mOnTrackingUpdateListener = null;
    private ExerciseEntry mExerciseEntry = null;

    public TrackingService() {
        super();
        Log.e("VVV", "Tagging service()");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("VVV", "Tagging service onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("VVV", "Tagging service onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    public OnTrackingUpdateListener getOnTrackingUpdateListener() {
        return mOnTrackingUpdateListener;
    }

    public void setOnTrackingUpdateListener(OnTrackingUpdateListener mOnTrackingUpdateListener) {
        this.mOnTrackingUpdateListener = mOnTrackingUpdateListener;
    }

    /**
     *
     */
    private void setUpNotification() {

    }

    private void clearNotification() {

    }

    private void initExerciseEntry() {

    }

    private void startLocationUpdate() {

    }

    private void startActivityUpdate() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("VVV", "Tagging service onDestroy called");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("VVV", "Tagging service onUnBind called");
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("VVV", "Tagging service onBind called");
        return new TrackingBinder(this);
    }
}
