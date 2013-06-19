package net.sarangnamu.testc2dm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * C2DM 에서 데이터 를 받는 처리를 하는 클래스
 * 
 */
public abstract class C2DMReceiver extends BroadcastReceiver {
    private static final String TAG = "C2DMReceiver";

    private static final String C2DM_INTENT_RECEIVE      = "com.google.android.c2dm.intent.RECEIVE";
    private static final String C2DM_INTENT_REGISTRATION = "com.google.android.c2dm.intent.REGISTRATION";

    private static final String RECEIVE_ERROR            = "error";
    private static final String RECEIVE_UNREGISTRATION   = "unregistered";
    private static final String RECEIVE_REGISTRATION_ID  = "registration_id";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action == null) {
            Log.e(TAG, "===================================================================");
            Log.e(TAG, "ERROR: action is null");
            Log.e(TAG, "===================================================================");
            return ;
        }

        if (action.equals(C2DM_INTENT_REGISTRATION)) {
            String resID = intent.getStringExtra(RECEIVE_REGISTRATION_ID);

            if (intent.getStringExtra(RECEIVE_ERROR) != null) {
                onError();
            } else if (intent.getStringExtra(RECEIVE_UNREGISTRATION) != null) {
                unRegistration();
            } else if (resID != null) {
                C2DMManager.getInstance().setRegistrationID(resID);
            }
        } else if (action.equals(C2DM_INTENT_RECEIVE)) {
            Log.d(TAG, "===================================================================");
            Log.e(TAG, "msg : " + intent.getStringExtra("msg"));
            Log.d(TAG, "===================================================================");
            onPushReceive(context, intent);
        }
    }

    public abstract void onError();
    public abstract void unRegistration();
    public abstract void onPushReceive(Context context, Intent intent);
}
