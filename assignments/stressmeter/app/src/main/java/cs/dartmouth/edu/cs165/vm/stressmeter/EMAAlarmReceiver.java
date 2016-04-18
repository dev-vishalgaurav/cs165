package cs.dartmouth.edu.cs165.vm.stressmeter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by varun on 1/20/15.
 */

public class EMAAlarmReceiver extends BroadcastReceiver {
    //Receive broadcast, upon which start the stress meter
    @Override
    public void onReceive(final Context context, Intent intent) {
        startPSM(context);
    }

    // Start the stress meter
    private void startPSM(Context context) {
        Intent emaIntent = new Intent(context, StressMeter.class); //The activity you  want to start.
        emaIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(emaIntent);
    }
}