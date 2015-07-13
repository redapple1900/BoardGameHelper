package com.yuanwei.resistance.model.protocol;


/**
 * Created by chenyuanwei on 15/7/12.
 */
public interface GamePresenter{
   public void updateViewBeforeEvent(Event event, int extra);
   public void updateViewAfterEvent(Event event);
}
