package com.yuanwei.resistance;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yuanwei.resistance.model.protocol.GameEventListener;
import com.yuanwei.resistance.widget.ButtonOnTouchListener;

import java.util.Random;

public class MissionFragment extends Fragment implements OnClickListener {
    public static final String TAG = "MissionFragment";
    private GameEventListener mCallback;
    private View excuteLayout;
    private View sabotageLayout;
    private ImageView image;
    private int identity;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Random random = new Random(System.currentTimeMillis());
        View v;
        int j = random.nextInt(2);
        switch (j) {
            case 0:
                if (isTablet(getActivity())) {
                    v = inflater.inflate(R.layout.fragment_mission_large, container, false);
                } else
                    v = inflater.inflate(R.layout.fragment_mission, container, false);
                break;
            case 1:
                if (isTablet(getActivity())) {
                    v = inflater.inflate(R.layout.fragment_mission2_large, container, false);
                } else v = inflater.inflate(R.layout.fragment_mission2, container, false);
                break;
            default:
                v = inflater.inflate(R.layout.fragment_mission, container, false);
                break;
        }

        excuteLayout = v.findViewById(R.id.layout_excute_mission);
        sabotageLayout = v.findViewById(R.id.layout_sabotage_mission);
        image = (ImageView) v.findViewById(R.id.imageView_mission);
        image.setImageResource(R.drawable.excution1);
        identity = getArguments().getInt("identity");
        excuteLayout.setOnClickListener(this);
        excuteLayout.setOnTouchListener(new ButtonOnTouchListener(getActivity()));
        sabotageLayout.setOnClickListener(this);
        sabotageLayout.setOnTouchListener(new ButtonOnTouchListener(getActivity()));
        if (identity == DataSet.SOILDER) {
            sabotageLayout.setVisibility(View.INVISIBLE);
        }
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_excute_mission:
                mCallback.onEventStart(TAG, 1);
                break;
            case R.id.layout_sabotage_mission:
                if (identity == DataSet.SPY)
                    mCallback.onEventStart(TAG, -1);
                break;
        }
    }

    private  boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

}
