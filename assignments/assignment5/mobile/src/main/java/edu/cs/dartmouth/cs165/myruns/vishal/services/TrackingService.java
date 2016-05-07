package edu.cs.dartmouth.cs165.myruns.vishal.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.AsyncTask;
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
import java.util.concurrent.ArrayBlockingQueue;

import edu.cs.dartmouth.cs165.myruns.vishal.R;
import edu.cs.dartmouth.cs165.myruns.vishal.classifier.Globals;
import edu.cs.dartmouth.cs165.myruns.vishal.classifier.WekaClassifier;
import edu.cs.dartmouth.cs165.myruns.vishal.classifier.meapsoft.FFT;
import edu.cs.dartmouth.cs165.myruns.vishal.storage.db.ExerciseEntry;
import edu.cs.dartmouth.cs165.myruns.vishal.ui.activity.MapDisplayActivity;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class TrackingService extends Service implements SensorEventListener {

    public interface OnTrackingUpdateListener {

        void onEntryUpdate(ExerciseEntry entry);
    }

    private static final int WINDOW_SIZE = 3;
    public static final int TRACKING_SERVICE_NOTIFICATION_ID = 1;
    public static final double AVERAGE_WEIGHT_OF_MAN = 74;
    private OnTrackingUpdateListener mOnTrackingUpdateListener = null;
    private ExerciseEntry mExerciseEntry = null;
    private GoogleApiClient mGoogleApiClient = null;
    private boolean isGoogleApiConnected = false;
    private LocationRequest mLocationRequest = null;
    private boolean isRequestingLocationUpdates = false;
    private Location lastLocation = null;
    private long startTime = System.currentTimeMillis();
    private double distanceTravelled = 0.0;
    private double currentClimb = 0.0;
    /*declaring activity recognition variables*/
    private static final int mFeatLen = Globals.ACCELEROMETER_BLOCK_CAPACITY + 2;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private int mServiceTaskType;
    private Instances mDataset;
    private Attribute mClassAttribute;
    private OnSensorChangedTask mAsyncTask;
    private static ArrayBlockingQueue<Double> mAccBuffer;
    public static final DecimalFormat mdf = new DecimalFormat("#.##");

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
        startActivityUpdate();
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
        mExerciseEntry = new ExerciseEntry(-1l, 0, 0, Calendar.getInstance().getTimeInMillis(), 0, 0, 0, 0, 0, 0, 0, "");
        mExerciseEntry.setLocationList(new ArrayList<LatLng>());
    }

    private void startActivityUpdate() {
        Log.e("VVV", "startActivityUpdate :- onStartCommand");
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        Log.e("VVV","mAccelerometer = " + mAccelerometer);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        mServiceTaskType = Globals.SERVICE_TASK_TYPE_COLLECT;
        mAccBuffer = new ArrayBlockingQueue<Double>(Globals.ACCELEROMETER_BUFFER_CAPACITY);
        // Create the container for attributes
        ArrayList<Attribute> allAttr = new ArrayList<Attribute>();

        // Adding FFT coefficient attributes
        DecimalFormat df = new DecimalFormat("0000");

        for (int i = 0; i < Globals.ACCELEROMETER_BLOCK_CAPACITY; i++) {
            allAttr.add(new Attribute(Globals.FEAT_FFT_COEF_LABEL + df.format(i)));
        }
        // Adding the max feature
        allAttr.add(new Attribute(Globals.FEAT_MAX_LABEL));
        // Declare a nominal attribute along with its candidate values
        ArrayList<String> labelItems = new ArrayList<String>(4);
        labelItems.add(Globals.CLASS_LABEL_STANDING);
        labelItems.add(Globals.CLASS_LABEL_WALKING);
        labelItems.add(Globals.CLASS_LABEL_RUNNING);
        labelItems.add(Globals.CLASS_LABEL_OTHER);
        mClassAttribute = new Attribute(Globals.CLASS_LABEL_KEY, labelItems);
        allAttr.add(mClassAttribute);
        // Construct the dataset with the attributes specified as allAttr and
        // capacity 10000
        mDataset = new Instances(Globals.FEAT_SET_NAME, allAttr, Globals.FEATURE_SET_CAPACITY);
        // Set the last column/attribute (standing/walking/running) as the class
        // index for classification
        mDataset.setClassIndex(mDataset.numAttributes() - 1);
        mAsyncTask = new OnSensorChangedTask();
        mAsyncTask.execute();
    }

    private void stopActivityUpdate() {
        mAsyncTask.cancel(true);
        mSensorManager.unregisterListener(this);
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
            mLocationRequest.setSmallestDisplacement(0.0f);
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
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            Log.e("VVV","onSensorChanged :- Linear Acceleration");
            String record = event.values[0] + "," + event.values[1] + "," + event.values[2] + "\n";
            double m = Math.sqrt(event.values[0] * event.values[0]
                    + event.values[1] * event.values[1] + event.values[2]
                    * event.values[2]);

            // Inserts the specified element into this queue if it is possible
            // to do so immediately without violating capacity restrictions,
            // returning true upon success and throwing an IllegalStateException
            // if no space is currently available. When using a
            // capacity-restricted queue, it is generally preferable to use
            // offer.

            try {
                mAccBuffer.add(new Double(m));
            } catch (IllegalStateException e) {

                // Exception happens when reach the capacity.
                // Doubling the buffer. ListBlockingQueue has no such issue,
                // But generally has worse performance
                ArrayBlockingQueue<Double> newBuf = new ArrayBlockingQueue<Double>(
                        mAccBuffer.size() * 2);

                mAccBuffer.drainTo(newBuf);
                mAccBuffer = newBuf;
                mAccBuffer.add(new Double(m));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("VVV", "Taracking service onDestroy");
        stopActivityUpdate();
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

    private class OnSensorChangedTask extends AsyncTask<Void, Void, Void> {
        private int[] windowBuffer = new int[WINDOW_SIZE];
        private boolean isWindowBufferFull = false;
        private int windowSize = 0;

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.e("VVV", "Sensor service :- doInBackground");
            Instance inst = new DenseInstance(mFeatLen);
            inst.setDataset(mDataset);
            int blockSize = 0;
            FFT fft = new FFT(Globals.ACCELEROMETER_BLOCK_CAPACITY);
            double[] accBlock = new double[Globals.ACCELEROMETER_BLOCK_CAPACITY];
            double[] re = accBlock;
            double[] im = new double[Globals.ACCELEROMETER_BLOCK_CAPACITY];

            double max = Double.MIN_VALUE;

            while (true) {
                try {
                    // need to check if the AsyncTask is cancelled or not in the while loop
                    if (isCancelled() == true) {
                        return null;
                    }


                    // Dumping buffer
                    accBlock[blockSize++] = mAccBuffer.take().doubleValue();

                    if (blockSize == Globals.ACCELEROMETER_BLOCK_CAPACITY) {
                        blockSize = 0;

                        // time = System.currentTimeMillis();
                        max = .0;
                        for (double val : accBlock) {
                            if (max < val) {
                                max = val;
                            }
                        }

                        fft.fft(re, im);

                        for (int i = 0; i < re.length; i++) {
                            double mag = Math.sqrt(re[i] * re[i] + im[i]
                                    * im[i]);
                            inst.setValue(i, mag);
                            im[i] = .0; // Clear the field
                        }

                        // Append max after frequency component
                        inst.setValue(Globals.ACCELEROMETER_BLOCK_CAPACITY, max);
                        //inst.setValue(mClassAttribute, mLabel);
                        mDataset.add(inst);
                        Log.i("VVV", mDataset.size() + "");
                        Double[] array = getDoubleArray(inst.toDoubleArray());
                        int label = (int) WekaClassifier.classify(array);
                        Log.e("VVV", "Label :-" + label);
                        updateBufferAndNotify(label);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void updateBufferAndNotify(int label) {
            if (!isWindowBufferFull) {
                Log.e("VVV", "Window is not full :- " + label);
                windowBuffer[windowSize] = label;
                windowSize++;
                if (windowSize == WINDOW_SIZE - 1) {
                    isWindowBufferFull = true;
                }
            } else {
                Log.e("VVV", "will update label " + label);
                windowBuffer[windowSize] = label;
                int dominantLabel = getDominantLabel(windowBuffer);
                windowSize = (windowSize == WINDOW_SIZE - 1) ? 0 : windowSize + 1;
                sendResultToReceiver(dominantLabel);
            }
            //printBuffer();
        }


        private int getDominantLabel(int[] windowBuffer) {
            int dominant = 0;
            int[] counts = new int[windowBuffer.length];
            for (int i = 0; i < windowBuffer.length; i++) {
                counts[windowBuffer[i]]++;
            }

            for (int i = 0; i < counts.length; i++) {
                if (counts[i] > counts[dominant]) {
                    dominant = i;
                }
            }
            return dominant;
        }

        private void printBuffer() {
            Log.e("VVV", "Buffer data");
            for (int i = 0; i < windowBuffer.length; i++) {
                Log.e("VVV", "Index = " + i + " value =  " + windowBuffer[i]);
            }
        }

        private void sendResultToReceiver(int result) {
            Log.e("VVV", "update result in entry, recognition result = " + result);
//            if (mReciver != null) {
//                Bundle resultBundle = new Bundle();
//                resultBundle.putInt(Globals.EXTRA_RESULT, result);
//                mReciver.send(Activity.RESULT_OK, resultBundle);
//            }
        }

        private Double[] getDoubleArray(double[] arrayToCast) {
            Double[] result = new Double[arrayToCast.length];
            for (int i = 0; i < arrayToCast.length; i++) {
                result[i] = arrayToCast[i];
            }
            return result;
        }

        @Override
        protected void onCancelled() {

            Log.e("123", mDataset.size() + "");
            Log.e("VVV", "Sensor service :- onCancelled");
            if (mServiceTaskType == Globals.SERVICE_TASK_TYPE_CLASSIFY) {
                super.onCancelled();
                return;
            }
            super.onCancelled();
        }

    }

    private void onGoogleApiDisabled() {
        isGoogleApiConnected = false;
    }

    private void onGoogleApiEnabled(Bundle connectionHint) {
        isGoogleApiConnected = true;
        startRequestingLocationUpdates();
    }

    private LatLng getLatLngFromLocation(Location location) {
        return (location != null) ? new LatLng(location.getLatitude(), location.getLongitude()) : null;
    }

    private LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location changedLocation) {

            Log.e("VVV", "onLocationChanged called location = " + changedLocation + "Altitude = " + changedLocation.getAltitude() + " Speed = " + changedLocation.hasSpeed() + " = " + changedLocation.getSpeed());
            LatLng updatedLocation = getLatLngFromLocation(changedLocation);
            if (updatedLocation != null) {
                if (lastLocation != null) {
                    distanceTravelled = distanceTravelled + changedLocation.distanceTo(lastLocation);
                    if (changedLocation.getAltitude() > lastLocation.getAltitude()) {
                        currentClimb += changedLocation.getAltitude() - lastLocation.getAltitude();
                    }
                }
                DecimalFormat format = new DecimalFormat("0.00");
                double distanceInMiles = distanceTravelled / 1600; // in miles
                double climbInMiles = currentClimb / 1600;
                Log.e("VVV", "Distance travelled in miles= " + distanceTravelled);
                mExerciseEntry.setDistance(Double.valueOf(format.format(distanceInMiles)));
                double timeElapsed = (System.currentTimeMillis() - startTime) / 3600; // in hrs
                double avgSpeed = (timeElapsed == 0) ? 0.0 : distanceInMiles / timeElapsed;
                int calorieBurnt = (int) ((AVERAGE_WEIGHT_OF_MAN / 400) * distanceTravelled); // some random formula from internet
                // set formatted values to entry object
                mExerciseEntry.setAvgPace(Double.valueOf(format.format(avgSpeed)));
                mExerciseEntry.setCurrentSpeed(Double.valueOf(format.format(changedLocation.getSpeed())));
                mExerciseEntry.setClimb(Double.valueOf(format.format(climbInMiles)));
                mExerciseEntry.getLocationList().add(updatedLocation);
                mExerciseEntry.setCalorie(calorieBurnt);
                if (mOnTrackingUpdateListener != null) {
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
            Log.e("VVV", "OnGoogleApiConnected");
            onGoogleApiEnabled(connectionHint);
        }
    };

    public static void start(Context context) {
        Intent intent = new Intent(context, TrackingService.class);
        context.startService(intent);
    }

    public static void bind(Context context, ServiceConnection mConnection) {
        Intent intent = new Intent(context, TrackingService.class);
        context.bindService(intent, mConnection, BIND_AUTO_CREATE);
    }
}
