package net.sarangnamu.calcdate;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private Context context = null;
    private static final String PREFERENCES = "preferences.calcdate";

    private static final String DATE = "date";
    private static final String MESSAGE = "message";

    public PreferenceManager(Context context) {
        this.context = context;
    }

    public void setDate(final JSONArray date) {
        setData(DATE, date);
    }

    public JSONArray getDate() throws JSONException {
        return getData(DATE);
    }

    public void setMessage(final JSONArray message) {
        setData(MESSAGE, message);
    }

    public JSONArray getMessage() throws JSONException {
        return getData(MESSAGE);
    }

    private void setData(final String name, final JSONArray date) {
        SharedPreferences spRefresh = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spRefresh.edit();
        editor.putString(name, date.toString());
        editor.commit();
    }

    private JSONArray getData(final String name) throws JSONException {
        SharedPreferences spRefresh = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return new JSONArray(spRefresh.getString(name, "[]"));
    }
}
