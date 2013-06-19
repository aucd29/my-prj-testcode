package net.sarangnamu.testexception;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.unzip.UnzipUtil;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

class TestActivity extends Activity {
    private static final String TAG = "TestActivity";
    //    public WebViewEx webview;
    public WebView webview;

    protected static final int MENU_GROUP_ADD = 1;
    protected static final int MENU_ADD       = Menu.FIRST + 1;

    public final static int API_WIDGET                   = 0x1;
    public final static int API_STORAGE                  = 0x2;
    public final static int API_DEVICEAPIS               = 0x4;
    public final static int API_ACCELEROMETER            = 0x8;
    public final static int API_DEVICEINTERATIONMANAGER  = 0x10;
    public final static int API_DEVICECAMERMANAGER       = 0x20;
    public final static int API_DEVICESTATUSMANAGER      = 0x40;
    public final static int API_FILESYSTEMMANAGER        = 0x80;
    public final static int API_MESSAGING                = 0x100;
    public final static int API_GEOLOCATION              = 0x200;
    public final static int API_TELEPHONY                = 0x400;
    public final static int API_AUTHPROVIDER             = 0x800;
    public final static int API_GETCONTACTINFO           = 0x1000;
    public final static int API_EXTENSION                = 0x2000;
    public final static int API_CACHE                    = 0x4000;
    public final static int API_KEYSOUND                 = 0x8000;
    public final static int API_ALL                      =   API_WIDGET | API_STORAGE | API_DEVICEAPIS
            | API_ACCELEROMETER | API_DEVICEINTERATIONMANAGER | API_DEVICECAMERMANAGER
            | API_DEVICESTATUSMANAGER | API_FILESYSTEMMANAGER | API_MESSAGING
            | API_GEOLOCATION | API_TELEPHONY | API_AUTHPROVIDER
            | API_GETCONTACTINFO | API_EXTENSION | API_CACHE | API_KEYSOUND;

    //    private ImageView img[];
    private ImageView img[] = new ImageView[15];
    private ImageView cameraImage;
    private LinearLayout linLayout;

    // cameraTest
    private Uri outputFileUri = null;
    private static final int CAMERA_CAPTURE = 99;
    private static final int CAMERA_TYPE = 0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        try {
            sendMessage(1, null);
            sendMessage(2, null);
            sendMessage(3, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case 1:
                Log.d(TAG, "===================================================================");
                Log.d(TAG, "1");
                Log.d(TAG, "===================================================================");
                break;

            case 2:
                try {
                    Thread.sleep(10000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "===================================================================");
                Log.d(TAG, "2");
                Log.d(TAG, "===================================================================");
                break;

            case 3:
                Log.d(TAG, "===================================================================");
                Log.d(TAG, "3");
                Log.d(TAG, "===================================================================");
                break;
            }
        }
    };

    private void sendMessage(int type, Object obj) {
        Message msg = handler.obtainMessage();
        msg.what = type;
        msg.obj  = obj;
        handler.sendMessage(msg);
    }

    private void setNewItem() {
        boolean newItem = false;

        // date sample : 2012-08-14 08:54:08
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = formatter.parse("2012-09-11 08:54:08");
            long item = date.getTime() / 1000;
            Calendar ca = Calendar.getInstance();
            ca.setTime(date);
            long current = Calendar.getInstance().getTimeInMillis() / 1000;
            long gap = current - item;

            if (gap <= 86400) {
                newItem = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void email() {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND, Uri.fromParts("mailto", "aucd29@gmail.com", null));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {"Burke.Choi@obigo.com"});
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "subject!!!!!");
        //intent.putExtra(android.content.Intent.EXTRA_CC, "Burke.Choi@obigo.com");
        intent.setType("text/html");
        intent.putExtra("exit_on_sent", false);
        intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("Hello <b>World</b>"));
        //

        //intent.putExtra(android.content.Intent.EXTRA_TEXT, "Hello <b>World</b>");

        startActivityForResult(Intent.createChooser(intent, "Send email using"), 112);


        //        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "Burke.Choi@obigo.com", null));
        //        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //        intent.putExtra(Intent.EXTRA_EMAIL, "aucd29@gmail.com");
        //        intent.putExtra(Intent.EXTRA_SUBJECT, "subject!!!!!");
        //        //intent.putExtra(Intent.EXTRA_CC, "Burke.Choi@obigo.com");
        //        intent.setType("text/html");
        //        intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("Hello <b>World</b>"));
        //
        //        startActivity(Intent.createChooser(intent, "Send email using..."));
    }

    /**
     * The toast pops up a quick message to the user showing what could be
     * the text of an incoming message.  It uses a custom view to do so.
     */
    protected void showToast() {
        // create the view
        View view = inflateView(R.layout.custom_notification_alert_dialog);

        // set the text in the view
        TextView tv = (TextView)view.findViewById(R.id.txtMessage);
        tv.setText("khtx. meet u for dinner. cul8r");

        // show the toast
        Toast toast = new Toast(this);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    private View inflateView(int resource) {
        LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return vi.inflate(resource, null);
    }

    private final int BUFF_SIZE = 4096;

    public void getFileSize() throws ZipException {
        ZipFile zipFile = new ZipFile("/sdcard/zip/test_file.zip");
        List fileHeaderList = zipFile.getFileHeaders();

        double size = 0.0f;
        for (int i = 0; i < fileHeaderList.size(); i++) {
            FileHeader fileHeader = (FileHeader)fileHeaderList.get(i);
            size += fileHeader.getUncompressedSize();
        }

        Log.d(TAG, "size : " + (size/1024.0f/1024.0f));
    }

    public void test() {
        Resources rs = null; // = context.createPackageContext(packageName, 0).getResources();
        AssetManager am = rs.getAssets();
    }

    public void ExtractAll() {
        ZipInputStream is = null;
        OutputStream os = null;

        try {
            // Initiate the ZipFile
            ZipFile zipFile = new ZipFile("/sdcard/zip/test_file2.zip");
            zipFile.setFileNameCharset("EUC-KR");
            String destinationPath = "/sdcard/zip/unzip";
            ProgressMonitor pm = zipFile.getProgressMonitor();

            // If zip file is password protected then set the password
            if (zipFile.isEncrypted()) {
                zipFile.setPassword("password");
            }

            //Get a list of FileHeader. FileHeader is the header information for all the
            //files in the ZipFile
            List fileHeaderList = zipFile.getFileHeaders();

            // Loop through all the fileHeaders
            for (int i = 0; i < fileHeaderList.size(); i++) {
                FileHeader fileHeader = (FileHeader)fileHeaderList.get(i);

                if (fileHeader != null) {
                    //Build the output file
                    String outFilePath = destinationPath + System.getProperty("file.separator") + fileHeader.getFileName();
                    File outFile = new File(outFilePath);

                    //Checks if the file is a directory
                    if (fileHeader.isDirectory() || outFile.isDirectory()) {
                        //This functionality is up to your requirements
                        //For now I create the directory
                        outFile.mkdirs();
                        continue;
                    }

                    //Check if the directories(including parent directories)
                    //in the output file path exists
                    File parentDir = outFile.getParentFile();
                    if (!parentDir.exists()) {
                        parentDir.mkdirs();
                    }

                    //Get the InputStream from the ZipFile
                    is = zipFile.getInputStream(fileHeader);
                    //Initialize the output stream
                    os = new FileOutputStream(outFile);

                    int readLen = -1;
                    byte[] buff = new byte[BUFF_SIZE];

                    //Loop until End of File and write the contents to the output stream
                    while ((readLen = is.read(buff)) != -1) {
                        os.write(buff, 0, readLen);

                        //Log.d(TAG, "res "+ pm.getResult() + ", st " + pm.getState());
                        Log.d(TAG, "getCurrentOperation " + pm.getCurrentOperation()) ;
                        Log.d(TAG, "getPercentDone " + pm.getPercentDone());
                        Log.d(TAG, "getWorkCompleted " + pm.getWorkCompleted());


                    }

                    //Please have a look into this method for some important comments
                    closeFileHandlers(is, os);

                    //To restore File attributes (ex: last modified file time,
                    //read only flag, etc) of the extracted file, a utility class
                    //can be used as shown below
                    UnzipUtil.applyFileAttributes(fileHeader, outFile);

                    System.out.println("Done extracting: " + fileHeader.getFileName());
                } else {
                    System.err.println("fileheader is null. Shouldn't be here");
                }
            }
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                closeFileHandlers(is, os);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeFileHandlers(ZipInputStream is, OutputStream os) throws IOException{
        //Close output stream
        if (os != null) {
            os.close();
            os = null;
        }

        //Closing inputstream also checks for CRC of the the just extracted file.
        //If CRC check has to be skipped (for ex: to cancel the unzip operation, etc)
        //use method is.close(boolean skipCRCCheck) and set the flag,
        //skipCRCCheck to false
        //NOTE: It is recommended to close outputStream first because Zip4j throws
        //an exception if CRC check fails
        if (is != null) {
            is.close();
            is = null;
        }
    }

    public void passwordZip2() {
        try {
            ZipFile zipFile = new ZipFile("/sdcard/zip/test_file.zip");

            if (zipFile.isEncrypted()) {
                zipFile.setPassword("test");
            }
            // Extracts all files to the path specified
            zipFile.extractAll("/sdcard/zip/unzip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testDAP() {
        boolean res = isAccelermeterFeatures();
        Log.d(TAG, "===================================================================");
        Log.d(TAG, "res " + res);
        Log.d(TAG, "===================================================================");
    }

    public boolean isAccelermeterFeatures() {
        int features = 0;
        features |= API_STORAGE;
        features |= API_MESSAGING;

        switch (features & API_ACCELEROMETER) {
        case API_ACCELEROMETER:
            return true;

        default:
            return false;
        }
    }

    public void testResizeArrayList() {
        ArrayList<Integer> test = new ArrayList<Integer>();
        for (int i=0; i<32; ++i) {
            test.add(i);
        }

        int cellValue = 24;
        Log.d(TAG, "===================================================================");
        Log.d(TAG, "cellValue " + cellValue);
        Log.d(TAG, "===================================================================");
        if (test.size() > cellValue) {
            for (int i=test.size() - 1; i>=cellValue; --i) {
                test.remove(i);
            }
        }

        Log.d(TAG, "===================================================================");
        Log.d(TAG, "test size : " + test.size());
        for (Integer val : test) {
            Log.d(TAG, "val : " + val);
        }
        Log.d(TAG, "===================================================================");

    }

    public void splitTest() {
        String data = "test.test.test1|com.sara.test2|com.ddd.test3";

        Log.d(TAG, "===================================================================");

        String datas[] = data.split("\\|");
        for (String pkg : datas) {
            Log.d(TAG, "pkgs " + pkg);
        }

        Log.d(TAG, "===================================================================");
        StringTokenizer stk = new StringTokenizer(data, "|");
        for (int i=0; i<stk.countTokens(); ++i) {
            Log.d(TAG, "pkg " + stk.nextToken());
        }
        Log.d(TAG, "pkg " + stk.nextToken());
        Log.d(TAG, "===================================================================");
    }

    public void getRunningTask() {
        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskInfo = am.getRunningTasks(50);
        List<RunningAppProcessInfo> processInfo = am.getRunningAppProcesses();
        MemoryInfo minfo = new ActivityManager.MemoryInfo();

        long memoryFirst = minfo.availMem;

        Log.d(TAG, "===================================================================");
        Log.d(TAG, "Memory info " + minfo.availMem);
        Log.d(TAG, "===================================================================");

        Log.d(TAG, "===================================================================");
        Log.d(TAG, "RUNNING TASK INFO");
        Log.d(TAG, "===================================================================");
        final int tsize = taskInfo.size();
        for (int i=1; i<tsize; ++i) {
            ComponentName topActivity = taskInfo.get(i).topActivity;
            int pid = taskInfo.get(i).id;
            Log.d(TAG, "pid " + pid);
            //Process.killProcess(pid);
            am.killBackgroundProcesses(topActivity.getPackageName());
            Log.d(TAG, "Running task [" +  i + "] " + topActivity.getPackageName());
        }
        Log.d(TAG, "===================================================================");

        Log.d(TAG, "===================================================================");
        Log.d(TAG, "RUNNING PROCESS INFO");
        Log.d(TAG, "===================================================================");
        final int psize = processInfo.size();
        for (int i=0; i<psize; ++i) {
            Log.d(TAG, "Running process [" +  i + "] " + processInfo.get(i).processName);
        }
        Log.d(TAG, "===================================================================");

        Log.d(TAG, "===================================================================");
        Log.d(TAG, "Memory info " + minfo.availMem + ", (" + (memoryFirst - minfo.availMem) + ")");
        Log.d(TAG, "===================================================================");

    }

    class AutoViewDialogManager {
        private static final String TAG = "AutoViewDialogManager";

        private static final int REQ_SHOW = 0;
        private static final int REQ_HIDE = 1;

        private static final int DELAY_SHOW = 3000;
        private static final int DELAY_HIDE = 1000;
        //        private static AutoViewDialogManager singleton = null;

        private ArrayList<String> msgList = new ArrayList<String>();
        private AlertDialog dlg = null;
        private Context context = null;
        private Boolean started = false;

        public Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what) {
                case REQ_HIDE:
                    closeDialog();
                    break;
                case REQ_SHOW:
                    showDialog();
                    break;

                default:break;
                }
            }
        };

        //        public static AutoViewDialogManager getInstance() {
        //            if (singleton == null) {
        //                singleton = new AutoViewDialogManager();
        //            }
        //
        //            return singleton;
        //        }
        //
        //        private AutoViewDialogManager() {
        //
        //        }

        public void addMessage(Context context, String str) {
            synchronized (msgList) {
                msgList.add(str);

                if (!started) {
                    Log.d(TAG, "===================================================================");
                    Log.d(TAG, "start !!");
                    Log.d(TAG, "===================================================================");
                    started = true;
                    this.context = context;
                    handler.sendEmptyMessage(REQ_SHOW);
                }
            }
        }

        private void showDialog() {
            if (context == null) {
                return ;
            }

            if (msgList.get(0) == null) {
                return ;
            }

            dlg = new AlertDialog.Builder(context).create();
            dlg.setMessage(msgList.get(0));
            dlg.setCancelable(false);
            dlg.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(DELAY_SHOW);
                        handler.sendEmptyMessage(REQ_HIDE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        private void closeDialog() {
            synchronized (msgList) {
                dlg.dismiss();
                msgList.remove(0);

                if (msgList.size() > 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(DELAY_HIDE);
                                handler.sendEmptyMessage(REQ_SHOW);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    started = false;
                    Log.d(TAG, "===================================================================");
                    Log.d(TAG, "end!!!");
                    Log.d(TAG, "===================================================================");
                }
            }
        }
    }

    AutoViewDialogManager dlgManager = new AutoViewDialogManager();

    private void testDialog() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dlgManager.addMessage(TestActivity.this, "hello1");
                    dlgManager.addMessage(TestActivity.this, "hello2");
                    dlgManager.addMessage(TestActivity.this, "hello3");
                    Thread.sleep(1000);

                    dlgManager.addMessage(TestActivity.this, "hello4");
                    Thread.sleep(10000);

                    dlgManager.addMessage(TestActivity.this, "hello5");
                    Thread.sleep(15000);

                    dlgManager.addMessage(TestActivity.this, "hello6");
                    dlgManager.addMessage(TestActivity.this, "hello7");
                    dlgManager.addMessage(TestActivity.this, "hello8");
                    dlgManager.addMessage(TestActivity.this, "hello9");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private String getRandomFileName() {
        Log.d(TAG, "" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        Log.d(TAG, "" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath());

        Calendar cal = Calendar.getInstance();
        StringBuilder res = new StringBuilder();
        String millis = "" + cal.getTimeInMillis();

        res.append("IMG_");
        res.append(cal.get(Calendar.YEAR));
        res.append(String.format("%02d", cal.get(Calendar.MONTH) + 1));
        res.append(String.format("%02d", cal.get(Calendar.DATE)));
        res.append("_");
        res.append(String.format("%02d", cal.get(Calendar.HOUR_OF_DAY)));
        res.append(String.format("%02d", cal.get(Calendar.MINUTE)));
        res.append(String.format("%02d", cal.get(Calendar.SECOND)));
        res.append(".jpg");

        Log.d(TAG, "===================================================================");
        Log.d(TAG, res.toString());
        Log.d(TAG, "===================================================================");

        return res.toString();
    }

    private void setWebview() {
        webview = new WebView(this);
        webview.loadUrl("http://daum.net");
        setContentView(webview);
    }

    private void classForName() {
        try {
            long calID = 3;
            long startMillis = 0;
            long endMillis = 0;
            Calendar beginTime = Calendar.getInstance();
            beginTime.set(2012, 9, 14, 7, 30);
            startMillis = beginTime.getTimeInMillis();
            Calendar endTime = Calendar.getInstance();
            endTime.set(2012, 9, 14, 8, 45);
            endMillis = endTime.getTimeInMillis();

            //            ContentResolver cr = getContentResolver();
            //            ContentValues values = new ContentValues();
            //            values.put(Events.DTSTART, startMillis);
            //            values.put(Events.DTEND, endMillis);
            //            values.put(Events.TITLE, "Jazzercise");
            //            values.put(Events.DESCRIPTION, "Group workout");
            //            values.put(Events.CALENDAR_ID, calID);
            //            values.put(Events.EVENT_TIMEZONE, "America/Los_Angeles");
            //            Uri uri = cr.insert(Events.CONTENT_URI, values);

            // get the event ID that is the last element in the Uri
            //            long eventID = Long.parseLong(uri.getLastPathSegment());

            Log.d(TAG, "===================================================================");
            Log.d(TAG, "classForName");
            Log.d(TAG, "===================================================================");

            //W/System.err(10066):
            //
            // java.lang.NoSuchMethodException:
            // query [class android.content.ContentResolver,
            // class java.lang.String,
            // long,
            // long,
            // class java.lang.String]
            //
            //public static final Cursor query(ContentResolver cr, String[] projection, long begin, long end, String searchQuery) {

            String CALENDER_CONTRACT_INSTANCES = "android.provider.CalendarContract$Instances";
            Class<?> cls = Class.forName(CALENDER_CONTRACT_INSTANCES);
            Field f = cls.getField("CONTENT_URI");

            Log.d(TAG, "===================================================================");
            Log.d(TAG, "title : " + cls.getField("TITLE").get(null).toString());
            Log.d(TAG, "type ; " + f.getType());
            Log.d(TAG, "generic type ; " + f.getGenericType());
            Log.d(TAG, "===================================================================");
            //            Class[] args = new Class[] {ContentResolver.class, String[].class, long.class, long.class, String.class};
            //            Method md = Class.forName(CALENDER_CONTRACT_INSTANCES).getMethod("query", args);

            //            Object[] params = new Object[] {cr, projection, begin, end, null};
            //            Method md = Class.forName(CALENDER_CONTRACT_INSTANCES).getMethod("query", args);
            //            md.invoke(Class.forName(CALENDER_CONTRACT_INSTANCES).getInterfaces(), params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void androidVersion() {
        Log.d(TAG, "===================================================================");
        Log.d(TAG, "Build.VERSION.RELEASE " + Build.VERSION.RELEASE);
        Log.d(TAG, "===================================================================");
    }

    private void testCalendarAndDate() {
        Calendar cal = Calendar.getInstance();

        int month = cal.get(Calendar.MONTH);
        int year  = cal.get(Calendar.YEAR);
        int date;
        date = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        cal.clear();
        cal.set(year, month, date, 23, 59, 59);

        long end = cal.getTimeInMillis();

        Log.d(TAG, "===================================================================");
        Log.d(TAG, "" + end);
        Log.d(TAG, "===================================================================");

        cal.clear();
        cal.set(year, month, 1);

        long start = cal.getTimeInMillis();

        Log.d(TAG, "===================================================================");
        Log.d(TAG, "" + start);
        Log.d(TAG, "===================================================================");

        cal.clear();
        cal.set(2011, 8, 3);

        Log.d(TAG, "===================================================================");
        Log.d(TAG, "" + cal.getTimeInMillis());
        Log.d(TAG, "===================================================================");

        Log.d(TAG, "===================================================================");
        Log.d(TAG, "" + new Date(2011-1900, 8, 3).getTime());
        Log.d(TAG, "===================================================================");
    }

    private void cameraTest() {
        cameraImage = new ImageView(getBaseContext());
        linLayout = new LinearLayout(this);
        setContentView(linLayout);


        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        if (CAMERA_TYPE == 0) {
            File file = new File(Environment.getExternalStorageDirectory(), "test.jpg");
            outputFileUri = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        }

        startActivityForResult(intent, CAMERA_CAPTURE);
    }

    private Bitmap getCameraBitmap() {
        if (outputFileUri == null) {
            return null;
        }

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(outputFileUri.getPath(), o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true)
        {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) {
                break;
            }

            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(outputFileUri.getPath(), o2);



        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE) {

            Log.d(TAG, "===================================================================");
            Log.d(TAG, "resultCode : " + resultCode);
            Log.d(TAG, "===================================================================");

            switch (CAMERA_TYPE) {
            case 0:
                // 직접 지정해서 받는 방법
                if (outputFileUri != null) {
                    Log.d(TAG, "===================================================================");
                    Log.d(TAG, "outputFileUri.getPath() " + outputFileUri.getPath());
                    Log.d(TAG, "===================================================================");

                    cameraImage.setScaleType(ScaleType.CENTER);
                    cameraImage.setImageDrawable(new BitmapDrawable(getCameraBitmap()));
                    linLayout.addView(cameraImage, new LinearLayout.LayoutParams(
                            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
                            ));
                }
                break;

            case 1:
                // thumbnail 받는 방법
                Bitmap bm = (Bitmap) data.getExtras().get("data");
                Log.d(TAG, "===================================================================");
                Log.d(TAG, "x " + bm.getWidth() + " y " + bm.getHeight());
                Log.d(TAG, "===================================================================");
                cameraImage.setScaleType(ScaleType.CENTER);
                cameraImage.setImageDrawable(new BitmapDrawable(getResources(), bm));
                linLayout.addView(cameraImage, new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
                        ));
                break;

            case 2:
                Log.d(TAG, "===================================================================");
                Log.d(TAG, "data.getData() " + data.getData());
                Log.d(TAG, "data.getExtras().get(data) " + data.getExtras().get("data"));
                Log.d(TAG, "===================================================================");


                //                Uri uri = (Uri)data.getExtras().get("data");
                //                Cursor cr = getContentResolver().query(uri, null,null,null,null);
                //                String absPath = null;
                //                if (cr.moveToFirst()) {
                //                    absPath = cr.getString(cr.getColumnIndex(MediaStore.MediaColumns.DATA));
                //                }

                //                BitmapFactory.Options options = new BitmapFactory.Options();
                //                options.inSampleSize = 1;
                //
                //                Bitmap orgImage = BitmapFactory.decodeFile(outputFileUri.getPath(), options);
                //
                //                Log.d(TAG, "===================================================================");
                //                Log.d(TAG, "x " + orgImage.getWidth() + " y " + orgImage.getHeight());
                //                Log.d(TAG, "===================================================================");
                //
                //                cameraImage.setScaleType(ScaleType.CENTER);
                //                cameraImage.setImageDrawable(new BitmapDrawable(orgImage));
                //                linLayout.addView(cameraImage, new LinearLayout.LayoutParams(
                //                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
                //                        ));
                break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public Bitmap resizeBitmapImage(Bitmap source, int maxResolution) {
        int width = source.getWidth();
        int height = source.getHeight();
        int newWidth = width;
        int newHeight = height;
        float rate = 0.0f;

        if (width > height) {
            if (maxResolution < width) {
                rate = maxResolution / (float) width;
                newHeight = (int) (height * rate);
                newWidth = maxResolution;
            }
        } else {
            if (maxResolution < height) {
                rate = maxResolution / (float) height;
                newWidth = (int) (width * rate);
                newHeight = maxResolution;
            }
        }

        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }

    private void receiverTest() {
        Intent intent = new Intent("net.sarangnamu.testpreference.Receiver");
        intent.putExtra("type", 1);
        intent.putExtra("data", "show me the money");
        intent.putExtra("key", "AR8kz5GN5Uy2");

        Log.d(TAG, "===================================================================");
        Log.d(TAG, "receiverTest");
        Log.d(TAG, "===================================================================");
        sendBroadcast(intent);
    }

    public static String encryptBlowfish(String to_encrypt, String strkey) {
        try {
            SecretKeySpec key = new SecretKeySpec(strkey.getBytes(), "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return new String(cipher.doFinal(to_encrypt.getBytes()));
        } catch (Exception e) { return null; }
    }

    private static final String APPBY_NAME_MAPS = "appby_name_maps";
    private static final String API_PREFERENCES = "api.preferences.l11";

    private void preferenceNameManager(final String key) {
        SharedPreferences spRefresh = getBaseContext().getSharedPreferences(API_PREFERENCES, Context.MODE_PRIVATE);
        String maps = spRefresh.getString(APPBY_NAME_MAPS, "");

        try {
            if (maps.length() != 0) {
                String[] names = maps.split("\\|");
                for (String name : names) {
                    if (name.equals(key)) {
                        // 동일한 이름이 존재하면 return
                        return ;
                    }
                }

                // 아니면 저장
                maps += "|" + key;
            } else {
                maps += key;
            }

            SharedPreferences.Editor editor = spRefresh.edit();
            editor.putString(APPBY_NAME_MAPS, maps);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetPreferenceName() {
        SharedPreferences spRefresh = getBaseContext().getSharedPreferences(API_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spRefresh.edit();
        editor.putString(APPBY_NAME_MAPS, "");
        editor.commit();
    }

    private void downloadCacheDir() {
        Log.d(TAG, "===================================================================");
        Log.d(TAG, "getDownloadCacheDirectory " + Environment.getExternalStorageDirectory ().getAbsolutePath());
        Log.d(TAG, "getDownloadCacheDirectory " + Environment.getDownloadCacheDirectory().getAbsolutePath());
        Log.d(TAG, "===================================================================");
    }

    private void installedApps() {
        //        PackageManager pm = this. getPackageManager ();
        //        List <ApplicationInfo> list = pm.getInstalledApplications (0);
        //        for (ApplicationInfo ai : list) {
        //            //            Log.i ("", ai.packageName + " " + ai.describeContents());
        //            Log.d("", pm.getApplicationLabel(ai).toString());
        //        }


        final PackageManager manager = getPackageManager();
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> apps = new ArrayList<ResolveInfo>();
        apps = manager.queryIntentActivities(mainIntent, 0);
        if (apps.size() == 0) {
            Log.e("Applauncher BONDI API", "There's no application available.");
            return;
        }

        HashMap<String, String> mAppList = new HashMap<String, String>();
        HashMap<String, String> mPackageList = new HashMap<String, String>();

        ResolveInfo info;
        String intentDetail = null;
        for (int i = 0; i < apps.size(); i++) {
            info = apps.get(i);
            ActivityInfo activity = info.activityInfo;
            ApplicationInfo appinfo = activity.applicationInfo;
            intentDetail = appinfo.packageName + ":" + activity.name;
            CharSequence c = null;

            Log.d("", "intentDetail " + intentDetail);

            //            try {
            //                c = manager.getApplicationLabel(manager.getApplicationInfo(appinfo.processName, PackageManager.GET_META_DATA));
            //            } catch (NameNotFoundException e) {
            //                e.printStackTrace();
            //            }

            String labelStr;
            labelStr = activity.name;

            mAppList.put(labelStr, intentDetail);
            mPackageList.put(appinfo.packageName, labelStr);
        }

        Log.d("", "mAppList " + mAppList.size());
        Log.d("", "mPackageList " + mPackageList.size());
    }


    private void setAppIcon() {
        //        setContentView(R.layout.main);
        //      Drawable icon = getAppsIcon("com.obigo.webby");
        //        if (icon == null) {
        //            Log.e("", "error");
        //
        //            return ;
        //        }

        //        img[0] = (ImageView)findViewById(R.id.imageView1);
        //        img[1] = (ImageView)findViewById(R.id.imageView2);
        //        img[2] = (ImageView)findViewById(R.id.imageView3);
        //        img[3] = (ImageView)findViewById(R.id.imageView4);
        //        img[4] = (ImageView)findViewById(R.id.imageView5);
        //        img[5] = (ImageView)findViewById(R.id.imageView6);
        //        img[6] = (ImageView)findViewById(R.id.imageView7);
        //        img[7] = (ImageView)findViewById(R.id.imageView8);
        //        img[8] = (ImageView)findViewById(R.id.imageView9);
        //        img[9] = (ImageView)findViewById(R.id.imageView10);
        //        img[10] = (ImageView)findViewById(R.id.imageView11);
        //        img[11] = (ImageView)findViewById(R.id.imageView12);
        //        img[12] = (ImageView)findViewById(R.id.imageView13);
        //        img[13] = (ImageView)findViewById(R.id.imageView14);
        //        img[14] = (ImageView)findViewById(R.id.imageView15);

        //        for (ImageView iv : img) {
        //            iv.setImageDrawable(getAppsIcon("net.sarangnamu.testexception"));
        //        }

        //        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        //        for(int i=0;i<15;i++) {
        //            PackageInfo p = packs.get(i);
        //            //            Log.d("", "count : " + i);
        //            //            if (p.packageName.equals(packageName)) {
        //            //                return p.applicationInfo.loadIcon(getPackageManager());
        //            //            }
        //            img[i].setImageDrawable(getAppsIcon(p.packageName));
        //        }
        //
        //        Log.d("", "count : " + packs.size());

        //        for (int i=0; i<img.length; ++i) {
        //            //            Log.d("", "count : " + i);
        //            img[i].setImageDrawable(getAppsIcon("net.sarangnamu.testexception"));
        //        }

        //        img.setImageDrawable(icon);
        //        webview.setInitialScale(130);
        //        getFileCount();
    }

    private Drawable getAppsIcon(String packageName) {
        try {
            ApplicationInfo info = createPackageContext(packageName, 0).getApplicationInfo();

            if (info == null) {
                Log.d("", "############################################### info is null");
                return null;
            }

            Drawable icon = info.loadIcon(getPackageManager());
            if (icon == null) {
                Log.d("", "############################################### icon is null");
                return null;
            }

            return icon;

            //                        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
            //                        for(int i=0;i<packs.size();i++) {
            //                            PackageInfo p = packs.get(i);
            //
            //                            if (p.packageName.equals(packageName)) {
            //                                return p.applicationInfo.loadIcon(getPackageManager());
            //                            }
            //                        }
            //
            //                        return null;

            //            //            ComponentName component = new ComponentName(packageName, ".TestActivity");
            //            Log.d("", "component name : " + component.toString());
            //
            //            if (component == null) {
            //                Log.e("", "component is null");
            //            }
            //
            //            PackageManager packageManager = getPackageManager();
            //            ActivityInfo activityInfo = packageManager.getActivityInfo(component, 0);
            //
            //            return activityInfo.loadIcon(packageManager);
        } catch (Exception e) {
            e.printStackTrace();
            //            Log.e("", "Couldn't find ActivityInfo for selected application", e);
        }

        return null;
    }

    public static int fileCount = 0;

    public void getFileCount() {
        try {
            Resources rs = createPackageContext("com.single.view", 0).getResources();
            AssetManager am = rs.getAssets();

            getFileCount(am, "webbyview");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFileCount(AssetManager am, String path) throws IOException {
        String assets[] = am.list(path);
        for (String asset : assets) {
            try {
                InputStream is = am.open(path + "/" + asset);
                is.close();
                ++fileCount;
            } catch (Exception e) {
                getFileCount(am, path + "/" + asset);
            }
        }
    }

    public void checkPackageData() {
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);


        for (ApplicationInfo packageInfo : packages) {
            //            Log.d("APPLIST", packageInfo.processName);
            try {
                Resources rs = createPackageContext(packageInfo.processName, 0).getResources();
                AssetManager am = rs.getAssets();

                am.open("webbyview/config.xml");
                for (String list : am.list("webbyview")) {
                    Log.d("webbyview", list);
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }

    public void apiChecker() {
        try {

            Resources rs = createPackageContext("com.single.view", 0).getResources();
            AssetManager am = rs.getAssets();
            try {
                for (String list : am.list("/assets/")) {
                    Log.d("", list);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        int select = 0; //API_ALL, select2 = API_ALL;
        select |= API_WIDGET;
        select |= API_STORAGE;
        select |= API_EXTENSION;
        select |= API_CACHE;
        select |= API_KEYSOUND;
        select |= API_TELEPHONY;
        select |= API_AUTHPROVIDER;

        select |= API_DEVICEAPIS;
        select |= API_DEVICEINTERATIONMANAGER;
        select |= API_ACCELEROMETER;
        select |= API_DEVICESTATUSMANAGER;
        select |= API_GETCONTACTINFO;
        select |= API_MESSAGING;
        select |= API_GEOLOCATION;

        Log.d("", "value : " + select);
        //        select = 65375;
        int MASK = 0x0001;

        int i = -1;
        int mask = 0, mask2;
        while (++i < 31) {

            //        for (int i=0; select != 0; select = select ^ MASK << i, i++) {
            //            int mask = select & MASK << i;

            //            select2 = select2 ^ (MASK << i);
            mask = select & (MASK << i);
            //            mask2 = select2 & (MASK << i);
            //            Log.d("", "select : " + mask + " " + i);
            switch(mask) {
            case API_WIDGET:
                Log.d("", "API_WIDGET");
                break;
            case API_STORAGE:
                Log.d("", "API_STORAGE");
                break;
            case API_DEVICEAPIS:
                Log.d("", "API_DEVICEAPIS");
                break;
            case API_ACCELEROMETER:
                Log.d("", "API_ACCELEROMETER");
                break;
            case API_DEVICEINTERATIONMANAGER:
                Log.d("", "API_DEVICEINTERATIONMANAGER");
                break;
            case API_DEVICECAMERMANAGER:
                Log.d("", "API_DEVICECAMERMANAGER");
                break;
            case API_DEVICESTATUSMANAGER:
                Log.d("", "API_DEVICESTATUSMANAGER");
                break;
            case API_FILESYSTEMMANAGER:
                Log.d("", "API_FILESYSTEMMANAGER");
                break;
            case API_MESSAGING:
                Log.d("", "API_MESSAGING");
                break;
            case API_GEOLOCATION:
                Log.d("", "API_GEOLOCATION");
                break;
            case API_TELEPHONY:
                Log.d("", "API_TELEPHONY");
                break;
            case API_AUTHPROVIDER:
                Log.d("", "API_AUTHPROVIDER");
                break;
            case API_GETCONTACTINFO:
                Log.d("", "API_GETCONTACTINFO");
                break;
            case API_EXTENSION:
                Log.d("", "API_EXTENSION");
                break;
            case API_CACHE:
                Log.d("", "API_CACHE");
                break;
            case API_KEYSOUND:
                Log.d("", "API_KEYSOUND");
                break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Handle the back button
        //        if(keyCode == KeyEvent.KEYCODE_MENU) {
        //            Toast.makeText(this, "Do not found Webby", Toast.LENGTH_LONG).show();
        //
        //            return true;
        //        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("", "onCreateOptionsMenu===================================");

        boolean res = super.onCreateOptionsMenu(menu);

        menu.add(MENU_GROUP_ADD, MENU_ADD, 0, "add").setIcon(android.R.drawable.ic_menu_add).setAlphabeticShortcut('A');

        return res;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MENU_ADD:
            Toast.makeText(this, "test", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

class WebViewEx extends WebView {

    public WebViewEx(Context context) {
        super(context);

        this.setVerticalScrollBarEnabled(false);
        this.setHorizontalScrollBarEnabled(false);

        //        getSettings().setSupportMultipleWindows(true);

        //        this.setWebViewClient(new ActivityWebClient());
        //        this.setWebChromeClient(new ActivityWebChromeClient());
        this.setBackgroundColor(Color.BLACK);
        //        getSettings().setBuiltInZoomControls(true);
        getSettings().setUseWideViewPort(true);
        getSettings().setLoadWithOverviewMode(true);
        setInitialScale(1);
    }

    class ActivityWebClient extends WebViewClient {
        @Override
        public void onReceivedError (WebView view, int errorCode, String description, String failingUrl) {

        }
    }

    class ActivityWebChromeClient extends WebChromeClient {
        @Override
        public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(view);
            resultMsg.sendToTarget();

            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int progress) {
            super.onProgressChanged(view,progress);
        }
    }
}

public class TestExceptionActivity extends TestActivity {

}