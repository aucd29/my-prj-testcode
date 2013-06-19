package net.sarangnamu.testc2dm;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CTestReceiver extends C2DMReceiver {
    private static final String TAG = "CTestReceiver";

    static String c2dm_msg = "";
    static String registration_id = null;
    //
    //    @Override
    //    public void onReceive(Context context, Intent intent) {
    //        // 리시버로 받은 데이터가 Registration ID이면
    //        if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) {
    //            handleRegistration(context, intent);
    //        }
    //        // 리시버가 받은 데이터가 메세지이면
    //        else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
    //
    //            // 추출
    //
    //            c2dm_msg = intent.getStringExtra("msg");
    //            c2dm_msg = intent.getStringExtra("url");
    //
    //            // 출력
    //            Log.d(TAG, "===================================================================");
    //            Log.v("C2DM", "C2DM Message : " + c2dm_msg);
    //            Log.d(TAG, "===================================================================");
    //
    //            Toast toast = Toast.makeText(context, c2dm_msg, Toast.LENGTH_SHORT);
    //            toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 150);
    //            toast.show();
    //        }
    //    }
    //
    //    public void handleRegistration(Context context, Intent intent) {
    //
    //        registration_id = intent.getStringExtra("registration_id");
    //
    //        // 받은 메세지가 error일 경우
    //        if (intent.getStringExtra("error") != null) {
    //            Log.v("C2DM", "C2DM REGISTRATION : Registration failed,"
    //                    + "should try again later");
    //        }
    //        // 받은 메세지가 unregistered일 경우
    //        else if (intent.getStringExtra("unregistered") != null) {
    //            Log.v("C2DM", "C2DM REGISTRATION : unregistration done, "
    //                    + "new messages from the authorized "
    //                    + "sender will be rejected");
    //        }
    //        // 받은 메세지가 Registration ID일 경우
    //        else if (registration_id != null) {
    //            Log.v("C2DM", "Registration ID complete!");
    //
    //            C2DMManager.getInstance().setRegistrationID(registration_id);
    //        }
    //    }

    @Override
    public void onError() {
        // TODO Auto-generated method stub

    }
    @Override
    public void unRegistration() {
        // TODO Auto-generated method stub

    }
    @Override
    public void onPushReceive(Context context, Intent intent) {
        c2dm_msg = intent.getStringExtra("msg");
        Log.d(TAG, "===================================================================");
        Log.v("C2DM", "C2DM Message : " + c2dm_msg);
        Log.d(TAG, "===================================================================");

    }
}
