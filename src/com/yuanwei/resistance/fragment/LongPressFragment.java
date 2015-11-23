package com.yuanwei.resistance.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yuanwei.resistance.R;
import com.yuanwei.resistance.ui.fragment.BasePlotFragment;
import com.yuanwei.resistance.ui.widget.TiltEffectAttacher;

public class LongPressFragment extends BasePlotFragment {
    public static final String TAG = "press";

    private String tag;

    public static LongPressFragment createInstance(String tag, String name) {
        LongPressFragment fragment = new LongPressFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tag", tag);
        bundle.putString("name", name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_execution, container, false);

        tag = getArguments().getString("tag");

        String name = getArguments().getString("name");

        TextView textview = (TextView) view.findViewById(R.id.textview_Execution);

        if (name != null && !name.isEmpty()) {
            textview.setText(name);
            textview.setTextColor(Color.WHITE);
        }

        ImageButton button = (ImageButton) view.findViewById(R.id.imageButton_Execution);
        button.setLongClickable(true);
        button.setOnLongClickListener(new Button.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                getPlotListener().onEventStart(tag, 0);
                return true;
            }
        });

        TiltEffectAttacher.attach(button);

        return view;
    }
}

