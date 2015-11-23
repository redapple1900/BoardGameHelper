package com.yuanwei.resistance.partygame.origin.ui;

import android.os.Bundle;

import com.yuanwei.resistance.constant.Constants;
import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.partygame.origin.Config;
import com.yuanwei.resistance.partygame.origin.texttospeech.Narrator;
import com.yuanwei.resistance.ui.fragment.BasePrepareFragment;

import java.util.ArrayList;

/**
 * Created by chenyuanwei on 15/10/22.
 */
public class ResistancePrepareFragment extends BasePrepareFragment {

    public static final String TAG = "ResistancePrepareFragment";

    private Config mConfig;

    public static ResistancePrepareFragment createInstance(ArrayList<User> users) {
        ResistancePrepareFragment fragment = new ResistancePrepareFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("gamerList", users);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getGameId() {
        return Constants.ORIGIN;
    }

    @Override
    protected void initUtility() {
        super.initUtility();

        setNarrator(new Narrator(getActivity(), mTTS));

        mConfig = new Config();

        mConfig.load(getActivity());
    }
}
