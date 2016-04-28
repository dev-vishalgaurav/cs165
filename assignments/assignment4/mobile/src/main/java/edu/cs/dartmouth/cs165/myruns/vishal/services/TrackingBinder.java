package edu.cs.dartmouth.cs165.myruns.vishal.services;

/**
 * Created by Vishal Gaurav
 */

import android.os.Binder;

/**
 * Binder acting as a mediator between Activity and Service
 */
public class TrackingBinder extends Binder {
    private TrackingService mService = null;

    /**
     * constructor requiring the @link TrackingService instance;
     *
     * @param service
     */
    public TrackingBinder(TrackingService service) {
        this.mService = service;
    }

    public TrackingService getTrackingService() {
        return mService;
    }

    /**
     * sets the tracking update listener for service
     *
     * @param mTrackingListener
     */
    public void setOnTrackingUpdateListener(TrackingService.OnTrackingUpdateListener mTrackingListener) {
        mService.setOnTrackingUpdateListener(mTrackingListener);
    }

    /**
     * this method will call stopService on the service instance
     */
    public void stopService() {
        mService.stopSelf();
    }


}