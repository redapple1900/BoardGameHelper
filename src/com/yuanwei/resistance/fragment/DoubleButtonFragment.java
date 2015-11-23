package com.yuanwei.resistance.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yuanwei.resistance.R;
import com.yuanwei.resistance.ui.DoubleButtonView;
import com.yuanwei.resistance.ui.fragment.BasePlotFragment;
import com.yuanwei.resistance.ui.widget.ButtonOnTouchListener;

/**
 * Created by chenyuanwei on 15/10/11.
 */
public class DoubleButtonFragment extends BasePlotFragment {

    public static final String KEY = DoubleButtonFragment.class.getName();
    public static final int PRIMARY = 0;
    public static final int SECONDARY = 1;

    private String TAG;

    private ButtonOnTouchListener mOnTouchListener;

    private DoubleButtonView doubleButtonView;

    public static DoubleButtonFragment createInstance(String tag) {
        DoubleButtonFragment fragment = new DoubleButtonFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY, tag);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getArguments().getString(KEY);
        mOnTouchListener = new ButtonOnTouchListener(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_double_button, container, false);

        doubleButtonView = (DoubleButtonView) view.findViewById(R.id.buttons);

        doubleButtonView.setButtonOnTouchListener(mOnTouchListener, mOnTouchListener);

        doubleButtonView.setButtonImage(R.drawable.next, R.drawable.play);

        doubleButtonView.setButtonText(R.string.button_main, R.string.playsound);

        doubleButtonView.setButtonOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                getPlotListener().onEventStart(TAG, PRIMARY);
            }

        }, new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                getPlotListener().onEventStart(TAG, SECONDARY);
            }
        });

        return view;
    }

    public DoubleButtonView getDoubleButtonView() {
        return doubleButtonView;
    }
}