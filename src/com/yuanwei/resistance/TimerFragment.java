package com.yuanwei.resistance;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

import com.yuanwei.resistance.model.protocol.GameEventListener;

public class TimerFragment extends Fragment {
    public static final String TAG = "TimerFragment";
    private GameEventListener mCallback;
    private Button button;
    private Chronometer chronometer;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (GameEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnCheckResultListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timer, container, false);
        button = (Button) v.findViewById(R.id.button_timer);
        chronometer = (Chronometer) v.findViewById(R.id.chronometer);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
                mCallback.onEventStart(TAG, 0);
            }
        });
        chronometer.start();
        return v;
    }
}
