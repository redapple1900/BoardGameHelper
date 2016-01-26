package com.yuanwei.resistance.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.yuanwei.resistance.BuildConfig;
import com.yuanwei.resistance.GameActivity;
import com.yuanwei.resistance.QuickPrefsActivity;
import com.yuanwei.resistance.R;
import com.yuanwei.resistance.constant.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class QuickStartFragment extends Fragment {
    @Bind(R.id.quickstart)
    Button mStartButton;
    @Bind(R.id.resume_quickstart)
    Button mResumeButton;
    @Bind(R.id.setting_quickstart)
    Button mSettingButton;
    private int mGame;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quickstart, container, false);

        ButterKnife.bind(this, view);

        String[] gameNames = {getString(R.string.resistance), getString(R.string.avalon)};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.myspinner, gameNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_quickstart);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                mGame = arg2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

                mGame = Constants.AVALON;
            }
        });
        spinner.setSelection(1);

        mStartButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                QuickStartTransitionDialogFragment fragment =
                        new QuickStartTransitionDialogFragment();

                Bundle bundle = new Bundle();

                bundle.putInt(Constants.GAME, mGame);

                fragment.setArguments(bundle);

                fragment.show(
                        getActivity().getSupportFragmentManager(),
                        QuickStartTransitionDialogFragment.TAG);
            }
        });

        mResumeButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                SharedPreferences prefs =
                        PreferenceManager.getDefaultSharedPreferences(getActivity());
                String result = prefs.getString(Constants.BOOKKEEPER_KEY, "");
                if (BuildConfig.DEBUG) Log.d("Resistance: tag", result);
                if (!"".equals(prefs.getString(Constants.BOOKKEEPER_KEY, ""))) {
                    Intent intent = new Intent();
                    intent.putExtras(new Bundle());
                    intent.setClass(getActivity(), GameActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

        mSettingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), QuickPrefsActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }
}