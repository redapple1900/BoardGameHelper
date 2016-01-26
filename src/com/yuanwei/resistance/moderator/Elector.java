package com.yuanwei.resistance.moderator;

import com.yuanwei.resistance.moderator.protocol.OnSwitchListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenyuanwei on 15/10/28.
 */
public class Elector extends BaseSwitcher{

    private List<Integer> mList;

    public Elector(OnSwitchListener listener) {
        super(listener);
    }

    @Override
    public Staff load(Object... objects) {
        super.load(objects);
        if (objects.length >= 4 && objects[3] instanceof List) {
            mList = (List<Integer>) objects[3];
        }
        return this;
    }

    @Override
    public Elector prepare(int total, int primary, int secondary) {
        super.prepare(total, primary, secondary);
        mList = new ArrayList<>(total);
        return this;
    }

    public List<Integer> provide() {
        return mList;
    }

    public final void vote(int side) {

        mList.add(side);

        super.incrementInternal(side, 1);
    }

    public Integer getLastItem(){
        return mList.get(getCount() -1);
    }

    // TODO:: add vote (T t) method
}
