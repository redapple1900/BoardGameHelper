package com.yuanwei.resistance;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yuanwei.resistance.model.protocol.GameEventListener;
import com.yuanwei.resistance.widget.ButtonOnTouchListener;

public class VoteFragment extends Fragment {
    public static final String TAG = "VoteFragment";
    private GameEventListener mCallback;
    private Button button_negative, button_positive;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (GameEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnPreMissionExcutionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_vote, container, false);
        button_negative = (Button) v.findViewById(R.id.button_nay_vote);
        button_positive = (Button) v.findViewById(R.id.button_aye_vote);
        button_negative.setOnTouchListener(new ButtonOnTouchListener(getActivity()));
        button_positive.setOnTouchListener(new ButtonOnTouchListener(getActivity()));
        button_negative.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCallback.onEventStart(TAG, 0);
            }
        });
        button_positive.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCallback.onEventStart(TAG, 1);
            }
        });
        return v;
    }

}
