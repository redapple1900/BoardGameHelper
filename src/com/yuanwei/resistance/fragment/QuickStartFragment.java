package com.yuanwei.resistance.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.yuanwei.resistance.CustomGameActivity;
import com.yuanwei.resistance.QuickPrefsActivity;
import com.yuanwei.resistance.R;
import com.yuanwei.resistance.constant.Constants;
import com.yuanwei.resistance.partygame.avalon.model.Avalon;
import com.yuanwei.resistance.partygame.origin.model.Resistance;

public class QuickStartFragment extends Fragment {
    private int mGame;
    private Button button, button_custom, button_setting;
    private Spinner spinner;
    private static final String[] mGames = {
            Resistance.getInstance().getName(),
            Avalon.getInstance().getName()};
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View quickstartLayout = inflater.inflate(R.layout.fragment_quickstart, container, false);

        button = (Button) quickstartLayout.findViewById(R.id.button_quickstart);
        button_custom = (Button) quickstartLayout.findViewById(R.id.button_customgame_quickstart);
        button_setting = (Button) quickstartLayout.findViewById(R.id.button_setting_quickstart);

        adapter = new ArrayAdapter<>(getActivity(), R.layout.myspinner, mGames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = (Spinner) quickstartLayout.findViewById(R.id.spinner_quickstart);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                mGame = arg2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

                mGame = Constants.ORIGIN;
            }
        });

        button.setOnClickListener(new Button.OnClickListener() {

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

        button_custom.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(getActivity(), CustomGameActivity.class);
                startActivity(intent);
            }
        });

        button_setting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), QuickPrefsActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return quickstartLayout;
    }
}