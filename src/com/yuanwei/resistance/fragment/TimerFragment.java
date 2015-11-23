package com.yuanwei.resistance.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

import com.yuanwei.resistance.R;
import com.yuanwei.resistance.ui.fragment.BasePlotFragment;

public class TimerFragment extends BasePlotFragment {
    private String TAG = "discussion";

    public static TimerFragment createInstance(String name) {
        TimerFragment fragment = new TimerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        TAG = getArguments().getString("name");

        View v = inflater.inflate(R.layout.fragment_timer, container, false);
        Button button = (Button) v.findViewById(R.id.button_timer);
        final Chronometer chronometer = (Chronometer) v.findViewById(R.id.chronometer);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
                getPlotListener().onEventStart(TAG, 0);
            }
        });
        chronometer.start();
        return v;
    }
}
