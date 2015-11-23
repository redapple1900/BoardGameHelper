package com.yuanwei.resistance.model.protocol;

/**
 * Created by chenyuanwei on 15/10/20.
 */
public interface PlotHost {
    <T extends PlotListener> T getPlotListener();

    <T extends PlotListener> void setPlotListener(T listener);
}
