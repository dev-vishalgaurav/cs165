/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.cs.dartmouth.cs165.myruns.vishal.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.cs.dartmouth.cs165.myruns.vishal.R;
import edu.cs.dartmouth.cs165.myruns.vishal.global.MyRunsApp;
import edu.cs.dartmouth.cs165.myruns.vishal.storage.db.ExerciseEntryDbHelper;
import edu.cs.dartmouth.cs165.myruns.vishal.ui.activity.HomeActivity;

public class MyRunsGcmListenerService extends GcmListenerService {

    private static final String TAG = "VVV";
    public static final String BROADCAST_TYPE_DATA_UPDATE = "broadcast_data_update";
    public static final String KEY_EVENT_TYPE = "key_event_type";
    public static final String KEY_IDS = "ids";
    public static final String TYPE_DELETE = "delete";
    /*{"key_event_type" : "delete","ids":[1,2]}*/
    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        try {
            JSONObject obj = new JSONObject(message);
            if(obj.has(KEY_EVENT_TYPE)){
                if(obj.getString(KEY_EVENT_TYPE).equalsIgnoreCase(TYPE_DELETE)){
                    JSONArray jArray = obj.getJSONArray(KEY_IDS);
                    ExerciseEntryDbHelper db = MyRunsApp.getDb(getBaseContext());
                    for(int i = 0 ; i < jArray.length() ; i++){
                        db.removeEntry(jArray.getLong(i));
                    }
                    sendDataUpdateBroadcast();
                }
            }
        }catch (JSONException ex){
            ex.printStackTrace();
        }catch (NumberFormatException ex){
            ex.printStackTrace();
        }


        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        //sendNotification(message);
        // [END_EXCLUDE]
    }

    private void sendDataUpdateBroadcast() {
        Intent syncComplete = new Intent(BROADCAST_TYPE_DATA_UPDATE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(syncComplete);
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.dartmouth)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
