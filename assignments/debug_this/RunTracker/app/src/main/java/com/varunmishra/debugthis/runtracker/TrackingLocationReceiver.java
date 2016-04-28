package com.varunmishra.debugthis.runtracker;

import android.content.Context;
import android.location.Location;
import android.util.Log;

public class TrackingLocationReceiver extends LocationReceiver {
    
    @Override
    protected void onLocationReceived(Context c, Location loc) {
        Log.d("VVV","Tracking onLocationReceived");
        RunManager.get(c).insertLocation(loc);
    }

}
