package net.sarangnamu.calcdate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class CalcDateActivity extends Activity {
    private static final String TAG = "CalcDateActivity";
    private static final int DATE_DIALOG_ID = 1;

    private long currentTime;
    private Button add, refresh;
    private TextView date;
    private EditText message;
    private ListView list;
    private JSONArray dateValue, msgValue;
    private DateAdapter adapter;
    private PreferenceManager preManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        message = (EditText)findViewById(R.id.message);
        date = (TextView)findViewById(R.id.date);
        list = (ListView)findViewById(R.id.list);
        add  = (Button)findViewById(R.id.add);
        refresh     = (Button)findViewById(R.id.refresh);
        preManager  = new PreferenceManager(this);
        adapter     = new DateAdapter(this);

        setService();
        setCurrentTime();
        loadDate();
        initDate();
        initAdd();
        initList();
        hideSoftKeyboard();
        restartSetting();
    }

    private void loadDate() {
        try {
            dateValue = preManager.getDate();
            msgValue  = preManager.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initDate() {
        date.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
    }

    public void initAdd() {
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (date.getText() == null || date.getText().length() == 0) {
                    return ;
                }

                String ymd = date.getText().toString();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    Date dt = formatter.parse(ymd);

                    int y = dt.getYear() + 1900;
                    int m = dt.getMonth();
                    int d = dt.getDate();

                    dateValue = preManager.getDate();
                    Calendar ca = Calendar.getInstance();
                    ca.set(y, m, d, 0, 0, 1);

                    dateValue.put(ca.getTimeInMillis());
                    preManager.setDate(dateValue);

                    msgValue = preManager.getMessage();
                    msgValue.put(message.getText());
                    preManager.setMessage(msgValue);

                    adapter.notifyDataSetChanged();

                    date.setText("");
                    message.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void initRefersh() {
        refresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setCurrentTime();
                    dateValue = preManager.getDate();
                    msgValue  = preManager.getMessage();
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void initList() {
        list.setAdapter(adapter);
        list.setEmptyView(findViewById(R.id.emptyText));
    }

    class ViewHolder {
        public TextView title;
        public TextView date;
    }

    private class DateAdapter extends BaseAdapter {
        private final LayoutInflater mInflater;

        public DateAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (dateValue == null) {
                return 0;
            }

            return dateValue.length();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_date_item, null);

                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.date  = (TextView) convertView.findViewById(R.id.date);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            try {
                long timeValue = dateValue.getLong(position);
                long calTime   = (currentTime/1000) - (timeValue/1000);

                StringBuilder sb = new StringBuilder();
                sb.append(position + 1);
                holder.title.setText(sb.toString());

                StringBuilder sb2 = new StringBuilder();
                sb2.append(") ");
                sb2.append(msgValue.getString(position));
                sb2.append(" [");
                sb2.append(getStartDay(timeValue));
                sb2.append("] ");
                sb2.append(getCalcDay(calTime));
                sb2.append(getString(R.string.day));
                holder.date.setText(sb2.toString());

                convertView.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(final View v) {
                        new AlertDialog.Builder(CalcDateActivity.this)
                        .setMessage(R.string.want_remove)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    ViewHolder holder = (ViewHolder)v.getTag();
                                    int pos = Integer.parseInt(holder.title.getText().toString()) - 1;
                                    JSONArray modDate = new JSONArray();
                                    JSONArray modMessage = new JSONArray();

                                    int size = dateValue.length();
                                    for (int i=0; i<size; ++i) {
                                        if (pos != i) {
                                            modDate.put(dateValue.get(i));
                                            modMessage.put(msgValue.get(i));
                                        }
                                    }

                                    dateValue = modDate;
                                    msgValue  = modMessage;

                                    preManager.setDate(modDate);
                                    preManager.setMessage(modMessage);

                                    setCurrentTime();
                                    adapter.notifyDataSetChanged();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

                        return false;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            return convertView;
        }
    }

    private void setCurrentTime() {
        Calendar ca = Calendar.getInstance();
        ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DATE), 0, 0, 1);
        currentTime = ca.getTimeInMillis();
    }

    private String getStartDay(long time) {
        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(time);

        String locale = getResources().getConfiguration().locale.toString().substring(0, 2);
        if (locale.equals("ko")) {
            return String.format("%04d.%02d.%02d", ca.get(Calendar.YEAR), ca.get(Calendar.MONTH) + 1, ca.get(Calendar.DATE));
        }

        return String.format("%02d.%02d.%04d", ca.get(Calendar.DATE), ca.get(Calendar.MONTH) + 1, ca.get(Calendar.YEAR));
    }

    private long getCalcDay(long time) {
        return (time / 86400) + 1;
    }

    private void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // DATE PICKER
    //
    ////////////////////////////////////////////////////////////////////////////////////

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            date.setText(String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay));
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_DIALOG_ID:
            Calendar ca = Calendar.getInstance();

            return new DatePickerDialog(this, datePickerListener, ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DATE));
        }
        return null;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // SERVICE
    //
    ////////////////////////////////////////////////////////////////////////////////////

    private Thread.UncaughtExceptionHandler onRuntimeError = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            //Try starting the service again
            //May be a pending intent might work

            Log.d(TAG, "===================================================================");
            Log.d(TAG, "===================================================================");
            Log.d(TAG, "===================================================================");
            Log.d(TAG, "KILL APP !!!!!!!!!!!!!!!!!!!!!!!!!");
            Log.d(TAG, "===================================================================");
            Log.d(TAG, "===================================================================");
            Log.d(TAG, "===================================================================");

            //            AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            //            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, intent);
            //            System.exit(2);
        }
    };
    //    private PendingIntent intent = null;

    private void restartSetting() {
        Thread.setDefaultUncaughtExceptionHandler(onRuntimeError);
        //        intent = PendingIntent.getActivity(getBaseContext(), 0, new Intent(getIntent()), getIntent().getFlags());
    }

    private void setService() {
        startService(new Intent(this, AlertService.class));
    }
}