package com.yuanwei.resistance;


import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;

import com.yuanwei.resistance.model.Gamer;

import java.util.ArrayList;

public class QuickStartFragment extends Fragment {
    private static int TOTAL_PLAYERS;
    private TextView tv;
    private Button button, button_custom, button_setting;
    private Spinner spinner;
    private static final String[] NumOfGamers = {"5", "6", "7", "8", "9", "10"};
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View quickstartLayout = inflater.inflate(R.layout.fragment_quickstart, container, false);
        button = (Button) quickstartLayout.findViewById(R.id.button_quickstart);
        button_custom = (Button) quickstartLayout.findViewById(R.id.button_customgame_quickstart);
        button_setting = (Button) quickstartLayout.findViewById(R.id.button_setting_quickstart);
        tv = (TextView) quickstartLayout.findViewById(R.id.textView2_quickstart);
        spinner = (Spinner) quickstartLayout.findViewById(R.id.spinner_quickstart);
        adapter = new ArrayAdapter<>(getActivity(), R.layout.myspinner, NumOfGamers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setBackgroundColor(Color.WHITE);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                TOTAL_PLAYERS = Integer.parseInt(NumOfGamers[arg2]);
                arg0.setVisibility(View.VISIBLE);
                tv.setText(getString(R.string.string_eachsides_quickstart, DataSet.getNormalPlayers(TOTAL_PLAYERS), DataSet.getSpyPlayers(TOTAL_PLAYERS)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

                TOTAL_PLAYERS = 5;
            }
        });
        button.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                ArrayList<Gamer> gamers = new ArrayList<>(TOTAL_PLAYERS);

                for (int i = 0; i < TOTAL_PLAYERS; i++) {
                    gamers.add(new Gamer());
                }
                bundle.putParcelableArrayList("gamerList", gamers);

                intent.setClass(getActivity(), MainActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                getActivity().finish();
                onDestroy();
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
                //intent.setClass(getActivity(), QuickPrefsActivity.class);
                intent.setClass(getActivity(), GameSetupActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return quickstartLayout;
    }
}