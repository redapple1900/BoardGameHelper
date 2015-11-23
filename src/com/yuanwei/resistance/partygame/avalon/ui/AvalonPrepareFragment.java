package com.yuanwei.resistance.partygame.avalon.ui;

import android.os.Bundle;

import com.yuanwei.resistance.constant.Constants;
import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.partygame.avalon.Config;
import com.yuanwei.resistance.partygame.avalon.texttospeech.Narrator;
import com.yuanwei.resistance.ui.fragment.BasePrepareFragment;

import java.util.ArrayList;

/**
 * Created by chenyuanwei on 15/10/23.
 */
public class AvalonPrepareFragment extends BasePrepareFragment {

    public static final String TAG = "AvalonPrepareFragment";

    private Config mConfig;

    public static AvalonPrepareFragment createInstance(ArrayList<User> users) {
        AvalonPrepareFragment fragment = new AvalonPrepareFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("gamerList", users);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getGameId() {
        return Constants.AVALON;
    }

    @Override
    protected void initUtility() {
        super.initUtility();

        mConfig = new Config();

        mConfig.load(getActivity());

        Narrator narrator = new Narrator(getActivity(), mTTS);

        narrator.setConfig(mConfig);

        setNarrator(narrator);
    }
}
