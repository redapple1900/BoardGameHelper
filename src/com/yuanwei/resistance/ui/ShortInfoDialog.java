package com.yuanwei.resistance.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import com.yuanwei.resistance.R;

;

/**
 * Created by chenyuanwei on 15/12/10.
 */
public class ShortInfoDialog extends Dialog {

    public ShortInfoDialog(Context context) {
        super(context);
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_regular_view);

        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }
}
