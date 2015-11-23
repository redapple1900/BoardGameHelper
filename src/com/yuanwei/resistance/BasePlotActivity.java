package com.yuanwei.resistance;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.yuanwei.resistance.model.protocol.PlotHost;
import com.yuanwei.resistance.model.protocol.PlotListener;
import com.yuanwei.resistance.util.GeneralMethodSet;

/**
 * Created by chenyuanwei on 15/10/23.
 *
 */
public abstract class BasePlotActivity extends FragmentActivity implements PlotHost{

    private PlotListener mPlotListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GeneralMethodSet gms = new GeneralMethodSet();
        gms.updateLanguage(this);
        gms.setActivityTheme(this);
    }

    @Override
    public PlotListener getPlotListener() {
        return mPlotListener;
    }

    @Override
    public  void setPlotListener(PlotListener listener) {
        mPlotListener = listener;
    }
}
