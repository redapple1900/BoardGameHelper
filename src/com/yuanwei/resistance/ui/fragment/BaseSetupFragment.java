package com.yuanwei.resistance.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yuanwei.resistance.PrepareActivity;
import com.yuanwei.resistance.R;
import com.yuanwei.resistance.constant.Constants;
import com.yuanwei.resistance.fragment.NumberPickDialogFragment;
import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.model.protocol.PlotHost;
import com.yuanwei.resistance.model.protocol.PlotListener;
import com.yuanwei.resistance.partygame.avalon.Config;
import com.yuanwei.resistance.partygame.origin.model.Resistance;

import java.util.ArrayList;

/**
 * Created by chenyuanwei on 15/10/21.
 */
public abstract class BaseSetupFragment extends BaseTextToSpeechFragment
        implements PlotListener{

    public abstract int getGameId();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            ((PlotHost) activity).setPlotListener(this);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement GameEventListener");
        }
        mActivity = (FragmentActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setup, container, false);

        initData();
        initNarrator();
        initRecyclerView(v); // Must be after initData();
        initTopView(v);
        initInteraction(v); // Must be called last

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initAdapter();
    }

    @Override
    public void onDetach() {

        try {
            ((PlotHost) mActivity).setPlotListener(null);
        } catch (ClassCastException e) {
            // Will not happen
        }
        super.onDetach();
    }

    @Override
    protected void onSpeakDone() {
        mButtonNext.setClickable(true);
    }

    @Override
    protected void onSpeakStart() {
        mButtonNext.setClickable(false);
    }

    @Override
    protected void onSpeakError() {
        Toast.makeText(
                getActivity(),
                getString(R.string.string_playsound_toast),
                Toast.LENGTH_LONG).show();
    }

    /* Data */
    protected boolean isGameNeeded;
    protected int mNumberOfPlayers;

    /*View */
    protected Button mButtonTotal, mButtonNext;
    protected RecyclerView mRecyclerView;
    protected TextView tv;

    /* Utility */
    protected LinearLayoutManager mLayoutManager;
    protected FragmentActivity mActivity;

    protected void initRecyclerView(View v) {
        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // END_INCLUDE(initializeRecyclerView)
    }

    protected void initTopView(View v) {
        tv = (TextView) v.findViewById(R.id.setup_spy);
        String string = getGameId() == Constants.ORIGIN ?
                getString(R.string.game_setup_button_total_spy,
                        Constants.getSpyPlayers(mNumberOfPlayers)) :
                getString(R.string.game_setup_button_total_spy_avalon,
                        Constants.getSpyPlayers(mNumberOfPlayers));
        tv.setText(string);
    }

    protected void initInteraction(View v) {
        mButtonNext = (Button) v.findViewById(R.id.button_next_setup);

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveConfig();

                if (isGameNeeded) {
                    onEventStart(Constants.GAME_WITHOUT_CARDS, 0);
                } else if (getGameId() == Constants.ORIGIN &&
                        getConfig().isOptionEnabled(Resistance.Option.BLIND_SPY)) {
                    Toast.makeText(
                            getActivity(),
                            getString(R.string.enjoy_game),
                            Toast.LENGTH_SHORT).show();
                } else {
                    playSound();
                }
            }
        });

        mButtonTotal = (Button) v.findViewById(R.id.setup_total);
        mButtonTotal.setText(getString(R.string.game_setup_button_total, mNumberOfPlayers));
        mButtonTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new NumberPickDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.TOTAL_PLAYERS_KEY, mNumberOfPlayers);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(mActivity.getSupportFragmentManager(), "numberPicker");
            }
        });
    }

    protected void updateTopView() {
        mButtonTotal.setText(getString(R.string.game_setup_button_total, mNumberOfPlayers));
        String string = getGameId() == Constants.ORIGIN ?
                getString(R.string.game_setup_button_total_spy,
                        Constants.getSpyPlayers(mNumberOfPlayers)) :
                getString(R.string.game_setup_button_total_spy_avalon,
                        Constants.getSpyPlayers(mNumberOfPlayers));
        tv.setText(string);
    }

    protected void startNextActivity() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("gamerList", assignIdentity());
        bundle.putInt(Constants.GAME, getGameId());
        intent.putExtras(bundle);
        intent.setClass(getActivity(), PrepareActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    protected abstract void initNarrator();

    protected abstract void initData();

    protected abstract void initAdapter();

    protected abstract ArrayList<User> assignIdentity();

    protected abstract Config getConfig();

    protected abstract void saveConfig();
}
