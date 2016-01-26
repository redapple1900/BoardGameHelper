package com.yuanwei.resistance.partygame.origin.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chenyuanwei on 15/12/9.
 * <p/>
 * This class records Non Game State: Such as what fragment is on.
 */
public class StateKeeper {

    public static final String ROUND_KEY = "round";
    public static final String PROPOSE_KEY = "propose";
    public static final String LEADER_KEY = "leader";
    public static final String GAMER_KEY = "gamer";
    public static final String WIN_KEY = "win";
    public static final String LOSE_KEY = "lose";
    private static final String PREF_KEY = "com.yuanwei.resistance.statekeeper";
    private static final String STATE_KEY = "state";
    private Context mContext;
    private State mState;
    // TODO:: Consider using a map;
    private int mRound = 0;
    private int mPropose = 0;
    private int mLeader = 0;
    private int mWin = 0;
    private int mLose = 0;
    private int mCurrentUserIndex = 0;

    public StateKeeper(Context context) {
        mContext = context;
        mState = State.PROPOSE;
    }

    public void keep(String key, Integer integer) {
        switch (key) {
            case ROUND_KEY:
                mRound = integer;
                break;
            case PROPOSE_KEY:
                mPropose = integer;
                break;
            case LEADER_KEY:
                mLeader = integer;
                break;
            case WIN_KEY:
                mWin = integer;
                break;
            case LOSE_KEY:
                mLose = integer;
                break;
            case GAMER_KEY:
                mCurrentUserIndex = integer;
                break;
        }
        save();
    }

    public void keep(State state) {
        mState = state;
        save();
    }

    //////////////////////////////////////////////////////////////////////////
    // Serialization

    public int getRound() {
        return mRound;
    }

    public int getPropose() {
        return mPropose;
    }

    public int getLeader() {
        return mLeader;
    }

    public int getWin() {
        return mWin;
    }

    public int getLose() {
        return mLose;
    }

    public int getCurrentUserIndex() {
        return mCurrentUserIndex;
    }

    public State getState() {
        return mState;
    }

    // TODO:: Do not save everything.
    private void save() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        try {
            JSONObject obj = new JSONObject();

            obj.put(STATE_KEY, mState.ordinal());
            obj.put(ROUND_KEY, mRound);
            obj.put(PROPOSE_KEY, mPropose);
            obj.put(LEADER_KEY, mLeader);
            obj.put(WIN_KEY, mWin);
            obj.put(LOSE_KEY, mLose);
            obj.put(GAMER_KEY, mCurrentUserIndex);

            prefs.edit().putString(PREF_KEY, obj.toString()).apply();
        } catch (JSONException e) {
            // Not going to happen
        }
    }

    public void load() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String json = prefs.getString(PREF_KEY, "{}");

        try {
            JSONObject obj = new JSONObject(json);

            mState = State.values()[obj.optInt(STATE_KEY, 0)];
            mRound = obj.optInt(ROUND_KEY, 0);
            mPropose = obj.optInt(PROPOSE_KEY, 0);
            mLeader = obj.optInt(LEADER_KEY, 0);
            mWin = obj.optInt(WIN_KEY, 0);
            mLose = obj.optInt(LOSE_KEY, 0);
            mCurrentUserIndex = obj.optInt(GAMER_KEY, 0);
        } catch (JSONException e) {
            // Not going to happen
        }
    }

    public void clear() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        prefs.edit().putString(PREF_KEY, "").apply();
    }

    public enum State {
        PROPOSE,
        MISSION,
        ASSASSINATION,
        OVER,
        RESELECT
    }
}
