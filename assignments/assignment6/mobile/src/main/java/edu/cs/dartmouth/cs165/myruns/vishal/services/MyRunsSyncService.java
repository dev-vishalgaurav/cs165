package edu.cs.dartmouth.cs165.myruns.vishal.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpStatusCodes;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.cs.dartmouth.cs165.myruns.vishal.global.MyRunsApp;
import edu.cs.dartmouth.cs165.myruns.vishal.storage.db.ExerciseEntry;
import edu.cs.dartmouth.cs165.myruns.vishal.storage.preferences.PreferenceUtils;
import edu.cs.dartmouth.cs165.myruns.vishal.utils.NetworkUtils;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyRunsSyncService extends IntentService {
    public static final String EXTRA_RESULT = "extra_result";
    public static final String EXTRA_MESSAGE = "extra_message";
    public static final String BROADCAST_SYNC_COMPLETE = "sync_complete";
    public MyRunsSyncService() {
        super("MyRunsSyncService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("VVV","MuRunsSyncService :- onHandleIntent");
        Intent syncComplete = new Intent(BROADCAST_SYNC_COMPLETE);
        syncComplete.putExtra(EXTRA_RESULT, Activity.RESULT_CANCELED);

        try {
            JSONObject entriesJson = ExerciseEntry.getEntriesInJson(MyRunsApp.getDb(getBaseContext()).fetchAllEntryCursor());
            Uri uri = NetworkUtils.URL_POST;
            URL url = new URL(uri.toString() + "?" + ExerciseEntry.VALUES_JSON_DATA + "=" + entriesJson.toString());
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setUseCaches(true);
            httpConn.setDoOutput(true);
            httpConn.setRequestMethod("GET");

            httpConn.connect();
            int responseCode = httpConn.getResponseCode();
            String responseMessage = httpConn.getResponseMessage();
            syncComplete.putExtra(EXTRA_MESSAGE,responseMessage);
            if(responseCode == HttpStatusCodes.STATUS_CODE_OK){
                // success
                syncComplete.putExtra(EXTRA_RESULT, Activity.RESULT_OK);
                Log.e("VVV","MuRunsSyncService :- Success");
            }else{
                //failure
                Log.e("VVV","MuRunsSyncService :- Server Error");
            }
        }catch (JSONException ex){
            Log.e("VVV","MuRunsSyncService :- Error");
            ex.printStackTrace();
        }catch (IOException ex){
            Log.e("VVV","MuRunsSyncService :- Error");
            ex.printStackTrace();
        }
        // Notify UI that sync has completed, so the progress indicator can be hidden.
        LocalBroadcastManager.getInstance(this).sendBroadcast(syncComplete);
    }

    public static void start(Context context){
        Intent intent = new Intent(context,MyRunsSyncService.class);
        context.startService(intent);
    }

}
