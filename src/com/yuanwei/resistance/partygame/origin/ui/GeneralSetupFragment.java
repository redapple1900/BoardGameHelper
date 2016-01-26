package com.yuanwei.resistance.partygame.origin.ui;

import android.os.Bundle;

import com.yuanwei.resistance.constant.Constants;
import com.yuanwei.resistance.fragment.NumberPickDialogFragment;
import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.partygame.avalon.Config;
import com.yuanwei.resistance.partygame.origin.model.Resistance;
import com.yuanwei.resistance.partygame.origin.texttospeech.Narrator;
import com.yuanwei.resistance.ui.fragment.BaseSetupFragment;
import com.yuanwei.resistance.ui.list.OptionSetupRecyclerViewAdapter;
import com.yuanwei.resistance.ui.list.OptionSetupRecyclerViewAdapter.OptionItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by chenyuanwei on 15/10/19.
 */
// TODO: Union with AvalonSetupFragment
public class GeneralSetupFragment extends BaseSetupFragment {

    public static final String TAG = "ResistanceSetupFragment";

    private List<User> mUserList;
    /* Data */
    private List<OptionItem> data;
    /* Presenter, Adapter and Utility */
    private OptionSetupRecyclerViewAdapter mAdapter;
    private Config mConfig;

    public static GeneralSetupFragment createInstance(boolean isGameNeeded, ArrayList<User> list) {
        GeneralSetupFragment fragment = new GeneralSetupFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.USERLIST_KEY, list);
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
            case NumberPickDialogFragment.SET_SELECTION:
                mNumberOfPlayers = extra;
                // Update the view
                updateTopView();
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

    protected void initNarrator() {
        // mTTS is fromJSON parent class
        setNarrator(new Narrator(getActivity(), mTTS));
    }

    protected void initData() {

        isGameNeeded =
                getArguments().getBoolean("IsGameNeeded");

        mUserList = getArguments().getParcelableArrayList(Constants.USERLIST_KEY);

        mConfig = new Config(getGameId());

        mConfig.load(mActivity);

        mNumberOfPlayers = mConfig.getNumberOfPlayers();

        data = new ArrayList<>();

        for (Resistance.Option option : Resistance.getInstance().getOptions()) {
            data.add(new OptionSetupRecyclerViewAdapter
                    .OptionItem(option, mConfig.isOptionEnabled(option)));
        }
    }

    @Override
    protected void initAdapter() {
        mAdapter = new OptionSetupRecyclerViewAdapter(data);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void saveConfig() {
        mConfig.setNumberOfPlayers(mNumberOfPlayers);
        for (OptionItem item : data) {
            mConfig.setOptionEnabled(item.getOption(), item.isChecked());
        }
        mConfig.save(mActivity);
    }

    @Override
    protected Config getConfig() {
        return mConfig;
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

        Collections.shuffle(list);

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
