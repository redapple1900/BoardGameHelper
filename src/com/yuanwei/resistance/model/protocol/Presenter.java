package com.yuanwei.resistance.model.protocol;


import com.yuanwei.resistance.model.BaseGameEvent;

/**
 * Created by chenyuanwei on 15/7/12.
 *
 * It is a duplicate class with ##EventListener##
 */
public interface Presenter {
   void updateViewBeforeEvent(BaseGameEvent event, int extra);
   void updateViewAfterEvent(BaseGameEvent event);
}
