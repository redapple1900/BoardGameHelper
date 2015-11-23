package com.yuanwei.resistance.partygame.avalon;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.yuanwei.resistance.model.protocol.BaseConfig;
import com.yuanwei.resistance.model.protocol.Role;
import com.yuanwei.resistance.partygame.avalon.model.Avalon;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Config extends BaseConfig{

	private Map<Role, Boolean> mConfig = new HashMap<>();

	public void setRoleEnabled(Role option, boolean enabled) {
		mConfig.put(option, enabled);
	}

	public boolean isRoleEnabled(Role option) {
		if (mConfig.containsKey(option)) {
			return mConfig.get(option);
		}
		return false;
	}

	//////////////////////////////////////////////////////////////////////////
	// Serialization

	private static final String PREF_KEY = "com.yuanwei.avalon.config";

	private static final String NUM_OF_PLAYERS_KEY = "number_of_players";

	public void save(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

		try {
			JSONObject obj = new JSONObject();

			obj.put(NUM_OF_PLAYERS_KEY, mCount);

			for (Role role : Avalon.getInstance().getSpecialRoles()) {
				obj.put(role.getName(), isRoleEnabled(role));
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

			for (Role role :Avalon.getInstance().getSpecialRoles()) {
				mConfig.put(role, obj.optBoolean(role.getName(), false));
			}
		}
		catch (JSONException e) {
			// Not going to happen
		}
	}
}

