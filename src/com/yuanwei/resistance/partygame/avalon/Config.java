package com.yuanwei.resistance.partygame.avalon;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.yuanwei.resistance.constant.Constants;
import com.yuanwei.resistance.model.protocol.BaseConfig;
import com.yuanwei.resistance.model.protocol.Role;
import com.yuanwei.resistance.partygame.avalon.model.Avalon;
import com.yuanwei.resistance.partygame.origin.model.Resistance;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Config extends BaseConfig{

    private static final String NUM_OF_PLAYERS_KEY = "number_of_players";
    private int mGame;
	private Map<Role, Boolean> mRoles = new HashMap<>();
	private Map<Resistance.Option, Boolean> mOptions = new HashMap<>();

	public Config(int game) {
		mGame = game;
	}

	public int getGame() {
		return mGame;
	}

	public void setRoleEnabled(Role option, boolean enabled) {
		mRoles.put(option, enabled);
	}

	public boolean isRoleEnabled(Role option) {
		if (mRoles.containsKey(option)) {
			return mRoles.get(option);
		}
		return false;
	}

	public void setOptionEnabled(Resistance.Option option, boolean enabled) {
		mOptions.put(option, enabled);
	}

    //////////////////////////////////////////////////////////////////////////
    // Serialization

	public boolean isOptionEnabled(Resistance.Option option) {
		if (mOptions.containsKey(option)) {
			return mOptions.get(option);
		}
		return false;
	}

	public void save(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

		try {
			JSONObject obj = new JSONObject();

			obj.put(NUM_OF_PLAYERS_KEY, mCount);

			for (Resistance.Option option : Resistance.getInstance().getOptions()) {
				obj.put(option.name(), isOptionEnabled(option));
			}

			for (Role role : Avalon.getInstance().getSpecialRoles()) {
				obj.put(role.getName(), isRoleEnabled(role));
			}

			prefs.edit().putString(getPrefKey(mGame), obj.toString()).apply();
		}
		catch (JSONException e) {
			// Not going to happen
		}
	}

	public void load(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String json = prefs.getString(getPrefKey(mGame), "{}");

		try {
			JSONObject obj = new JSONObject(json);

			mCount = obj.optInt(NUM_OF_PLAYERS_KEY, 5);

			for (Resistance.Option option : Resistance.getInstance().getOptions()) {
				mOptions.put(option, obj.optBoolean(option.name(), false));
			}

			if (mGame == Constants.AVALON) mOptions.remove(Resistance.Option.BLIND_SPY);

			for (Role role :Avalon.getInstance().getSpecialRoles()) {
				mRoles.put(role, obj.optBoolean(role.getName(), false));
			}
		}
		catch (JSONException e) {
			// Not going to happen
		}
    }

    public void clear(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(getPrefKey(mGame), "").apply();
    }

	private String getPrefKey(int game) {
		switch (game) {
			case Constants.ORIGIN:
				return "com.yuanwei.resistance.config";
			case Constants.AVALON:
				return "com.yuanwei.avalon.config";
		}
		return "com.yuanwei.avalon.config";
	}
}

