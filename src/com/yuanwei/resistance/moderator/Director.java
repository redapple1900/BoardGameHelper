package com.yuanwei.resistance.moderator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;


/**
 * Created by chenyuanwei on 15/10/26.
 */
public class Director {

    private static final String TAG = "com.yuanwei.resistance.director";

    private static final String SIZE = "size";

    private static final String DATA = "data";

    private Context mContext;

    private LinkedHashSet<Staff> mSecretaries;

    private Map<String, Staff> mRegisterMap;

    public Director(Context context) {
        mContext = context;
        mSecretaries = new LinkedHashSet<>();
        mRegisterMap = new HashMap<>();
    }

    public final void saveAll() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        JSONObject obj = new JSONObject();
        try {
            // Store the size
            obj.put(SIZE, mSecretaries.size());

            // Store all data
            JSONArray array = new JSONArray();

            int i = 0;

            for (Staff secretary : mSecretaries) {
                array.put(i, secretary.getInternalTag());
                i++;

                secretary.save(mContext);
            }

            obj.put(DATA, array);
        } catch (JSONException e) {

        }
        prefs.edit().putString(TAG, obj.toString()).commit();
    }

    public final void loadAll() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        String json = prefs.getString(TAG, "{}");

        try {
            JSONObject obj = new JSONObject(json);

            int size = obj.getInt(SIZE);

            JSONArray array = obj.getJSONArray(DATA);

            for (int i = 0; i < size; i++) {
                mSecretaries.add(mRegisterMap.get(array.getString(i)));
            }

        } catch (JSONException e) {
            // Not going to happen
        }

        for (Staff secretary : mSecretaries) {
            secretary.load(mContext);
        }
    }

    public final void clearAll() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        prefs.edit().remove(TAG).commit();

        for (Staff secretary : mSecretaries) {
            secretary.clear(mContext);
        }
    }

    public boolean register(Staff secretary) {

        if (secretary == null) throw new NullPointerException();

        if (mRegisterMap.containsKey(secretary.getInternalTag())) {
            return false;
        } else {
            mRegisterMap.put(secretary.getInternalTag(), secretary);
            return true;
        }
    }

    public boolean unregister(Staff secretary) {

        if (secretary == null) throw new NullPointerException();

        if (!mRegisterMap.containsKey(secretary.getInternalTag())){
            return false;
        } else {

            if (mSecretaries.contains(secretary)) {
                secretary.clear(mContext);
                mSecretaries.remove(secretary);
            }
            mRegisterMap.remove(secretary.getInternalTag());
            return true;
        }
    }

    public final void enroll(Staff secretary) {
        if (secretary == null) throw new NullPointerException();
        
        if (!mRegisterMap.containsKey(secretary.getInternalTag()))
            register(secretary);

        mSecretaries.add(secretary);
    }

    public final void dismiss(Staff secretary) {
        if (secretary == null) throw new NullPointerException();

        mSecretaries.remove(secretary);
        secretary.clear(mContext);
    }
}
