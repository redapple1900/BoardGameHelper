package com.yuanwei.resistance.moderator;

import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.moderator.protocol.OnActListener;

import java.util.List;

/**
 * Created by chenyuanwei on 15/11/4.
 */
public class Rotater extends Staff {

    // TODO: persistance
    private static final String PREF_KEY = "com.yuanwei.resistance.rotator";

    protected static final String TOTAL_COUNT_KEY = "total_count";

    protected static final String CURRENT_COUNT_KEY = "current_count";

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
        mCount ++;
        mCount %= mTotal;
        mListener.onRoundComplete();
    }

    public int getCount() {
        return mCount;
    }

    public User getItem(int index) {
        if (mList == null)
            return null;

        if (index == mList.size())
            return mList.get(0);

        return mList.get(index);
    }

    /* TODO: add advanced api
    public void add (int index, Gamer gamer) {
        mTotal ++;
    }
    public void remove (int index, Gamer gamer) {

    }
    public void swap
    */
    public interface OnRotateListener extends OnActListener{
        void onRoundStart();
        void onRoundComplete();
    }
}
