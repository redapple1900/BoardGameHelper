package com.yuanwei.resistance.moderator;

import com.yuanwei.resistance.moderator.protocol.OnCountListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chenyuanwei on 15/10/25.
 */
public class Scorer extends Staff {

    private static final String PREF_KEY = "com.yuanwei.resistance.accountant";

    private static final String TOTAL_COUNT_KEY = "total_count";

    private static final String CURRENT_COUNT_KEY = "current_count";

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

        mListener.onCountChange(mCurrentCount, mCurrentCount + num);

        mCurrentCount += num;
    }

    public final void decrement(int num) {
        if (mCurrentCount <= 0) {
            return;
        }

        mListener.onCountChange(mCurrentCount, mCurrentCount - num);

        mCurrentCount -= num;
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
    protected String getInternalTag() {
        if (getTag() != null)
            return PREF_KEY;
        else
            return PREF_KEY + getTag();
    }

    @Override
    protected void saveToJSONObj(JSONObject obj) {

        try {
            obj.put(TOTAL_COUNT_KEY, mTotalCount);

            obj.put(CURRENT_COUNT_KEY, mCurrentCount);
        } catch (JSONException e) {
            // Nothing going to happen
        }
    }

    @Override
    protected void loadFromJSONObj(JSONObject obj) {

        mTotalCount = obj.optInt(TOTAL_COUNT_KEY);

        mCurrentCount = obj.optInt(CURRENT_COUNT_KEY);
    }
}
