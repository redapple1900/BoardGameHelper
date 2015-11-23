package com.yuanwei.resistance.partygame.origin.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.yuanwei.resistance.R;
import com.yuanwei.resistance.constant.Constants;
import com.yuanwei.resistance.fragment.NumberPickDialogFragment;
import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.partygame.origin.Config;
import com.yuanwei.resistance.partygame.origin.model.Resistance;
import com.yuanwei.resistance.partygame.origin.texttospeech.Narrator;
import com.yuanwei.resistance.ui.fragment.BaseSetupFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenyuanwei on 15/10/19.
 */
public class ResistanceSetupFragment extends BaseSetupFragment {

    public static final String TAG = "ResistanceSetupFragment";

    public static ResistanceSetupFragment createInstance(boolean isGameNeeded) {
        ResistanceSetupFragment fragment = new ResistanceSetupFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("IsGameNeeded", isGameNeeded);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public int getGameId() {
        return Constants.ORIGIN;
    }

    @Override
    public void onEventStart(String type, int extra) {

        switch (type) {
            case NumberPickDialogFragment.CLEAR_SELECTION:
            case NumberPickDialogFragment.TAG:
                mNumberOfPlayers = extra;
                // Update the view
                mButtonTotal.setText(
                        mActivity.getString(R.string.game_setup_button_total, mNumberOfPlayers));
                tv1.setText("Spy:" + Constants.getSpyPlayers(mNumberOfPlayers));
                // reset the selection
                break;
            case Constants.GAME_WITHOUT_CARDS:
                startNextActivity();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        saveConfig();
        super.onDestroyView();
    }

    /* Data */
    private List<GameSetupItem> data;

    /* View */
    private TextView tv1;

    /* Presenter, Adapter and Utility */
    private GameSetupRecyclerViewAdapter mAdapter;
    private Config mConfig;

    protected void initNarrator() {
        // mTTS is from parent class
        setNarrator(new Narrator(getActivity(), mTTS));
    }

    protected void initData() {

        isGameNeeded =
                getArguments().getBoolean("IsGameNeeded");

        mConfig = new Config();

        mConfig.load(mActivity);

        mNumberOfPlayers = mConfig.getNumberOfPlayers();

        data = new ArrayList<>();

        for (Resistance.Option option : Resistance.getInstance().getOptions()) {
            data.add(new GameSetupItem(option, mConfig.isOptionEnabled(option)));
        }
    }

    protected void initAdapter() {
        mAdapter = new GameSetupRecyclerViewAdapter(data);
        mRecyclerView.setAdapter(mAdapter);
    }

    protected void initTopView(View v) {
        tv1 = (TextView) v.findViewById(R.id.setup_spy);
        tv1.setText(getText(R.string.role_spy) + ":" + Constants.getSpyPlayers(mNumberOfPlayers));
    }


    protected void initInteraction(View v) {
        mButtonNext = (Button) v.findViewById(R.id.button_next_setup);

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveConfig();

                if (isGameNeeded) {
                    onEventStart(Constants.GAME_WITHOUT_CARDS, 0);
                } else if (!mConfig.isOptionEnabled(Resistance.Option.BLIND_SPY)) {
                    playSound();
                } else {
                    Toast.makeText(getActivity(), "Enjoy Game", Toast.LENGTH_SHORT).show();
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

    private void saveConfig() {
        mConfig.setNumberOfPlayers(mNumberOfPlayers);
        for (GameSetupItem item : data) {
            mConfig.setOptionEnabled(item.getOption(), item.isChecked());
        }
        mConfig.save(mActivity);
    }

    protected ArrayList<User> assignIdentity() {
        ArrayList<User> list = new ArrayList<>(mNumberOfPlayers);
        int visible = Constants.getNormalPlayers(mNumberOfPlayers);
        int invisible = Constants.getSpyPlayers(mNumberOfPlayers);

        while (visible > 0) {
            User good = new User();
            good.setIdentity(Resistance.Role.RESISTANT.getRoleId());
            list.add(good);
            visible--;
        }

        while (invisible > 0) {
            User evil = new User();
            evil.setIdentity(Resistance.Role.SPY.getRoleId());
            list.add(evil);
            invisible--;
        }
        return list;
    }

    private class GameSetupItem {

        private Resistance.Option option;
        private boolean checked;

        public GameSetupItem(Resistance.Option option, boolean checked) {
            this.option = option;
            this.checked = checked;
        }

        public Resistance.Option getOption() {
            return option;
        }

        public void setOption(Resistance.Option option) {
            this.option = option;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }
    }

    private class GameSetupRecyclerViewAdapter
            extends RecyclerView.Adapter<GameSetupRecyclerViewAdapter.ViewHolder> {

        private List<GameSetupItem> data;

        public GameSetupRecyclerViewAdapter(List<GameSetupItem> data) {
            this.data = data;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public TextView name;
            public TextView description;
            public CheckBox checkBox;

            public ViewHolder(View v) {
                super(v);
                name = (TextView) v.findViewById(R.id.character_name);
                description = (TextView) v.findViewById(R.id.character_description);
                checkBox = (CheckBox) v.findViewById(R.id.character_checkbox);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.listitem_game_setup, parent, false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final GameSetupItem mainItem = data.get(position);
            final Resistance.Option option = mainItem.getOption();

            holder.name.setText(option.getTitleResId());
            holder.description.setText(option.getDescResId());
            holder.checkBox.setChecked(mainItem.isChecked());

            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mainItem.setChecked(!mainItem.isChecked());

                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
