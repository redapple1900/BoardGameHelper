package com.yuanwei.resistance.model.protocol;


import com.yuanwei.resistance.model.BaseGameEvent;

/**
 * Created by chenyuanwei on 15/7/12.
 */
public interface GamePresenter{
   public void updateViewBeforeEvent(BaseGameEvent event, int extra);
   public void updateViewAfterEvent(BaseGameEvent event);
}
