package com.yuanwei.resistance.moderator.protocol;

import java.util.List;

/**
 * Created by chenyuanwei on 15/10/28.
 */
public interface Recordable<T> {
    List<T> provide();
}
