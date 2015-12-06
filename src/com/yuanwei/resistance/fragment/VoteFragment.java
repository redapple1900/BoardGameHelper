package com.yuanwei.resistance.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yuanwei.resistance.R;
import com.yuanwei.resistance.moderator.BaseSwitcher;
import com.yuanwei.resistance.ui.fragment.BasePlotFragment;
import com.yuanwei.resistance.ui.widget.ButtonOnTouchListener;

public class VoteFragment extends BasePlotFragment {

    private String TAG;

    public static VoteFragment createInstance(String name) {
        VoteFragment fragment = new VoteFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TAG = getArguments().getString("name");

        View v = inflater.inflate(R.layout.fragment_mission, container, false);

        Button button_negative = (Button) v.findViewById(R.id.secondary);
        Button button_positive = (Button) v.findViewById(R.id.primary);

        button_negative.setBackgroundResource(R.drawable.vote_nay);
        button_positive.setBackgroundResource(R.drawable.vote_aye);

        button_negative.setText(getString(R.string.string_vote_nay));
        button_positive.setText(getString(R.string.string_vote_aye));

        button_negative.setOnTouchListener(new ButtonOnTouchListener(getActivity()));
        button_positive.setOnTouchListener(new ButtonOnTouchListener(getActivity()));

        button_negative.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPlotListener().onEventStart(TAG, BaseSwitcher.SECONDARY);
            }
        });
        button_positive.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPlotListener().onEventStart(TAG, BaseSwitcher.PRIMARY);
            }
        });
        return v;
    }

}
