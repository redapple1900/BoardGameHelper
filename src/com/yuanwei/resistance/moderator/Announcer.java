package com.yuanwei.resistance.moderator;

import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.moderator.protocol.OnAnnounceListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenyuanwei on 15/10/22.
 *
 *
 */
public class Announcer extends Staff {

    private static final String PREF_KEY = "com.yuanwei.resistance.announcer";

    protected static final String CURRENT_STEP_KEY = "current_step";

    protected static final String TOTAL_COUNT_KEY = "total_count";

    protected static final String CURRENT_COUNT_KEY = "current_count";

    protected static final Step[] STEPS = {Step.START, Step.INTRODUCE, Step.DONE};

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
            mListener.onIntroduceStart();
        } else if (mCurrentStep == Step.INTRODUCE) {
            mCurrentStep = Step.DONE;
            mListener.onIntroduce();
        } else if (mCurrentStep == Step.DONE) {
            mCurrentStep = Step.START;
            mCount ++;
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

    //////////////////////////////////////////////////////////////////////////
    // Serialization

    @Override
    protected String getInternalTag() {
        if (getTag() != null)
            return PREF_KEY;
        else
            return PREF_KEY + getTag();
    }

    @Override
    protected void saveToJSONObj(JSONObject obj) {
        super.saveToJSONObj(obj);
        try {
            obj.put(TOTAL_COUNT_KEY, mTotal);
            obj.put(CURRENT_COUNT_KEY, mCount);
            obj.put(CURRENT_STEP_KEY, mCurrentStep.ordinal());
        } catch (JSONException e) {
            // Nothing going to happen
        }
    }

    @Override
    protected void loadFromJSONObj(JSONObject obj) {
        super.loadFromJSONObj(obj);

        mTotal = obj.optInt(TOTAL_COUNT_KEY);

        mCount = obj.optInt(CURRENT_COUNT_KEY);

        mCurrentStep = STEPS[obj.optInt(CURRENT_STEP_KEY)];
    }

    private enum Step{
        START,
        INTRODUCE,
        DONE
    }
}
