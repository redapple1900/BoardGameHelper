package com.yuanwei.resistance.moderator;

import android.util.Log;

import com.yuanwei.resistance.BuildConfig;
import com.yuanwei.resistance.moderator.protocol.OnSwitchListener;

/**
 * Created by chenyuanwei on 15/10/25.
 *
 * Manages three counters, one master and two slaves. Trigger callback
 * when the master counter is reached, may trigger both callbacks or none
 */
public abstract class BaseSwitcher extends Staff {

    public static final int PRIMARY = -2;
    
    public static final int SECONDARY = -1;

    private static final String SWITCH_LOG = "Switch to: ";
    
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

            if (BuildConfig.DEBUG) {
                Log.d(getTag(), SWITCH_LOG + PRIMARY);
            }
        }

        if (mSecondaryCount >= mSecondaryTotal && !mSecondaryReached) {
            mSecondaryReached = true;
            mListener.onSwitch(SECONDARY);

            if (BuildConfig.DEBUG) {
                Log.d(getTag(), SWITCH_LOG + SECONDARY);
            }
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
    public Staff load(Object... objects) {
        mCurrent = (int) objects[0];
        if (objects.length >= 3) {
            mPrimaryCount = (int) objects[1];
            mSecondaryCount = (int) objects[2];
        }
        return this;
    }
}
