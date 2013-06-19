package net.sarangnamu.testcalendar;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.pim.EventRecurrence;
import android.provider.Calendar.Attendees;
import android.provider.Calendar.Calendars;
import android.provider.Calendar.Events;
import android.provider.Calendar.EventsColumns;
import android.provider.*;
import android.provider.Calendar.Instances;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;



public class TestCalendarActivity extends Activity {
    /** Called when the activity is first created. */
    

//    public int getLastDayOfMonth(int year, int month, int week) {
//        Calendar c = Calendar.getInstance();
//        Date date = new Date(year - 1900, month - 1, 1);
//        c.setTimeInMillis(date.getTime());
//        c.set(Calendar.DAY_OF_WEEK, week);
//        c.set(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
//
//        return c.get(Calendar.DAY_OF_MONTH);
//    }
//
//    public int getLastDayOfMonth(Date date, int week) {
//        Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(date.getTime());
//        c.set(Calendar.DAY_OF_WEEK, week);
//        c.set(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
//
//        return c.get(Calendar.DAY_OF_MONTH);
//    }
    
    private static final String SORT_EVENTS_BY =
            "begin ASC, end DESC, title ASC";
    private static final String SORT_ALLDAY_BY =
            "startDay ASC, endDay DESC, title ASC";
    private static final String DISPLAY_AS_ALLDAY = "dispAllday";

    private static final String EVENTS_WHERE = DISPLAY_AS_ALLDAY + "=0";
    private static final String ALLDAY_WHERE = DISPLAY_AS_ALLDAY + "=1";

    private static final String[] PROJECTION = new String[] {
        Instances.TITLE,                 // 0
        Instances.EVENT_LOCATION,        // 1
        Instances.ALL_DAY,               // 2
        Instances.COLOR,                 // 3
        Instances.EVENT_TIMEZONE,        // 4
        Instances.EVENT_ID,              // 5
        Instances.BEGIN,                 // 6
        Instances.END,                   // 7
        Instances._ID,                   // 8
        Instances.START_DAY,             // 9
        Instances.END_DAY,               // 10
        Instances.START_MINUTE,          // 11
        Instances.END_MINUTE,            // 12
        Instances.HAS_ALARM,             // 13
        Instances.RRULE,                 // 14
        Instances.RDATE,                 // 15
        Instances.SELF_ATTENDEE_STATUS,  // 16
        Events.ORGANIZER,                // 17
        Events.GUESTS_CAN_MODIFY,        // 18
        Instances.DESCRIPTION            // 20
    };
    
 // The name of the shared preferences file. This name must be maintained for historical
    // reasons, as it's what PreferenceManager assigned the first time the file was created.
    static final String SHARED_PREFS_NAME = "com.android.calendar_preferences";

    // Preference keys
    public static final String KEY_HIDE_DECLINED = "preferences_hide_declined";

    public static final int ATTENDEE_STATUS_DECLINED = 2;
    
    public int startDay;       // start Julian day
    public int endDay;         // end Julian day
    public int startTime;      // Start and end time are in minutes since midnight
    public int endTime;


    // The indices for the projection array above.
    private static final int PROJECTION_TITLE_INDEX = 0;
    private static final int PROJECTION_LOCATION_INDEX = 1;
    private static final int PROJECTION_ALL_DAY_INDEX = 2;
    private static final int PROJECTION_COLOR_INDEX = 3;
    private static final int PROJECTION_TIMEZONE_INDEX = 4;
    private static final int PROJECTION_EVENT_ID_INDEX = 5;
    private static final int PROJECTION_BEGIN_INDEX = 6;
    private static final int PROJECTION_END_INDEX = 7;
    private static final int PROJECTION_START_DAY_INDEX = 9;
    private static final int PROJECTION_END_DAY_INDEX = 10;
    private static final int PROJECTION_START_MINUTE_INDEX = 11;
    private static final int PROJECTION_END_MINUTE_INDEX = 12;
    private static final int PROJECTION_HAS_ALARM_INDEX = 13;
    private static final int PROJECTION_RRULE_INDEX = 14;
    private static final int PROJECTION_RDATE_INDEX = 15;
    private static final int PROJECTION_SELF_ATTENDEE_STATUS_INDEX = 16;
    private static final int PROJECTION_ORGANIZER_INDEX = 17;
    private static final int PROJECTION_GUESTS_CAN_INVITE_OTHERS_INDEX = 18;
    private static final int PROJECTION_DESCRIPTION_INDEX = 20;

                
    private int[] dayString2Int(String[] bydays) {
        int[] values = new int[bydays.length];
        
        int i=0;
        for (String byday : bydays) {
            if (byday.equals("MO")) {
                values[i] = EventRecurrence.MO;
            } else if (byday.equals("TU")) {
                values[i] = EventRecurrence.TU;
            } else if (byday.equals("WE")) {
                values[i] = EventRecurrence.WE;
            } else if (byday.equals("TH")) {
                values[i] = EventRecurrence.TH;
            } else if (byday.equals("FR")) {
                values[i] = EventRecurrence.FR;
            } else if (byday.equals("SA")) {
                values[i] = EventRecurrence.SA;
            } else if (byday.equals("SU")) {
                values[i] = EventRecurrence.SU;
            }
            
            ++i;
        }

        return values;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("@@@@@@@@@", Environment.getExternalStorageDirectory().toString());
        
        
//        File f = new File("/mnt/sdcard", "kurome");
//        if (!f.exists()) {
//            Log.d("@@@@@@", "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + f.getPath());
//            f.mkdirs();
//        }
        
//        Log.d("@@@@@@", "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//        PackageManager m = getPackageManager();  
//        String s = getPackageName();  
//        try {  
//            PackageInfo p = m.getPackageInfo(s, 0);  
//            s = p.applicationInfo.dataDir;
//              
//            Log.d("@@@@@@@@@@@@", "PATH INFO " + s);  
//              
//        } catch (NameNotFoundException e) {  
//            e.printStackTrace();  
//        }         
//        

//      Integer lastDayOfMonth = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH); 
//      Log.d("@@@@", lastDayOfMonth.toString());
      
//        Uri uri = Events.CONTENT_URI;
//        int x = 0, y = 0;
//        if (x != 0 && ++y == 0) {
//
//        }
//        
//        Log.d("@@", "DATA : " + Integer.toString(y));
        
//        Log.d("@@@@@", Integer.toString(Integer.valueOf("+5")));
//        Log.d("@@@@@", Integer.toString(Integer.valueOf("-5")));
//        Log.d("@@@@@", Integer.toString(Integer.valueOf("5")));
//        
//        String d = "3WA";        
//        if (d.matches("[+]?-?\\d?(MO|TU|WE|TH|FR|SA|SU)")) {
//            Log.d("ok", "############### ok ###############");
//        } else {
//            Log.d("ok", "############### false ###############");
//        }
//        d = "WA";        
//        if (d.matches("[+]?-?\\d?(MO|TU|WE|TH|FR|SA|SU)")) {
//            Log.d("ok", "############### ok ###############");
//        } else {
//            Log.d("ok", "############### false ###############");
//        }
//        d = "-3WA";        
//        if (d.matches("[+]?-?\\d?(MO|TU|WE|TH|FR|SA|SU)")) {
//            Log.d("ok", "############### ok ###############");
//        } else {
//            Log.d("ok", "############### false ###############");
//        }
//        d = "+3WA";        
//        if (d.matches("[+]?-?\\d?(MO|TU|WE|TH|FR|SA|SU)")) {
//            Log.d("ok", "############### ok ###############");
//        } else {
//            Log.d("ok", "############### false ###############");
//        }
        
        
//        JSONObject obj = new JSONObject();
//        JSONArray byday = new JSONArray();
//        try {
//            obj.put("byday", byday);
//            Log.d("############", obj.toString());
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        
//        String rrule = "FREQ=WEEKLY;UNTIL=20161208T060000Z;INTERVAL=2;WKST=SU;BYDAY=MO,TH,WE,FI";
//        String INTERVAL = "INTERVAL";
//        String BYDAY = "BYDAY";
//        
//        if (rrule != null) {
//         // Add interval
//            int sPos = rrule.indexOf(INTERVAL), ePos;
//            if (sPos != -1) {
//                sPos = rrule.indexOf("=", sPos) + 1;
//                ePos = rrule.indexOf(";", sPos);
//                if (ePos == -1) {
//                    ePos = rrule.length();
//                }
//                
//                Log.d("### INTERVAL : ", rrule.substring(sPos, ePos));
//            }
//            
//            // Add byday
//            sPos = rrule.indexOf(BYDAY);
//            if (sPos != -1) {
//                sPos = rrule.indexOf("=", sPos) + 1;
//                ePos = rrule.indexOf(";", sPos);
//                if (ePos == -1) {
//                    ePos = rrule.length();
//                }
//                
//                Log.d("### BYDAY", rrule.substring(sPos, ePos));
//                String[] arrByday = rrule.substring(sPos, ePos).split(","); 
//                for (int i=0; i<arrByday.length; ++i) {
//                    Log.d("### BYDAY", "(" + Integer.toString(i) + ")" + arrByday[i]);  
//                }
//            }
//            
//            
//        }

//        Log.d("", Integer.toString(Calendar.SUNDAY) + ", " + Integer.toString(Calendar.MONDAY));
//        Log.d("", "VAL : " + Integer.toString(getLastDayOfMonth(2010, 9, Calendar.WEDNESDAY)));

//        Long unixtime = System.currentTimeMillis();
//        Log.d("", "Unixtime: " + unixtime.toString());
//        Date date = new Date(unixtime);
//        Log.d("", "Date: " + date.toString());

//        Date expire = new Date(1323324000000L);
//        Log.d("", "Expire: " + expire.toString());
//        Log.d("", "Week of month now : " + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
//
//        Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(1323903600000L);
//        //c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
//        c.set(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
//        Log.d("", "Last thursday of month : " + c.get(Calendar.DAY_OF_MONTH));

//        Calendar c = Calendar.getInstance();
//        Date date = new Date(2011 - 1900, 5, 1);
//        c.setTimeInMillis(date.getTime());
//        Log.d("", "YEAR : " + c.get(Calendar.YEAR));
//        Log.d("", "MONTH : " + c.get(Calendar.MONTH));
//        Log.d("", "DAY : " + c.get(Calendar.DATE));
//        c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
//        c.set(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
//        Log.d("", "YEAR : " + c.get(Calendar.YEAR));
//        Log.d("", "MONTH : " + c.get(Calendar.MONTH));
//        Log.d("", "DAY : " + c.get(Calendar.DATE));
//        Log.d("", "Last WEDNEDSAY of month : " + c.get(Calendar.DAY_OF_MONTH));

//      Remove all calendar events
//        String where = EventsColumns.CALENDAR_ID + "="
//        getContentResolver().delete(uri, null, null);


        // Reading calendar DB
//        String[] fields = new String[] {"_id", EventsColumns.TITLE, EventsColumns.RRULE, EventsColumns.DTSTART, EventsColumns.DTEND, EventsColumns.LAST_DATE};
//        String[] fields = null;
//
//        Date sd = new Date(2011 - 1900, 11, 9);
//        Date ed = new Date(2011 - 1900, 11, 10, 23, 59, 59);
//
//        String where = "";
//        String whereDeny = EventsColumns.VISIBILITY + "=0 AND " + EventsColumns.HAS_ATTENDEE_DATA + "=1";
//        String whereDeny = EventsColumns.HAS_ATTENDEE_DATA + "=1 AND deleted=0";
//
//        // 초기값
////        where = "_id > 405";
//        where += whereDeny;
//
//        // 시작일
//        where += " AND " + EventsColumns.DTSTART + " >= " + Long.toString(sd.getTime());
//
//        // 종료일 은 query 에 의미 없는 듯
//        //where += " AND (" + EventsColumns.DTEND + " is null OR " + EventsColumns.DTEND + "<=" + Long.toString(ed.getTime()) + ")";
//
//        // 재귀 범위
//        where += " OR (";
//        where += EventsColumns.LAST_DATE + " >= " + Long.toString(sd.getTime());
//        where += " AND " + whereDeny;
//        where += ")";
//
//        // Order by
//        String orderby = EventsColumns.DTSTART + " ASC";
//
//        Cursor cr;
//        cr = getContentResolver().query(uri, fields , where, null, orderby);
//        if (cr == null) {
//            return;
//        }
//        int count = 0;
//        Log.d("FIELD", "== START(" + Integer.toString(cr.getCount()) + ") ==");
//        while (cr.moveToNext()) {
//            traceAlldata(cr);
//        }
//        cr.close();
        
        ///////////////
        
//        Uri REMINDERS_URI = Uri.parse("content://com.android.calendar/" + "reminders");
//        Cursor cr2 = getContentResolver().query(REMINDERS_URI, null , null, null, null);
//        Log.d("REMINDERS", "== START(" + Integer.toString(cr2.getCount()) + ") ==");
//        while (cr2.moveToNext()) {
//            traceAlldata(cr2);
//        }
//        cr2.close();
//        
//        uri = Uri.parse("content://com.android.calendar/attendees");
//        cr = getContentResolver().query(uri, null , null, null, null);
//        Log.d(uri.toString(), "== START(" + Integer.toString(cr.getCount()) + ") ==");
//        while (cr.moveToNext()) {
//            traceAlldata(cr);
//        }
//        cr.close();
        
//        uri = Uri.parse("content://com.android.calendar/calendar_alerts");
//        cr = getContentResolver().query(uri, null , null, null, null);
//        Log.d(uri.toString(), "== START(" + Integer.toString(cr.getCount()) + ") ==");
//        while (cr.moveToNext()) {
//            traceAlldata(cr);
//        }
//        cr.close();
        
//        uri = Uri.parse("content://com.android.calendar/calendars");
//        cr = getContentResolver().query(uri, null , null, null, null);
//        Log.d(uri.toString(), "== START(" + Integer.toString(cr.getCount()) + ") ==");
//        while (cr.moveToNext()) {
//            traceAlldata(cr);
//        }
//        cr.close();
//
//        
//        uri = Uri.parse("content://com.android.calendar/extendedproperties");
//        cr = getContentResolver().query(uri, null , null, null, null);
//        Log.d(uri.toString(), "== START(" + Integer.toString(cr.getCount()) + ") ==");
//        while (cr.moveToNext()) {
//            traceAlldata(cr);
//        }
//        cr.close();
        
//      uri = Uri.parse("content://com.android.calendar/event_entities");
//      cr = getContentResolver().query(uri, null , null, null, null);
//      Log.d(uri.toString(), "== START(" + Integer.toString(cr.getCount()) + ") ==");
//      while (cr.moveToNext()) {
//          traceAlldata(cr);
//      }
//      cr.close(); 

//      uri = Uri.parse("content://com.android.calendar/instances/when");
//      cr = getContentResolver().query(uri, null , null, null, null);
//      Log.d(uri.toString(), "== START(" + Integer.toString(cr.getCount()) + ") ==");
//      while (cr.moveToNext()) {
//          traceAlldata(cr);
//      }
//      cr.close();
//        
//        Time local = new Time();
//        int count;
//        
//        
//      Date sd = new Date(2011 - 1900, 11, 1);
//      Date ed = new Date(2011 - 1900, 11, 30);
//
//        
//        long start = sd.getTime();
//        int days = 30;
//
//        local.set(start);
//        int startDay = Time.getJulianDay(start, local.gmtoff);
//        int endDay = startDay + days;
//
//        local.monthDay += days;
//        long end = ed.getTime(); //local.normalize(true /* ignore isDst */);
//        String orderBy = Instances.SORT_CALENDAR_VIEW;
//        
//        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
//        boolean hideDeclined = prefs.getBoolean(KEY_HIDE_DECLINED, false);
//
////        String where = "";
//        if (hideDeclined) {
//            where = Instances.SELF_ATTENDEE_STATUS + "!=" + Attendees.ATTENDEE_STATUS_DECLINED;
//        }
//        
//        Cursor c = null;
//        boolean searchingType = false;
//        if (searchingType) {
//            c = Instances.query(getContentResolver(), PROJECTION, start - DateUtils.DAY_IN_MILLIS, end + DateUtils.DAY_IN_MILLIS, where, orderBy);
//        } else {
//            
//            String searchArea = (start - DateUtils.DAY_IN_MILLIS) + "/" +  (end + DateUtils.DAY_IN_MILLIS);
//            Log.d("start", "start : " + searchArea);
//            
//            orderBy = Instances.DEFAULT_SORT_ORDER;
//            Uri uri = Uri.withAppendedPath(Instances.CONTENT_URI, (start - DateUtils.DAY_IN_MILLIS) + "/" +  (end + DateUtils.DAY_IN_MILLIS));
//            c = getContentResolver().query(uri, 
//                        new String[] {
//                            "_id", Instances.BEGIN, Instances.END, EventsColumns.DESCRIPTION, 
//                            EventsColumns.TITLE, EventsColumns.DTSTART, EventsColumns.DTEND, 
//                            EventsColumns.EVENT_LOCATION, EventsColumns.RRULE, EventsColumns.STATUS
//                            },
//                    null, null, orderBy);    
//        }
//        
//        if (c == null) {
//            Log.e("Cal", "loadEvents() returned null cursor!");
//            return;
//        }
//        
//        count = c.getCount();
//        Log.d("", "COUNT: " + count);
//
//        if (count == 0) {
//            return;
//        }

        
//        while (c.moveToNext()) {
////            Event e = new Event();
//            
//            StringBuilder sb = new StringBuilder();
//            sb.append("ID: ");
//            
//            if (searchingType) {
//              sb.append(c.getString(PROJECTION_EVENT_ID_INDEX));
//            } else {
//                sb.append(c.getString(0));    
//            }
//            sb.append(", TITLE: ");
//            
//            if (searchingType) {
//                sb.append(c.getString(PROJECTION_TITLE_INDEX));
//            } else {
//                sb.append(c.getString(4));    
//            }
//            
//            Log.d("", sb.toString());
//            
            
//            e.id = c.getLong(PROJECTION_EVENT_ID_INDEX);
//            e.title = c.getString(PROJECTION_TITLE_INDEX);
//            e.location = c.getString(PROJECTION_LOCATION_INDEX);
//            e.allDay = c.getInt(PROJECTION_ALL_DAY_INDEX) != 0;
//            e.organizer = c.getString(PROJECTION_ORGANIZER_INDEX);
//            e.guestsCanModify = c.getInt(PROJECTION_GUESTS_CAN_INVITE_OTHERS_INDEX) != 0;
//
//            String timezone = c.getString(PROJECTION_TIMEZONE_INDEX);

//            if (e.title == null || e.title.length() == 0) {
//                e.title = res.getString(R.string.no_title_label);
//            }

//            if (!c.isNull(PROJECTION_COLOR_INDEX)) {
//                // Read the color from the database
//                e.color = c.getInt(PROJECTION_COLOR_INDEX);
//            } else {
//                e.color = res.getColor(R.color.event_center);
//            }
//
//            long eStart = c.getLong(PROJECTION_BEGIN_INDEX);
//            long eEnd = c.getLong(PROJECTION_END_INDEX);
//
//            e.startMillis = eStart;
//            e.startTime = c.getInt(PROJECTION_START_MINUTE_INDEX);
//            e.startDay = c.getInt(PROJECTION_START_DAY_INDEX);
//
//            e.endMillis = eEnd;
//            e.endTime = c.getInt(PROJECTION_END_MINUTE_INDEX);
//            e.endDay = c.getInt(PROJECTION_END_DAY_INDEX);
//
//            if (e.startDay > endDay || e.endDay < startDay) {
//                continue;
//            }
//
//            e.hasAlarm = c.getInt(PROJECTION_HAS_ALARM_INDEX) != 0;
//
//            // Check if this is a repeating event
//            String rrule = c.getString(PROJECTION_RRULE_INDEX);
//            String rdate = c.getString(PROJECTION_RDATE_INDEX);
//            if (!TextUtils.isEmpty(rrule) || !TextUtils.isEmpty(rdate)) {
//                e.isRepeating = true;
//            } else {
//                e.isRepeating = false;
//            }
//            
//            e.selfAttendeeStatus = c.getInt(PROJECTION_SELF_ATTENDEE_STATUS_INDEX);
//
//            events.add(e);
//        }
        
//        computePositions(events);
        
        setContentView(R.layout.main);
    }

//
//
//    public void traceColumnNames(Cursor cr) {
//        String names[] = cr.getColumnNames();
//        for (String name : names) {
//            Log.d("FIELD", name);
//        }
//
//        Log.d("RRULE", "=============");
//        Log.d("RRULE", cr.getString(5));
//    }
//
//    public void traceAlldata(Cursor cr) {
//        String names[] = cr.getColumnNames();
//        int count = 0;
//        StringBuilder sb = new StringBuilder();
//        for (String name : names) {
//            sb.append(name);
//            sb.append(":");
//            sb.append(cr.getString(count++));
//            sb.append(", ");
//        }
//        Log.d("VALUE", sb.toString());
//    }
}


/**
 * android.provider.Calender 내에 기본 검색된 정보에서 Recurrence rule
 * 를 적용해 값을 반환한다.
 *
 * @author <a href="mailto:Burke.Choi@obigo.com">krinbuch</a>
 * @see <a href="http://www.kanzaki.com/docs/ical/recur.html" target="_blank">Recurrence Rule</a>
 *
 * <pre>
 * {@code
 *  String rrule = cr.getString(0);
 *  RecurrenceRule rr = new RecurrenceRule(rrule);
 *  if (rr.isValid()) {
 *      // add data
 *  }
 * }
 * </pre>
 */
class RecurrenceRule {

    /**
     * The FREQ rule part identifies the type of recurrence rule.
     * This rule part MUST be specified in the recurrence rule.
     * Valid values include SECONDLY, to specify repeating events
     * based on an interval of a second or more;
     * MINUTELY, to specify repeating events based on an interval of a minute or more;
     * HOURLY, to specify repeating events based on an interval of an hour or more;
     * DAILY, to specify repeating events based on an interval of a day or more;
     * WEEKLY, to specify repeating events based on an interval of a week or more;
     * MONTHLY, to specify repeating events based on an interval of a month or more; and YEARLY,
     * to specify repeating events based on an interval of a year or more.
     */
    private String freq;

    /**
     * The BYDAY rule part specifies a COMMA character (US-ASCII decimal 44)
     * separated list of days of the week;
     *
     * Each BYDAY value can also be preceded by a positive (+n) or negative (-n) integer.
     * If present, this indicates the nth occurrence of the specific day within
     * the MONTHLY or YEARLY RRULE.
     *
     * For example, within a MONTHLY rule, +1MO (or simply 1MO) represents the first Monday
     * within the month, whereas -1MO represents the last Monday of the month.
     *
     * If an integer modifier is not present, it means all days of this type within the specified frequency.
     *
     * For example, within a MONTHLY rule, MO represents all Mondays within the month.
     */
    private String byDay;

    /**
     * The BYMONTHDAY rule part specifies a COMMA character (ASCII decimal 44)
     * separated list of days of the month.
     * Valid values are 1 to 31 or -31 to -1.
     * For example, -10 represents the tenth to the last day of the month.
     */
    private String byMonthDay;

    /**
     * The BYMONTH rule part specifies a COMMA character (US-ASCII decimal 44)
     * separated list of months of the year. Valid values are 1 to 12.
     */
    private String byMonth;

    /**
     * The INTERVAL rule part contains a positive integer representing
     * how often the recurrence rule repeats. The default value is "1",
     * meaning every second for a SECONDLY rule, or every minute
     * for a MINUTELY rule, every hour for an HOURLY rule,
     * every day for a DAILY rule, every week for a WEEKLY rule,
     * every month for a MONTHLY rule and every year for a YEARLY rule.
     *
     * First, the "INTERVAL=2" would be applied to "FREQ=YEARLY" to arrive at "every other year".
     * Then, "BYMONTH=1" would be applied to arrive at "every January, every other year". Then,
     * "BYDAY=SU" would be applied to arrive at "every Sunday in January, every other year".
     *
     * Then, "BYHOUR=8,9" would be applied to arrive at "every Sunday in January at 8 AM and 9 AM,
     * every other year". Then,
     *
     * "BYMINUTE=30" would be applied to arrive at "every Sunday in January at 8:30 AM and 9:30 AM,
     * every other year". Then, lacking information from RRULE, the second is derived from DTSTART,
     * to end up in "every Sunday in January at 8:30:00 AM and 9:30:00 AM, every other year".
     * Similarly, if the BYMINUTE, BYHOUR, BYDAY, BYMONTHDAY or BYMONTH rule part were missing,
     * the appropriate minute, hour, day or month would have been retrieved from the "DTSTART" property.
     */
    private String interval;

    private Date dateStart;

    private Date dateEnd;

    private Date eventStart;


    /**
     * MO indicates Monday;
     * TU indicates Tuesday;
     * WE indicates Wednesday;
     * TH indicates Thursday;
     * FR indicates Friday;
     * SA indicates Saturday;
     * SU indicates Sunday.
     */
    public static final String[] rfcWeekdays = {"SU", "MO", "TU", "WE", "TH", "FR", "SA"};

    public static final String DAILY   = "DAILY";
    public static final String WEEKLY  = "WEEKLY";
    public static final String MONTHLY = "MONTHLY";
    public static final String YEARLY  = "YEARLY";


    /**
     *
     * @param src 파싱할 Recurrence Rule 값
     * @param dateStart 이벤트 시작 타임 값
     */
    public RecurrenceRule(String src, Date dateStart, Date dateEnd) {
        String[] datas = src.split(";");

        this.dateStart = dateStart;
        this.dateEnd   = dateEnd;

        // data parsing
        int pos;
        for (String data : datas) {
            pos = data.indexOf('=');
            if (pos != -1) {
                pos++;
            } else {
                continue;
            }

            if (data.indexOf("FREQ") != -1) {
                freq = data.substring(pos);
            } else if (data.indexOf("BYDAY") != -1) {
                byDay = data.substring(pos);
            } else if (data.indexOf("INTERVAL") != -1) {
                interval = data.substring(pos);
            } else if (data.indexOf("BYMONTHDAY") != -1) {
                byMonthDay = data.substring(pos);
            } else if (data.indexOf("BYMONTH") != -1) {
                byMonth = data.substring(pos);
            }
        }
    }

    public int diffDate() {
        return ((int)(dateEnd.getTime() - dateStart.getTime()) / (24 * 60 * 60 * 1000));
    }


    /**
     * 현재 가지고 있는 data 가 유효한지 체크 한다.
     *
     * UNTIL 의 경우 android 내에 Last date 디비 필드로 선 확인 가능하기 때문에
     * 필터링에서 제외 한다.
     *
     * @return true or false
     */
    public boolean isValid() {
        if (freq.equals(DAILY)) {
            // FREQ=DAILY;UNTIL=20121208T060000Z;WKST=SU
            // DAILY 는 무조건 true;
        } else if (freq.equals(WEEKLY)) {
            // FREQ=WEEKLY;UNTIL=20161208T060000Z;WKST=SU;BYDAY=TH, DATE:null
            // FREQ=WEEKLY;UNTIL=20161208T060000Z;WKST=SU;BYDAY=MO,TU,WE,TH,FR
            // FREQ=WEEKLY;UNTIL=20161208T060000Z;INTERVAL=2;WKST=SU;BYDAY=TH

            int diffDate = diffDate();
            Log.d("", "diffdate : " + Integer.toString(diffDate));
            if (diffDate < 6) {
                int startDayOfWeek = getDayOfWeek(dateStart.getTime()) - 1;

                while(diffDate > 0) {

                    if (startDayOfWeek > 7) {
                        startDayOfWeek = 0;
                    }

                    diffDate--;
                    startDayOfWeek++;
                }

                if (byDay.indexOf(rfcWeekdays[startDayOfWeek]) == -1) {
                    return false;
                }
            }

//            if (interval != null) {
//                if (!checkValidWeek()) {
//                    return false;
//                }
//            }



            if (!isExistDayOfWeek()) {
                return false;
            }
        } else if (freq.equals(MONTHLY)) {
            // FREQ=MONTHLY;UNTIL=20211208T060000Z;WKST=SU;BYDAY=2TH
            // FREQ=MONTHLY;UNTIL=20211208T060000Z;WKST=SU;BYMONTHDAY=8
            // FREQ=MONTHLY;UNTIL=20211228T230000Z;WKST=SU;BYDAY=-1TH       // Last Thursday of month

            if (byMonthDay != null) {
                int byMonthDay = Integer.parseInt(this.byMonthDay);

//                if (dateStart.getMonth() == dateEnd.getMonth()) {
//                    // 월이 같으나 날짜가 범위 밖이라면 오류
//                    if (dateStart.getDate() > byMonthDay || dateEnd.getDate() < byMonthDay) {
//                        return false;
//                    }
//                } else {
//                    Calendar ca = Calendar.getInstance();
//                    ca.setTimeInMillis(dateStart.getTime());
//                    ca.add(ca.MONTH, 1);
//
//                    // 시작범위에서 1달이 안되면
//                    if (dateEnd.getTime() < ca.getTime().getTime()) {
//                        // 시작 달보다 날짜가 작고 종료 달 보다 날짜가 크면 오류
//                        if (dateStart.getDate() > byMonthDay) {
//                            if (dateEnd.getDate() < byMonthDay) {
//                                return false;
//                            }
//                        } else {
//                            if (dateEnd.getDate() < byMonthDay) {
//                                return false;
//                            }
//                        }
//                    }
//                }
            } else if (byDay != null) {
                if (byDay.charAt(0) == '-') {
                    // 값에 - 가 있는 경우 마지막 주를 찾아서 검색한다.
                    // <!> 원하는 달의 범위가 1개월 단위 이상이면 검색 범위에 문제가 발생 될 수 있다.
                    int value = Integer.parseInt(byDay.substring(1, 2));
                    int dayOfWeek = strToIntForDayOfWeek(byDay.substring(2, 4));
                    if (dayOfWeek == -1) {
                        return false;
                    }

                    int lastDayOfMonth = getLastDayOfMonth(dateStart.getTime(), dayOfWeek);
                    if (value > 1) {
                        int dec = --value * 7;
                        // value 값이 1보다 크면 lastDayOfMonth 값에서 (--value * 7) 씩 빼준다.
                        lastDayOfMonth -= dec;
                    }

                    // <!> 잠시 이건 패스

                } else {
                    if (!byDay.substring(1, 3).equals(getDayOfWeek())) {
                        return false;
                    }
                }
            } else {
                return false;
            }

        } else if (freq.equals(YEARLY)) {
            // FREQ=YEARLY;UNTIL=20361208T060000Z;WKST=SU;BYMONTHDAY=8;BYMONTH=12

            // Date.getMonth() : The value returned is between 0 and 11
            // Date.getDate()  : The value returned is between 1 and 31

            Date date;
            int byMonth    = Integer.parseInt(this.byMonth) - 1;
            int byMonthDay = Integer.parseInt(this.byMonthDay);    // Valid values are 1 to 31 or -31 to -1.
            int year       = byMonth > dateStart.getMonth() ? dateStart.getYear() : dateEnd.getYear();

            // byMonthDay 값이 -(음수) 값이면 해당 달의 마지막 일 에서 - 해서 날짜를 정의 하면 된다.
            if (byMonthDay < 0) {
                Date tmpDate = new Date(year, byMonth, 1);
                int lastDay = getLastDayOfMonth(tmpDate.getTime()) + 1;
                byMonthDay  = lastDay - byMonthDay;
                date = new Date(year, byMonth, byMonthDay);
            } else {
                date = new Date(year, byMonth, byMonthDay);
            }

            if (dateStart.getTime() > date.getTime() || dateEnd.getTime() < date.getTime()) {
                return false;
            }
        }

        return true;
    }


    /**
     * 현재의 day of week 를 iCalendar 에 weekday 형식에 맞게 반환 한다.
     *
     * @return rfcWeekdays 배열내의 값
     */
    public String getDayOfWeek() {
        return rfcWeekdays[Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1];
    }


    /**
     * byDay 내에 현재 day of week 가 포함되어 있는지 확인 한다.
     * @return true or false
     */
    public boolean isExistDayOfWeek() {
        String weekday = getDayOfWeek();
        if (byDay.indexOf(weekday) != -1) {
            return true;
        }

        return false;
    }

    /**
     * 현재 몇 째주 인지 반환 한다.
     *
     * @return 1~4
     */
    public int getCurrentWeekOfMonth() {
        return Calendar.getInstance().get(Calendar.WEEK_OF_MONTH);
    }

    /**
     * 입력된 timestamp 값이 몇 째주 인지 반환 한다.
     *
     * @param timestamp 원하는 날짜
     * @return 1~4
     */
    public int getWeekOfMonth(long timestamp) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timestamp);

        return c.get(Calendar.WEEK_OF_MONTH);
    }

    /**
     * 지정된 interval 이 유효한지 검사한다.
     *
     * @return true or false
     */
    public boolean checkValidWeek() {
        int weekOfMonth = getWeekOfMonth(dateStart.getTime()) % 2;
        int currentWeekOfMonth = getCurrentWeekOfMonth() % 2;
        if (weekOfMonth != currentWeekOfMonth) {
            return false;
        }

        return true;
    }

    public int getLastDayOfMonth(long timestamp) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timestamp);

        int lastDate = c.getActualMaximum(Calendar.DATE);
        c.set(Calendar.DATE, lastDate);

        return c.get(Calendar.DAY_OF_WEEK);
    }

//    public int getLastDayOfMonth(int year, int month, int week) {
//        Calendar c = Calendar.getInstance();
//        Date date = new Date(year - 1900, month - 1, 1);
//        c.setTimeInMillis(date.getTime());
//        c.set(Calendar.DAY_OF_WEEK, week);
//        c.set(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
//
//        return c.get(Calendar.DAY_OF_MONTH);
//    }

    public int getLastDayOfMonth(long timestamp, int week) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timestamp);
        c.set(Calendar.DAY_OF_WEEK, week);
        c.set(Calendar.DAY_OF_WEEK_IN_MONTH, -1);

        return c.get(Calendar.DAY_OF_MONTH);
    }

    public int getDayOfWeek(long timestamp) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timestamp);

        return c.get(Calendar.DAY_OF_WEEK);
    }

    public int strToIntForDayOfWeek(String value) {
        int i = 1;
        for (String weekday : rfcWeekdays) {
            if (weekday.equals(value)) {
                return i;
            }

            ++i;
        }

        return -1;
    }

    /**
     * 파싱된 데이터들을 출력 한다.
     */
    public void trace() {
        Log.d("", "FREQ         :" + freq);
        Log.d("", "BYMONTHDAY   :" + byMonthDay);
        Log.d("", "BYMONTH      :" + byMonth);
        Log.d("", "INTERVAL     :" + interval);
        Log.d("", "BYDAY        :" + byDay);
        Log.d("data", "=====");
    }

}