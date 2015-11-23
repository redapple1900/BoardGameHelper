package com.yuanwei.resistance.ui;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.yuanwei.resistance.R;

/**
 * Created by chenyuanwei on 15/11/5.
 */
public class DoubleButtonView extends LinearLayout {

    private Button primary, secondary;
    private View primary_layout, secondary_layout;

    public DoubleButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public DoubleButtonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DoubleButtonView(Context context) {
        this(context, null);
    }

    private void init(Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.view_double_button, this);

        primary = (Button) view.findViewById(R.id.primary_button);
        secondary = (Button) view.findViewById(R.id.secondary_button);

        primary_layout = view.findViewById(R.id.primary_layout);
        secondary_layout = view.findViewById(R.id.secondary_layout);
    }

    public void setButtonEnabled(boolean primary, boolean secondary) {
        this.primary.setEnabled(primary);
        this.secondary.setEnabled(secondary);
    }

    public void setButtonVisibility(int primary, int secondary) {
        this.primary_layout.setVisibility(primary);
        this.secondary_layout.setVisibility(secondary);
    }

    public void setButtonImage(@DrawableRes int primary, @DrawableRes int secondary) {
        this.primary.setCompoundDrawablesWithIntrinsicBounds(0, primary, 0, 0);
        this.secondary.setCompoundDrawablesWithIntrinsicBounds(0, secondary, 0, 0);
    }

    public void setButtonText(@StringRes int primary, @StringRes int secondary) {
        this.primary.setText(primary);
        this.secondary.setText(secondary);
    }

    public void setButtonOnClickListener(
            Button.OnClickListener listener0,
            Button.OnClickListener listener1) {
        this.primary.setOnClickListener(listener0);
        this.secondary.setOnClickListener(listener1);
    }

    public void setButtonOnTouchListener(
            Button.OnTouchListener listener0,
            Button.OnTouchListener listener1) {
        this.primary.setOnTouchListener(listener0);
        this.secondary.setOnTouchListener(listener1);
    }
}
