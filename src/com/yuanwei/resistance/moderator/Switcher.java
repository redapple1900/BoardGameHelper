package com.yuanwei.resistance.moderator;

import com.yuanwei.resistance.moderator.protocol.OnSwitchListener;

/**
 * Created by chenyuanwei on 15/11/8.
 */
public class Switcher extends BaseSwitcher {

    public Switcher(OnSwitchListener listener) {
        super(listener);
    }

    public Switcher prepare(int total, int primary, int secondary) {
        super.prepare(total, primary, secondary);
        return this;
    }

    public final void increment(int side, int step) {
        incrementInternal(side, step);
    }

    public final void decrement(int side, int step) {
        decrementInternal(side, step);
    }

    public final void increment(int side) {
        increment(side, 1);
    }

    public final void decrement(int side) {
        decrement(side, 1);
    }
}
