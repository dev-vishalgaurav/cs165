package edu.cs.dartmouth.cs165.myruns.vishal.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import edu.cs.dartmouth.cs165.myruns.vishal.R;
import edu.cs.dartmouth.cs165.myruns.vishal.storage.db.ExerciseEntry;
import edu.cs.dartmouth.cs165.myruns.vishal.ui.activity.MapDisplayActivity;

public class TrackingService extends Service {

    public interface OnTrackingUpdateListener {

        void onEntryUpdate(ExerciseEntry entry);
    }
    public static final int TRACKING_SERVICE_NOTIFICATION_ID = 1;
    public static final double AVERAGE_WEIGHT_OF_MAN = 74;
    private OnTrackingUpdateListener mOnTrackingUpdateListener = null;
    private ExerciseEntry mExerciseEntry = null;
    private GoogleApiClient mGoogleApiClient = null;
    private boolean isGoogleApiConnected = false;
    private LocationRequest mLocationRequest = null;
    private boolean isRequestingLocationUpdates = false;
    private Location lastLocation = null;
    private long startTime  = System.currentTimeMillis();
    private double distanceTravelled = 0.0 ;
    private double currentClimb = 0.0 ;
    public TrackingService() {
        super();
        Log.e("VVV", "Tagging service()");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("VVV", "Tagging service onCreate");
        buildGoogleApiClient();
        initExerciseEntry();
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
    private void stopRequestingLocationUpdates() {
        if (isRequestingLocationUpdates) {
            clearNotification();
            isRequestingLocationUpdates = false;
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, mLocationListener);
        }
    }
    /**
     *
     */
    private void setUpNotification() {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setTicker(getText(R.string.label_notification_tracking));
        builder.setContentTitle(getText(R.string.app_name));
        builder.setContentText(getText(R.string.label_notification_tracking));
        builder.setSmallIcon(R.drawable.greend);
        Intent notificationIntent = new Intent(this, MapDisplayActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentIntent(pendingIntent);
        startForeground(TRACKING_SERVICE_NOTIFICATION_ID, builder.build());
    }

    private void clearNotification() {
        stopForeground(true);
    }

    private void initExerciseEntry() {
        mExerciseEntry =  new ExerciseEntry(-1l,0,0, Calendar.getInstance().getTimeInMillis(),0,0,0,0,0,0,0,"");
        mExerciseEntry.setLocationList(new ArrayList<LatLng>());
    }

    private void startActivityUpdate() {

    }
    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(mGoogleApiCallback).addOnConnectionFailedListener(mConnectionFailedCallback)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    private void createLocationRequest() {
        if (mLocationRequest == null) {
            // currently hard coded parameters are set
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(100);
            mLocationRequest.setFastestInterval(500);
            mLocationRequest.setSmallestDisplacement (0.0f);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }
    }
    private void startRequestingLocationUpdates() {
        setUpNotification();
        createLocationRequest();
        isRequestingLocationUpdates = true;
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mLocationListener);
        startTime = System.currentTimeMillis();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("VVV", "Tagging service onDestroy");
        clearNotification();
        stopRequestingLocationUpdates();
        mGoogleApiClient.disconnect();
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
    private void onGoogleApiDisabled() {
        isGoogleApiConnected = false;
    }
    private void onGoogleApiEnabled(Bundle connectionHint) {
        isGoogleApiConnected = true;
        startRequestingLocationUpdates();
    }
    private LatLng getLatLngFromLocation(Location location){
        return (location!=null) ? new LatLng(location.getLatitude(), location.getLongitude()) : null ;
    }
    private LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location changedLocation) {

            Log.e("VVV","onLocationChanged called location = " + changedLocation + "Altitude = " + changedLocation.getAltitude() + " Speed = " + changedLocation.hasSpeed() + " = " + changedLocation.getSpeed());
            LatLng updatedLocation = getLatLngFromLocation(changedLocation);
            if(updatedLocation != null){
                if(lastLocation!=null){
                    distanceTravelled = distanceTravelled + changedLocation.distanceTo(lastLocation);
                    if(changedLocation.getAltitude() > lastLocation.getAltitude()) {
                        currentClimb += changedLocation.getAltitude() - lastLocation.getAltitude();
                    }
                }
                DecimalFormat format = new DecimalFormat("0.00");
                double distanceInMiles = distanceTravelled/1600 ; // in miles
                double climbInMiles = currentClimb / 1600;
                Log.e("VVV","Distance travelled in miles= " + distanceTravelled);
                mExerciseEntry.setDistance(Double.valueOf(format.format(distanceInMiles)));
                double timeElapsed = (System.currentTimeMillis() - startTime)/3600; // in hrs
                double avgSpeed = (timeElapsed == 0) ? 0.0 : distanceInMiles / timeElapsed;
                int calorieBurnt = (int)((AVERAGE_WEIGHT_OF_MAN / 400 ) * distanceTravelled ); // some random formula from internet
                // set formatted values to entry object
                mExerciseEntry.setAvgPace(Double.valueOf(format.format(avgSpeed)));
                mExerciseEntry.setCurrentSpeed(Double.valueOf(format.format(changedLocation.getSpeed())));
                mExerciseEntry.setClimb(Double.valueOf(format.format(climbInMiles)));
                mExerciseEntry.getLocationList().add(updatedLocation);
                mExerciseEntry.setCalorie(calorieBurnt);
                if(mOnTrackingUpdateListener!=null) {
                    mOnTrackingUpdateListener.onEntryUpdate(mExerciseEntry);
                }
                lastLocation = changedLocation;
            }
        }
    };
    private GoogleApiClient.OnConnectionFailedListener mConnectionFailedCallback = new GoogleApiClient.OnConnectionFailedListener() {

        @Override
        public void onConnectionFailed(ConnectionResult arg0) {
            onGoogleApiDisabled();
        }
    };
    private GoogleApiClient.ConnectionCallbacks mGoogleApiCallback = new GoogleApiClient.ConnectionCallbacks() {

        @Override
        public void onConnectionSuspended(int arg0) {
            onGoogleApiDisabled();
        }

        @Override
        public void onConnected(Bundle connectionHint) {
            Log.e("VVV","OnGoogleApiConnected");
            onGoogleApiEnabled(connectionHint);
        }
    };

    public static void start(Context context){
        Intent intent = new Intent(context,TrackingService.class);
        context.startService(intent);
    }
    public static void bind(Context context, ServiceConnection mConnection){
        Intent intent = new Intent(context,TrackingService.class);
        context.bindService(intent,mConnection,BIND_AUTO_CREATE);
    }
}
