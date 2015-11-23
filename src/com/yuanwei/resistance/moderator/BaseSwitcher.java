package com.yuanwei.resistance.moderator;

import com.yuanwei.resistance.moderator.protocol.OnSwitchListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chenyuanwei on 15/10/25.
 *
 * Manages three counters, one master and two slaves. Trigger callback
 * when the master counter is reached, may trigger both callbacks or none
 */
public abstract class BaseSwitcher extends Staff {

    public static final int PRIMARY = 1;
    
    public static final int SECONDARY = -1;

    private static final String PREF_KEY = "com.yuanwei.resistance.brancher";

    private static final String TOTAL_COUNT_KEY = "total_count";

    private static final String CURRENT_COUNT_KEY = "current_count";

    private static final String PRIMARY_TAG = ".primary_accountant";

    private static final String SECONDARY_TAG = ".secondary_accountant";
    
    private int mTotal;
    
    private int mCurrent;
    
    private int mPrimaryTotal;
    
    private int mPrimaryCount;
    
    private int mSecondaryTotal;
    
    private int mSecondaryCount;

    private boolean mPrimaryReached;

    private boolean mSecondaryReached;

    private OnSwitchListener mListener;

    protected BaseSwitcher(OnSwitchListener listener) {
        super(listener);
        mListener = listener;
    }
    
    protected BaseSwitcher prepare(int total, int primary, int secondary) {
        mTotal = total;
        mPrimaryTotal = primary;
        mSecondaryTotal = secondary;
        mCurrent = mPrimaryCount = mSecondaryCount = 0;
        mPrimaryReached = mSecondaryReached = false;
        return this;
    }
    
    void incrementInternal(int flag, int step) {
        if (mCurrent >= mTotal) {
            return;
        }

        switch (flag) {
            case PRIMARY:
                mPrimaryCount += step;
                break;
            case SECONDARY:
                mSecondaryCount += step;
                break;
        }

        mCurrent += step;


        if (mPrimaryCount >= mPrimaryTotal && !mPrimaryReached) {
            mPrimaryReached = true;
            mListener.onSwitch(PRIMARY);
        }

        if (mSecondaryCount >= mSecondaryTotal && !mSecondaryReached) {
            mSecondaryReached = true;
            mListener.onSwitch(SECONDARY);
        }

    }

     void decrementInternal(int flag, int step) {
        if (mCurrent <= 0) {
            return;
        }

        switch (flag) {
            case PRIMARY:
                mPrimaryCount -= step;
                break;
            case SECONDARY:
                mSecondaryCount -= step;
                break;
        }

        mCurrent -= step;
    }

    public int getCount() {
        return mCurrent;
    }
    
    public int getPrimaryCount() {
        return mPrimaryCount;
    }
    
    public int getSecondaryCount() {
        return mSecondaryCount;
    }

    public boolean isPrimaryReached() {
        return mPrimaryReached;
    }

    public boolean isSecondaryReached() {
        return mSecondaryReached;
    }

    @Override
    protected String getInternalTag() {
        if (getTag() != null)
            return PREF_KEY;
        else
            return PREF_KEY + getTag();
    }

    @Override
    protected void saveToJSONObj(JSONObject obj) {
        
        try {
            obj.put(TOTAL_COUNT_KEY, mTotal);

            obj.put(CURRENT_COUNT_KEY, mCurrent);

            obj.put(TOTAL_COUNT_KEY + PRIMARY_TAG, mPrimaryTotal);

            obj.put(CURRENT_COUNT_KEY + PRIMARY_TAG, mPrimaryCount);

            obj.put(TOTAL_COUNT_KEY + SECONDARY_TAG, mSecondaryTotal);

            obj.put(CURRENT_COUNT_KEY + SECONDARY_TAG, mSecondaryCount);
            
        } catch (JSONException e) {
            // Nothing going to happen
        }
    }

    @Override
    protected void loadFromJSONObj(JSONObject obj) {
        
        mTotal = obj.optInt(TOTAL_COUNT_KEY);
        
        mCurrent = obj.optInt(CURRENT_COUNT_KEY);

        mPrimaryTotal = obj.optInt(TOTAL_COUNT_KEY + PRIMARY_TAG);

        mPrimaryCount = obj.optInt(CURRENT_COUNT_KEY + PRIMARY_TAG);

        mSecondaryTotal = obj.optInt(TOTAL_COUNT_KEY + SECONDARY_TAG);

        mSecondaryCount = obj.optInt(CURRENT_COUNT_KEY + SECONDARY_TAG);
    }
}
