package net.sarangnamu.testc2dm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 *
 */
public class C2DMManager {
    private static final String TAG = "C2DMManager";

    private SharedPreferences shrdPref      = null;

    private static String authToken        = null;
    private static String registrationID   = null;
    private static C2DMManager singleton   = null;

    private static final String C2DM_APP              = "app";
    private static final String C2DM_SENDER           = "sender";
    private static final String C2DM_AUTH_TOKEN       = "c2dmAuthToken";
    private static final String C2DM_REGISTRATION_ID  = "c2dmRegistrationID";
    private static final String C2DM_INTENT_REGISTER  = "com.google.android.c2dm.intent.REGISTER";
    private static final String C2DM_SEND_URL         = "https://android.apis.google.com/c2dm/send";
    private static final String C2DM_CLIENT_LOGIN_URL = "https://www.google.com/accounts/ClientLogin";
    private static final String TEXT_ENCODING         = "UTF-8";

    private C2DMManager() {

    }

    public static C2DMManager getInstance() {
        if (singleton == null) {
            singleton = new C2DMManager();
        }

        return singleton;
    }

    /**
     * C2DM 연결을 위한 초기화
     *
     * @param context
     * @param sender
     * @param passwd
     */
    public void initRegistrationID(final Context context, final String sender) {
        if (context == null) {
            Log.e(TAG, "===================================================================");
            Log.e(TAG, "ERROR init");
            Log.e(TAG, "===================================================================");
            return ;
        }

        try {
            if (shrdPref == null) {
                shrdPref = PreferenceManager.getDefaultSharedPreferences(context);
            }

            registrationID = shrdPref.getString(C2DM_REGISTRATION_ID, null);

            if (registrationID == null) {
                Intent registrationIntent = new Intent(C2DM_INTENT_REGISTER);

                // Application ID(Package Name)
                registrationIntent.putExtra(C2DM_APP, PendingIntent.getBroadcast(context, 0, new Intent(), 0));

                // Developer ID
                registrationIntent.putExtra(C2DM_SENDER, sender);

                // Start request.
                context.startService(registrationIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initAuthToken(final Context context, final String sender, final String passwd) {
        if (context == null) {
            Log.e(TAG, "===================================================================");
            Log.e(TAG, "ERROR init");
            Log.e(TAG, "===================================================================");
            return ;
        }

        try {
            if (shrdPref == null) {
                shrdPref = PreferenceManager.getDefaultSharedPreferences(context);
            }

            if (authToken == null) {
                StringBuffer postDataBuilder = new StringBuffer();

                postDataBuilder.append("accountType=HOSTED_OR_GOOGLE")
                .append("&Email=" + sender)
                .append("&Passwd=" + passwd)
                .append("&service=ac2dm")
                .append("&source=none");

                //                byte[] postData = postDataBuilder.toString().getBytes(TEXT_ENCODING);
                //
                //                URL url = new URL(C2DM_CLIENT_LOGIN_URL);
                //                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //
                //                conn.setDoOutput(true);
                //                conn.setUseCaches(false);
                //                conn.setRequestMethod("POST");
                //                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                //                conn.setRequestProperty("Content-Length", Integer.toString(postData.length));
                //
                //                // 출력스트림을 생성하여 서버로 송신
                //                OutputStream out = conn.getOutputStream();
                //                out.write(postData);
                //                out.close();

                // 서버로부터 수신받은 스트림 객체를 버퍼에 넣어 읽는다.
                BufferedReader br = request(C2DM_CLIENT_LOGIN_URL, postDataBuilder.toString());

                String sIdLine = br.readLine();
                String lsIdLine = br.readLine();
                String authLine = br.readLine();

                authToken = authLine.substring(5, authLine.length());

                SharedPreferences.Editor editor = shrdPref.edit();
                editor.putString(C2DM_AUTH_TOKEN, authToken);
                editor.commit();
            }

            if (Option.DEBUG_C2DM_MANAGER) {
                Log.d(TAG, "===================================================================");
                Log.d(TAG, "AuthToken : " + authToken);
                Log.d(TAG, "===================================================================");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BufferedReader request(final String serverUrl, final String data) throws IOException {
        URL url = new URL(serverUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", Integer.toString(data.length()));

        // 출력스트림을 생성하여 서버로 송신
        OutputStream out = conn.getOutputStream();
        out.write(data.getBytes(TEXT_ENCODING));
        out.close();

        // 서버로부터 수신받은 스트림 객체를 버퍼에 넣어 읽는다.
        return new BufferedReader(new InputStreamReader(conn.getInputStream()));
    }

    /**
     * 메시지 전송을 시도 한다.
     *
     * @param msg
     * @return
     */
    public boolean send(final String regID, final String msg) {
        if (regID == null || authToken == null) {
            Log.e(TAG, "===================================================================");
            Log.e(TAG, "registrationID or authToken is null");
            Log.e(TAG, "===================================================================");
            return false;
        }

        if (msg == null || (msg != null && msg.length() == 0)) {
            Log.e(TAG, "===================================================================");
            Log.e(TAG, "message : " + msg);
            Log.e(TAG, "===================================================================");
            return false;
        }

        if (Option.DEBUG_C2DM_MANAGER) {
            Log.d(TAG, "===================================================================");
            Log.d(TAG, "regID : " + regID);
            Log.d(TAG, "message : " + msg);
            Log.d(TAG, "===================================================================");
        }

        try {
            // collapse_key는 C2DM에서 사용자가 SEND 버튼을 실수로 여러번 눌렀을때
            // 이전 메세지 내용과 비교해서 반복전송되는 것을 막기 위해서 사용된다.
            // 여기서는 반복전송도 허용되게끔 매번 collapse_key를 랜덤함수로 뽑는다.
            String collaspe_key = String.valueOf(Math.random() % 100 + 1);

            // 보낼 메세지 조립
            StringBuffer postDataBuilder = new StringBuffer();

            postDataBuilder.append("registration_id=" + regID)
            .append("&collapse_key=" + collaspe_key) // 중복방지 필터
            .append("&delay_while_idle=1")
            .append("&" + msg); // 메세지내용

            Log.d(TAG, "===================================================================");
            Log.d(TAG, "postDataBuilder " + postDataBuilder);
            Log.d(TAG, "===================================================================");

            // 조립된 메세지를 Byte배열로 인코딩
            byte[] postData = postDataBuilder.toString().getBytes(TEXT_ENCODING);

            // HTTP 프로토콜로 통신한다.
            // 먼저 해당 url 커넥션을 선언하고 연다.
            URL url = new URL(C2DM_SEND_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true); // 출력설정
            conn.setUseCaches(false);
            conn.setRequestMethod("POST"); // POST 방식
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", Integer.toString(postData.length));
            conn.setRequestProperty("Authorization", "GoogleLogin auth=" + authToken);

            // 출력스트림을 생성하여 postData를 기록.
            OutputStream out = conn.getOutputStream();

            // 출력(송신)후 출력스트림 종료
            out.write(postData);
            out.close();

            // 소켓의 입력스트림을 반환
            conn.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void setRegistrationID(final String regID) {
        if (regID == null) {
            return ;
        }

        if (Option.DEBUG_C2DM_MANAGER) {
            Log.d(TAG, "===================================================================");
            Log.d(TAG, "registration id : " + regID);
            Log.d(TAG, "===================================================================");
        }

        SharedPreferences.Editor editor = shrdPref.edit();
        editor.putString(C2DM_REGISTRATION_ID, regID);
        editor.commit();

        registrationID = regID;
    }

    public String getRegistrationID() {
        return registrationID;
    }
}
