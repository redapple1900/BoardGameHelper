package com.yuanwei.resistance.ui.fragment;

import android.app.Activity;
import android.support.v4.app.DialogFragment;

import com.yuanwei.resistance.model.protocol.PlotHost;
import com.yuanwei.resistance.model.protocol.PlotListener;

/**
 * Created by chenyuanwei on 15/10/23.
 */
public class BasePlotDialogFragment extends DialogFragment {

    protected PlotListener mPlotListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mPlotListener = ((PlotHost) activity).getPlotListener();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement GameEventListener");
        }
    }

    @Override
    public void onDetach() {
        mPlotListener = null;
        super.onDetach();
    }

    protected PlotListener getPlotListener() {
        return mPlotListener;
    }
}
