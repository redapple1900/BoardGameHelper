package com.yuanwei.resistance.ui;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;

/**
 * Created by chenyuanwei on 15/12/10.
 */
public class DrawableUtils {

    public static Drawable getDrawable(
            Resources resources,
            @DrawableRes int resId,
            @DimenRes int dimenID) {
        Drawable drawable = resources.getDrawable(resId);
        int dimen = (int) resources.getDimension(dimenID);
        drawable.setBounds(0, 0, dimen, dimen);
        return drawable;
    }
}
