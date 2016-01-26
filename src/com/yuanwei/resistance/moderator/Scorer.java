package com.yuanwei.resistance.moderator;

import android.util.Log;

import com.yuanwei.resistance.BuildConfig;
import com.yuanwei.resistance.moderator.protocol.OnCountListener;

/**
 * Created by chenyuanwei on 15/10/25.
 */
public class Scorer extends Staff {

    private static final String COUNT_LOG = "Coungt Changed from: ";

    private static final String TO_LOG = " to ";

    private OnCountListener mListener;

    private int mTotalCount;

    private int mCurrentCount;

    public Scorer(OnCountListener listener) {
        super(listener);
        mListener = listener;
    }

    public Scorer prepare(int num) {
        mTotalCount = num;
        mCurrentCount = 0;
        return this;
    }

    public final void increment(int num) {

        if (mCurrentCount >= mTotalCount) {
            return;
        }

        int after = mCurrentCount + num;
        mListener.onCountChange(mCurrentCount, after);

        if (BuildConfig.DEBUG) {
            Log.d(getTag(), COUNT_LOG + mCurrentCount + TO_LOG + after);
        }

        mCurrentCount = after;
    }

    public final void decrement(int num) {
        if (mCurrentCount <= 0) {
            return;
        }

        int after = mCurrentCount - num;
        mListener.onCountChange(mCurrentCount, after);

        if (BuildConfig.DEBUG) {
            Log.d(getTag(), COUNT_LOG + mCurrentCount + TO_LOG + after);
        }

        mCurrentCount = after;
    }

    public final void increment() {
        increment(1);
    }

    public final void decrement() {
        decrement(1);
    }

    public int getCount() {
        return mCurrentCount;
    }

    @Override
    public Staff load(Object... objects) {
        super.load(objects);
        mCurrentCount = (int) objects[0];
        return this;
    }
}
