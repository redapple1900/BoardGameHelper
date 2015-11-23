package com.yuanwei.resistance.model.protocol;

import android.content.Context;

/**
 * Created by chenyuanwei on 15/10/20.
 */
public abstract class BaseConfig {

    protected int mCount;

    public void setNumberOfPlayers(int count) {
        mCount = count;
    }

    public int getNumberOfPlayers() {
        return mCount;
    }

    public abstract void save(Context context);

    public abstract void load(Context context);
}
