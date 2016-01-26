package com.yuanwei.resistance.moderator;

import android.util.Log;

import com.yuanwei.resistance.BuildConfig;
import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.moderator.protocol.OnAnnounceListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenyuanwei on 15/10/22.
 *
 */
public class Announcer extends Staff {

    private static final String INTRODUCE_START_LOG = "introduce started";
    private static final String INTRODUCE_LOG = "introduced";
    private static final String INTRODUCE_COMPLETE_LOG = "introduce completed";

    protected int mTotal;

    protected int mCount;

    protected Step mCurrentStep;

    protected OnAnnounceListener mListener;

    protected List<User> mList;

    public Announcer(OnAnnounceListener listener) {
        super(listener);
        mListener = listener;
    }

    public final Announcer prepare(int total) {
        mCurrentStep = Step.START;
        mCount = 0;
        mTotal = total;
        return this;
    }

    public final Announcer prepare(List<User> list) {
        prepare(list.size());
        mList = new ArrayList<>(list);
        return this;
    }

    public void next() {
        if (mCount == mTotal) {
            complete();
            return;
        }

        if (mCurrentStep == Step.START) {
            mCurrentStep = Step.INTRODUCE;
            if (BuildConfig.DEBUG) Log.d(getTag(), INTRODUCE_START_LOG);
            mListener.onIntroduceStart();
        } else if (mCurrentStep == Step.INTRODUCE) {
            mCurrentStep = Step.DONE;
            if (BuildConfig.DEBUG) Log.d(getTag(), INTRODUCE_LOG);
            mListener.onIntroduce();
        } else if (mCurrentStep == Step.DONE) {
            mCurrentStep = Step.START;
            mCount ++;
            if (BuildConfig.DEBUG) Log.d(getTag(), INTRODUCE_COMPLETE_LOG);
            mListener.onIntroduceDone();
        }
    }

    public int getCount() {
        return mCount;
    }

    public int getLimit() {
        return mTotal;
    }

    public User getCurrentItem() {
        if (mList == null)
            return null;

        return mList.get(mCount);
    }

    private enum Step{
        START,
        INTRODUCE,
        DONE
    }
}
