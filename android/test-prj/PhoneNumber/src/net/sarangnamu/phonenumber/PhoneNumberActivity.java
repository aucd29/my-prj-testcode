package net.sarangnamu.phonenumber;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

public class PhoneNumberActivity extends Activity {
    private TextView hello;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        hello = (TextView) findViewById(R.id.hello);
        hello.setText(getPhoneNumber(this));
    }

    public static String getPhoneNumber(Context context)
    {
        TelephonyManager tMgr =(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String phone = "";
        try{
            if(tMgr.getLine1Number()!=null){
                phone = tMgr.getLine1Number();
            }
            //            phone = phone.substring(phone.length()-10,phone.length());
            //            phone="0"+phone;
        }catch(Exception e){
            e.printStackTrace();
        }
        Log.d("Phone Number ", phone);
        return phone;
    }
}