package com.yuanwei.resistance.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.yuanwei.resistance.R;
import com.yuanwei.resistance.constant.Constants;
import com.yuanwei.resistance.ui.fragment.BasePlotFragment;

import java.util.Random;

public class MissionFragment extends BasePlotFragment implements OnClickListener {

    public static final String KEY = "missionfragment";

    public static final int SUCCEED = 1;

    public static final int FAILED = -1;

    private String Tag;

    public static MissionFragment createInstance (String tag, int identity) {
        MissionFragment fragment = new MissionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("identity", identity);
        bundle.putString(KEY, tag);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Button positive, negative;

        Tag = getArguments().getString(KEY);

        View v = inflater.inflate(R.layout.fragment_mission, container, false);

        int j = new Random(System.currentTimeMillis()).nextInt(2);

        if (j == 0) {
            positive = (Button) v.findViewById(R.id.primary);
            negative = (Button) v.findViewById(R.id.secondary);
        } else {
            negative = (Button) v.findViewById(R.id.primary);
            positive = (Button) v.findViewById(R.id.secondary);
        }

        positive.setTag("positive");
        positive.setOnClickListener(this);
        positive.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.token_win, 0, 0);
        positive.setText(R.string.execute);

        int identity = getArguments().getInt("identity");

        if (identity == Constants.RESISTANT) {
            negative.setVisibility(View.INVISIBLE);
        } else {
            negative.setTag("negative");
            negative.setOnClickListener(this);
            negative.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.token_fail, 0, 0);
            negative.setText(R.string.sabotage);
        }

        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("positive")) {
            getPlotListener().onEventStart(Tag, SUCCEED);
        } else if (v.getTag().equals("negative")){
            getPlotListener().onEventStart(Tag, FAILED);
        }
    }

    private  boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
