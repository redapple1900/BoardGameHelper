package com.yuanwei.resistance.partygame.origin;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.yuanwei.resistance.model.protocol.BaseConfig;
import com.yuanwei.resistance.partygame.origin.model.Resistance;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Config extends BaseConfig {

	private Map<Resistance.Option, Boolean> mConfig = new HashMap<>();

	public void setOptionEnabled(Resistance.Option option, boolean enabled) {
		mConfig.put(option, enabled);
	}

	public boolean isOptionEnabled(Resistance.Option option) {
		if (mConfig.containsKey(option)) {
			return mConfig.get(option);
		}
		return false;
	}

	//////////////////////////////////////////////////////////////////////////
	// Serialization

	private static final String PREF_KEY = "com.yuanwei.resistance.config";

	private static final String NUM_OF_PLAYERS_KEY = "number_of_players";

	public void save(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

		try {
			JSONObject obj = new JSONObject();

			obj.put(NUM_OF_PLAYERS_KEY, mCount);

			for (Resistance.Option option : Resistance.getInstance().getOptions()) {
				obj.put(option.name(), isOptionEnabled(option));
			}

			prefs.edit().putString(PREF_KEY, obj.toString()).commit();
		}
		catch (JSONException e) {
			// Not going to happen
		}
	}

	public void load(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String json = prefs.getString(PREF_KEY, "{}");

		try {
			JSONObject obj = new JSONObject(json);

			mCount = obj.optInt(NUM_OF_PLAYERS_KEY, 5);

			for (Resistance.Option option : Resistance.getInstance().getOptions()) {
				mConfig.put(option, obj.optBoolean(option.name(), false));
			}
		}
		catch (JSONException e) {
			// Not going to happen
		}
	}
}

