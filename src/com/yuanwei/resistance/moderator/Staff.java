package com.yuanwei.resistance.moderator;

import android.util.Log;

import com.yuanwei.resistance.BuildConfig;
import com.yuanwei.resistance.moderator.protocol.OnActListener;

/**
 * Created by chenyuanwei on 15/10/27.
 */
public class Staff extends BaseStaff {

    private static final String INITIATED_LOG = "initiated";

    private static final String COMPLETED_LOG = "completed";

    private static final String TERMINATED_LOG = "terminated";

    private boolean isInitiated;

    private OnActListener mListener;

    public Staff(OnActListener listener) {
        mListener = listener;
    }

    public Staff load(Object... objects) {
        return this;
    }

    public void initiate() {
        if (BuildConfig.DEBUG) {
            Log.d(getTag(), INITIATED_LOG);
        }
        isInitiated = true;
        mListener.onInitiate();
    }

    public void complete() {
        mListener.onComplete();
        if (BuildConfig.DEBUG) {
            Log.d(getTag(), COMPLETED_LOG);
        }
        terminate(true);
    }

    public void terminate() {
        terminate(false);
    }

    public boolean isInitiated() {
        return isInitiated;
    }

    private void terminate(boolean isCompleted) {
        if (!isCompleted && BuildConfig.DEBUG) {
            Log.d(getTag(), TERMINATED_LOG);
        }
        isInitiated = false;
    }
}
