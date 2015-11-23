package com.yuanwei.resistance.moderator;

import com.yuanwei.resistance.moderator.protocol.OnActListener;

import org.json.JSONObject;

/**
 * Created by chenyuanwei on 15/10/27.
 */
public class Staff extends BaseStaff {

    private static final String PREF_KEY = "com.yuanwei.resistance.secretary";

    private OnActListener mListener;

    protected Director mDirector;

    public Staff(OnActListener listener) {
        mListener = listener;
    }

    public Staff report(Director director) {
        director.register(this);
        mDirector = director;
        return this;
    }

    public void initiate() {
        mListener.onInitiate();

        if (mDirector != null)
            mDirector.enroll(this);
    }

    public void complete() {
        mListener.onComplete();
        terminate();
    }

    public void terminate() {
        if (mDirector != null)
            mDirector.dismiss(this);
    }

    @Override
    protected void saveToJSONObj(JSONObject obj) {
        // Do nothing
    }

    @Override
    protected void loadFromJSONObj(JSONObject obj) {
        // Do nothing
    }

    @Override
    protected String getInternalTag() {
        if (getTag() != null)
            return PREF_KEY;
        else
            return PREF_KEY + getTag();
    }
}
