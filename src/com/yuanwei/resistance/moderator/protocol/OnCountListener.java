package com.yuanwei.resistance.moderator.protocol;

/**
 * Created by chenyuanwei on 15/10/25.
 */
public interface OnCountListener extends OnActListener{
    void onCountChange(int before, int after);
}
