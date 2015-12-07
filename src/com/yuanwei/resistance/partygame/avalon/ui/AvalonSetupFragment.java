package com.yuanwei.resistance.partygame.avalon.ui;

import android.os.Bundle;

import com.yuanwei.resistance.R;
import com.yuanwei.resistance.constant.Constants;
import com.yuanwei.resistance.fragment.NumberPickDialogFragment;
import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.partygame.avalon.Config;
import com.yuanwei.resistance.partygame.avalon.model.Avalon;
import com.yuanwei.resistance.partygame.avalon.model.Avalon.Role;
import com.yuanwei.resistance.partygame.avalon.rule.SetupRule;
import com.yuanwei.resistance.partygame.avalon.texttospeech.Narrator;
import com.yuanwei.resistance.partygame.origin.model.Resistance.Option;
import com.yuanwei.resistance.ui.fragment.BaseSetupFragment;
import com.yuanwei.resistance.ui.list.OptionSetupRecyclerViewAdapter;
import com.yuanwei.resistance.ui.list.OptionSetupRecyclerViewAdapter.OptionItem;
import com.yuanwei.resistance.ui.list.RoleSetupRecyclerViewAdapter;
import com.yuanwei.resistance.ui.list.RoleSetupRecyclerViewAdapter.RoleItem;
import com.yuanwei.resistance.ui.list.SectionRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenyuanwei on 15/10/16.
 */
public class AvalonSetupFragment extends BaseSetupFragment {


    public static final String TAG = "AvalonSetupFragment";
    /* Data */
    private List<RoleItem> roles;
    private List<OptionItem> options;
    private List<User> mUserList;
    /* Presenter, Adapter and Utility */
    private RoleSetupRecyclerViewAdapter mRoleAdapter;
    private SetupRule mRule;
    private OptionSetupRecyclerViewAdapter mOptionAdapter;
    private SectionRecyclerViewAdapter mSectionAdapter;
    private Config mConfig;

    public static AvalonSetupFragment createInstance(boolean isGameNeeded, ArrayList<User> list) {
        AvalonSetupFragment fragment = new AvalonSetupFragment();
        Bundle bundle = new Bundle();
        // TODO:: No hard coding
        bundle.putBoolean("IsGameNeeded", isGameNeeded);
        bundle.putParcelableArrayList(Constants.USERLIST_KEY, list);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        saveConfig();
        super.onDestroyView();
    }

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


        roles = new ArrayList<>();

        options = new ArrayList<>();

        mRule = new SetupRule();

        mConfig = new Config(getGameId());

        mConfig.load(mActivity);

        mUserList = getArguments().getParcelableArrayList(Constants.USERLIST_KEY);

        mNumberOfPlayers = mConfig.getNumberOfPlayers();

        for (Option option : Avalon.getInstance().getOptions()) {
            options.add(new OptionItem(option, mConfig.isOptionEnabled(option)));
        }

        for (Role role : Avalon.getInstance().getSpecialRoles()) {
            boolean selected = mConfig.isRoleEnabled(role);
            if (selected) {
                mRule.notifyRoleSelected(role);
            }
            roles.add(new RoleItem(role, selected, true));
        }

        for (RoleItem item : roles) {
            if (item.isChecked()) continue; // A checked item must be selectable
            item.setSelectable(mRule.isRoleSelectable(item.getRole(), mNumberOfPlayers));
        }

    }

    @Override
    protected void initAdapter() {
        mRoleAdapter = new RoleSetupRecyclerViewAdapter(roles);
        mRoleAdapter.setOnRoleCheckListener(new RoleSetupRecyclerViewAdapter.OnRoleCheckListener() {
            @Override
            public void onToggle(RoleItem mainItem) {

                final Avalon.Role role = mainItem.getRole();

                if (mainItem.isChecked()) {
                    mRule.notifyRoleRemoved(role);
                    for (RoleItem item : roles) {

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
                    for (RoleItem item : roles) {

                        if (item.getRole() == role) continue;

                        if (item.isChecked()) continue;

                        item.setSelectable(
                                mRule.isRoleSelectable(item.getRole(), mNumberOfPlayers));

                        if (!item.isSelectable()) continue;
                        item.setChecked(mRule.isRoleSelected(item.getRole()));
                    }
                    mainItem.setChecked(true);
                }
                mRoleAdapter.notifyDataSetChanged();
            }
        });
        mOptionAdapter = new OptionSetupRecyclerViewAdapter(options);

        mSectionAdapter =
                new SectionRecyclerViewAdapter(mRoleAdapter, mOptionAdapter);

        mSectionAdapter.setSections(new SectionRecyclerViewAdapter.Section[]{
                new SectionRecyclerViewAdapter.Section(0, getString(R.string.option)),
                new SectionRecyclerViewAdapter.Section(options.size() + 1, getString(R.string.role))
        });

        mRecyclerView.setAdapter(mSectionAdapter);
    }

    @Override
    public void onEventStart(String type, int extra) {

        switch (type) {
            case NumberPickDialogFragment.CLEAR_SELECTION:
                clearSelection();
            case NumberPickDialogFragment.SET_SELECTION:
                mNumberOfPlayers = extra;
                // Update the view
                updateTopView();
                // reset the selection

                for (RoleItem item : roles) {
                    if (item.isSelectable()) continue;

                    item.setSelectable(mRule.isRoleSelectable(item.getRole(), mNumberOfPlayers));
                }

                mRoleAdapter.notifyDataSetChanged();
                break;
            case Constants.GAME_WITHOUT_CARDS:
                startNextActivity();
                break;
        }
    }

    @Override
    protected void saveConfig() {
        mConfig.setNumberOfPlayers(mNumberOfPlayers);
        for (OptionItem item : options) {
            mConfig.setOptionEnabled(item.getOption(), item.isChecked());
        }
        for (RoleItem item : roles) {
            mConfig.setRoleEnabled(item.getRole(), item.isChecked());
        }
        mConfig.save(mActivity);
    }

    @Override
    protected Config getConfig() {
        return mConfig;
    }

    private void clearSelection() {
        for (RoleItem item: roles){
            item.setChecked(false);
        }
        mRoleAdapter.notifyDataSetChanged();
        mRule.reset();
    }

    protected ArrayList<User> assignIdentity() {
        ArrayList<User> list = new ArrayList<>(mNumberOfPlayers);
        int visible = Constants.getNormalPlayers(mNumberOfPlayers);
        int invisible = Constants.getSpyPlayers(mNumberOfPlayers);

        for (RoleItem item : roles) {
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

        if (mUserList != null && !mUserList.isEmpty()) {
            int i = 0;
            for (User user : mUserList) {
                if (user.getId() != -1) { // Not system auto assigned names -->
                    list.get(i).setName(user.getName());
                }
                i++;
            }
        }

        return list;
    }
}
