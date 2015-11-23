package com.yuanwei.resistance.ui.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.yuanwei.resistance.model.protocol.PlotHost;
import com.yuanwei.resistance.model.protocol.PlotListener;

/**
 * Created by chenyuanwei on 15/11/1.
 *
 * MultiSceneFragment should handle multiples views by using fragments.
 */
public abstract class BaseMultiSceneFragment extends BaseTextToSpeechFragment
        implements PlotListener {

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            ((PlotHost) activity).setPlotListener(this);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement GameEventListener");
        }
    }

    @Override
    public void onDetach() {

        try {
            ((PlotHost) getActivity()).setPlotListener(null);
        } catch (ClassCastException e) {
            // Will not happen
        }
        super.onDetach();
    }

    protected void showFragment(String tag) {

        FragmentTransaction transaction =
                getActivity().getSupportFragmentManager().beginTransaction();

        Fragment fragment = getFragmentManager().findFragmentByTag(tag);

        if (fragment == null) {
            fragment = createFragment(tag);
            transaction.add(getContainer(), fragment, tag);
        } else {
            transaction.replace(getContainer(), fragment, tag);
        }

        transaction
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);

        transaction.commit();
    }

    protected void hideFragment(String tag) {

        FragmentTransaction transaction =
                getActivity().getSupportFragmentManager().beginTransaction();

        Fragment fragment = getFragmentManager().findFragmentByTag(tag);

        if (fragment == null) return;

        transaction.remove(fragment).commit();
    }

    protected abstract Fragment createFragment(String tag);

    protected abstract int getContainer();
}
