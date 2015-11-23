package com.yuanwei.resistance.moderator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chenyuanwei on 15/10/25.
 *
 * Handle persistence api.
 */
public abstract class BaseStaff {

    private String Tag;

    public String getTag() {
        return Tag;
    }

    public BaseStaff setTag(String TAG) {
        this.Tag = TAG;
        return this;
    }

    public final void save(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        JSONObject obj = new JSONObject();

        saveToJSONObj(obj);

        prefs.edit().putString(getInternalTag(), obj.toString()).commit();
    }

    public final void load(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String json = prefs.getString(getInternalTag(), "{}");

        try {
            JSONObject obj = new JSONObject(json);

            loadFromJSONObj(obj);
        } catch (JSONException e) {
            // Not going to happen
        }
    }

    public final void clear(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        prefs.edit().remove(getInternalTag()).commit();
    }

    protected abstract void saveToJSONObj(JSONObject obj);

    protected abstract void loadFromJSONObj(JSONObject obj);

    protected abstract String getInternalTag();
}
