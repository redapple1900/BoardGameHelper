package com.yuanwei.resistance;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yuanwei.resistance.model.protocol.GameEventListener;
import com.yuanwei.resistance.widget.TiltEffectAttacher;

public class ExecutionFragment extends Fragment {
    public static final String TAG = "ExecutionFragment";
    private GameEventListener mCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (GameEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement GameEventListener");
        }
    }

    private ImageButton button;
    private TextView textview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View excutionLayout = inflater.inflate(R.layout.fragment_excution, container, false);

        textview = (TextView) excutionLayout.findViewById(R.id.textview_Execution);
        textview.setText(getArguments().getString("name"));
        textview.setTextColor(Color.WHITE);
        button = (ImageButton) excutionLayout.findViewById(R.id.imageButton_Execution);
        button.setLongClickable(true);
        TiltEffectAttacher.attach(button);
        button.setOnLongClickListener(new Button.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mCallback.onEventStart(TAG, 0);
                return true;
            }
        });
        return excutionLayout;
    }
}

