package com.yuanwei.resistance.partygame.avalon.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuanwei.resistance.R;
import com.yuanwei.resistance.constant.Constants;
import com.yuanwei.resistance.fragment.NumberPickDialogFragment;
import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.partygame.avalon.Config;
import com.yuanwei.resistance.partygame.avalon.model.Avalon;
import com.yuanwei.resistance.partygame.avalon.rule.SetupRule;
import com.yuanwei.resistance.partygame.avalon.texttospeech.Narrator;
import com.yuanwei.resistance.ui.fragment.BaseSetupFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenyuanwei on 15/10/16.
 */
public class AvalonSetupFragment extends BaseSetupFragment {


    public static final String TAG = "AvalonSetupFragment";

    public static AvalonSetupFragment createInstance(boolean isGameNeeded) {
        AvalonSetupFragment fragment = new AvalonSetupFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("IsGameNeeded", isGameNeeded);
        fragment.setArguments(bundle);
        return fragment;
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
    private SetupRule mRule;
    private Config mConfig;

    @Override
    public int getGameId() {
        return Constants.AVALON;
    }

    protected void initNarrator() {
        Narrator narrator = new Narrator(getActivity(), mTTS);

        narrator.setConfig(mConfig);

        setNarrator(narrator);
    }

    @Override
    protected void initData() {

        isGameNeeded =
                mActivity.getIntent().getExtras().getString("Cards").equals(Constants.GAME_WITHOUT_CARDS);

        data = new ArrayList<>();
        mRule = new SetupRule();

        mConfig = new Config();
        mConfig.load(mActivity);

        mNumberOfPlayers = mConfig.getNumberOfPlayers();

        for (Avalon.Role role : Avalon.getInstance().getSpecialRoles()) {
            boolean selected = mConfig.isRoleEnabled(role);
            if (selected) {
                mRule.notifyRoleSelected(role);
            }
            data.add(new GameSetupItem(role, selected, true));
        }
        for (GameSetupItem item : data) {
            if (item.isChecked()) continue; // A checked item must be selectable
            item.setSelectable(mRule.isRoleSelectable(item.getRole(), mNumberOfPlayers));
        }

    }

    @Override
    protected void initAdapter() {
        mAdapter = new GameSetupRecyclerViewAdapter(data);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initTopView(View v) {
        tv1 = (TextView) v.findViewById(R.id.setup_spy);
        tv1.setText("Spy:" + Constants.getSpyPlayers(mNumberOfPlayers));
    }

    @Override
    protected void initInteraction(View v) {
        mButtonNext = (Button) v.findViewById(R.id.button_next_setup);

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveConfig();

                if (isGameNeeded) {
                    onEventStart(Constants.GAME_WITHOUT_CARDS, 0);
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

    @Override
    public void onEventStart(String type, int extra) {

        switch (type) {
            case NumberPickDialogFragment.CLEAR_SELECTION:
                clearSelection();
            case NumberPickDialogFragment.TAG:
                mNumberOfPlayers = extra;
                // Update the view
                mButtonTotal.setText(
                        mActivity.getString(R.string.game_setup_button_total, mNumberOfPlayers));
                tv1.setText("Spy:" + Constants.getSpyPlayers(mNumberOfPlayers));
                // reset the selection

                for (GameSetupItem item : data) {
                    if (item.isSelectable()) continue;

                    item.setSelectable(mRule.isRoleSelectable(item.getRole(), mNumberOfPlayers));
                }

                mAdapter.notifyDataSetChanged();
                break;
            case Constants.GAME_WITHOUT_CARDS:
                startNextActivity();
                break;
        }

    }

    private void clearSelection() {
        for (GameSetupItem item: data){
            item.setChecked(false);
        }
        mAdapter.notifyDataSetChanged();
        mRule.reset();
    }

    protected ArrayList<User> assignIdentity() {
        ArrayList<User> list = new ArrayList<>(mNumberOfPlayers);
        int visible = Constants.getNormalPlayers(mNumberOfPlayers);
        int invisible = Constants.getSpyPlayers(mNumberOfPlayers);

        for (GameSetupItem item : data) {
            if (!item.isChecked()) continue;

            User user = new User();
            int id = item.getRole().getRoleId();
            user.setIdentity(id);

            if (id > 0) {
                visible --;
            } else {
                invisible --;
            }

            list.add(user);
        }

        while (visible > 0) {
            User good = new User();
            good.setIdentity(Avalon.Role.LOYALIST.getRoleId());
            list.add(good);
            visible--;
        }

        while (invisible > 0) {
            User evil = new User();
            evil.setIdentity(Avalon.Role.MINION.getRoleId());
            list.add(evil);
            invisible--;
        }
        return list;
    }

    private void saveConfig() {
        mConfig.setNumberOfPlayers(mNumberOfPlayers);
        for (GameSetupItem item : data) {
            mConfig.setRoleEnabled(item.getRole(), item.isChecked());
        }
        mConfig.save(mActivity);
    }

    private class GameSetupItem {

        private Avalon.Role role;
        private boolean checked;
        private boolean selectable;


        public GameSetupItem(Avalon.Role role, boolean checked, boolean selectable) {
            this.role = role;
            this.checked = checked;
            this.selectable = selectable;
        }

        public Avalon.Role getRole() {
            return role;
        }

        public void setRole(Avalon.Role role) {
            this.role = role;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public boolean isSelectable() {
            return selectable;
        }

        public void setSelectable(boolean selectable) {
            this.selectable = selectable;
        }
    }

    private class GameSetupRecyclerViewAdapter
            extends RecyclerView.Adapter<GameSetupRecyclerViewAdapter.ViewHolder> {

        private List<GameSetupItem> data;

        public GameSetupRecyclerViewAdapter(List<GameSetupItem> data) {
            this.data = data;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView name;
            public TextView description;
            public CheckBox checkBox;

            public ViewHolder(View v) {
                super(v);
                imageView = (ImageView) v.findViewById(R.id.character_image_view);
                name = (TextView) v.findViewById(R.id.character_name);
                description = (TextView) v.findViewById(R.id.character_description);
                checkBox = (CheckBox) v.findViewById(R.id.character_checkbox);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.listitem_game_setup, parent, false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final GameSetupItem mainItem = data.get(position);
            final Avalon.Role role = mainItem.getRole();

            holder.imageView.setImageResource(role.getImgResId());
            holder.name.setText(role.getTitleResId());
            holder.description.setText(role.getDescResId());
            holder.checkBox.setChecked(mainItem.isChecked());

            holder.checkBox.setVisibility(mainItem.isSelectable() ? View.VISIBLE : View.GONE);

            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mainItem.isChecked()) {
                        mRule.notifyRoleRemoved(role);
                        for (GameSetupItem item : data) {
                            if (item.getRole() == role) continue;
                            if (!mRule.isRoleSelectable(item.getRole(), mNumberOfPlayers)) {
                                item.setSelectable(false);
                                item.setChecked(false);
                                mRule.notifyRoleRemoved(item.getRole());
                            } else {
                                item.setSelectable(true);
                                item.setChecked(mRule.isRoleSelected(item.getRole()));
                            }
                        }
                        mainItem.setChecked(false);
                    } else {
                        mRule.notifyRoleSelected(role);
                        for (GameSetupItem item : data) {
                            if (item.getRole() == role) continue;
                            if (item.isChecked()) continue;
                            item.setSelectable(
                                    mRule.isRoleSelectable(
                                            item.getRole(), mNumberOfPlayers));
                            if (!item.isSelectable()) continue;
                            item.setChecked(mRule.isRoleSelected(item.getRole()));
                        }
                        mainItem.setChecked(true);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
