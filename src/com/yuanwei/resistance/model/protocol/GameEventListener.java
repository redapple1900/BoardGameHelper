package com.yuanwei.resistance.model.protocol;

/**
 * Created by chenyuanwei on 15/7/12.
 */
public interface GameEventListener {
    public void onEventStart(String type, int extra);
}
