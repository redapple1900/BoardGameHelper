package com.yuanwei.resistance.moderator;

import com.yuanwei.resistance.moderator.protocol.OnCountListener;
import com.yuanwei.resistance.moderator.protocol.Recordable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenyuanwei on 15/10/25.
 */
public class Selector<T> extends Scorer implements Recordable<T>{

    private List<T> mList;

    private int total;

    public Selector(OnCountListener listener) {
        super(listener);
    }

    @Override
    public final Selector<T> prepare(int size) {
        super.prepare(size);
        mList = new ArrayList<>(size);
        total = size;
        return this;
    }

    @Override
    public void terminate() {
        mList = null;
        total = 0;
        super.terminate();
    }

    @Override
    public List<T> provide() {
        return mList;
    }


    public final void select(T t) {
        if (t == null) return;

        if (mList.contains(t)){
            mList.remove(t);
            super.decrement();
        } else {
            mList.add(t);
            super.increment();
        }
    }

    public int getTotal() {
        return total;
    }
}
