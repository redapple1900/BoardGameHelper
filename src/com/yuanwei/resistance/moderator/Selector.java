package com.yuanwei.resistance.moderator;

import com.yuanwei.resistance.moderator.protocol.OnCountListener;
import com.yuanwei.resistance.moderator.protocol.Recordable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by chenyuanwei on 15/10/25.
 */
public class Selector<T> extends Scorer implements Recordable<T>{

    private Set<T> mSet;

    private int total;

    public Selector(OnCountListener listener) {
        super(listener);
    }

    @Override
    public final Selector<T> prepare(int size) {
        super.prepare(size);
        mSet = new HashSet<>(size);
        total = size;
        return this;
    }

    @Override
    public void terminate() {
        mSet = null;
        total = 0;
        super.terminate();
    }

    @Override
    public List<T> provide() {
        return new ArrayList<>(mSet);
    }


    public final void select(T t) {
        if (t == null) return;

        if (mSet.contains(t)){
            mSet.remove(t);
            super.decrement();
        } else {
            mSet.add(t);
            super.increment();
        }
    }

    public int getTotal() {
        return total;
    }
}
