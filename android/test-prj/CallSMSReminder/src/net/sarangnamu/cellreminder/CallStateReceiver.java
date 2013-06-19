package net.sarangnamu.cellreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class CallStateReceiver extends BroadcastReceiver {
    private static final String TAG = "CallStateReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String phone_state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (CallSMSReminderActivity.reminder == null) {
            return ;
        }

        if (phone_state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
        } else if (phone_state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            //            Log.d(TAG, "===================================================================");
            //            Log.d(TAG, "IDLE");
            //            Log.d(TAG, "===================================================================");
            //
            //            new Thread(new Runnable() {
            //                @Override
            //                public void run() {
            //                    try {
            //                        Thread.sleep(500);
            //                        CallSMSReminderActivity.reminder.refresh();
            //                    } catch (Exception e) {
            //                        e.printStackTrace();
            //                    }
            //                }
            //            });
        }
    }
}
