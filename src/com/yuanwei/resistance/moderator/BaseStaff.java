package com.yuanwei.resistance.moderator;

/**
 * Created by chenyuanwei on 15/10/25.
 */
public abstract class BaseStaff {

    private static final String DebugTag = "Resistance:";

    private String Tag;

    public String getTag() {
        return DebugTag + Tag;
    }

    public BaseStaff setTag(String TAG) {
        this.Tag = TAG;
        return this;
    }
}
