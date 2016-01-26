package com.yuanwei.resistance.moderator;

import android.util.Log;

import com.yuanwei.resistance.BuildConfig;
import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.moderator.protocol.OnActListener;

import java.util.List;

/**
 * Created by chenyuanwei on 15/11/4.
 */
public class Rotater extends Staff {

    private static final String ROTATE_START_LOG = "Rotate Start: ";

    private static final String ROTATE_COMPLETED_LOG = "Rotate Complete: ";

    protected int mTotal;

    protected int mCount;

    protected List<User> mList;

    protected OnRotateListener mListener;

    public Rotater(OnRotateListener listener) {
        super(listener);
        this.mListener = listener;
    }

    public final Rotater prepare(int total) {
        mCount = 0;
        mTotal = total;
        return this;
    }

    public final Rotater prepare(List<User> list) {
        prepare(list.size());
        mList = list;
        return this;
    }

    public void rotate() {
        if (mCount == mTotal) {
            mCount = 0;
            return;
        }
        mListener.onRoundStart();
        if (BuildConfig.DEBUG) {
            Log.d(getTag(), ROTATE_START_LOG + mCount);
        }
        mCount ++;
        mCount %= mTotal;
        mListener.onRoundComplete();

        if (BuildConfig.DEBUG) {
            Log.d(getTag(), ROTATE_COMPLETED_LOG + mCount);
        }
    }

    public int getCount() {
        return mCount == mTotal ? 0 : mCount;
    }

    public User getItem(int index) {
        if (mList == null)
            return null;

        if (index == mList.size())
            return mList.get(0);

        return mList.get(index);
    }

    @Override
    public Staff load(Object... objects) {
        super.load(objects);
        mCount = (int) objects[0];
        return this;
    }

    public interface OnRotateListener extends OnActListener{
        void onRoundStart();
        void onRoundComplete();
    }
}
