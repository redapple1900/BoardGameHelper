package com.yuanwei.resistance.partygame.origin.ui;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yuanwei.resistance.R;
import com.yuanwei.resistance.fragment.GridFragment;
import com.yuanwei.resistance.fragment.LongPressFragment;
import com.yuanwei.resistance.fragment.MissionFragment;
import com.yuanwei.resistance.fragment.TimerFragment;
import com.yuanwei.resistance.fragment.VoteFragment;
import com.yuanwei.resistance.model.Gamer;
import com.yuanwei.resistance.model.Params;
import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.partygame.origin.Config;
import com.yuanwei.resistance.partygame.origin.model.Bookkeeper;
import com.yuanwei.resistance.partygame.origin.model.Resistance;
import com.yuanwei.resistance.ui.fragment.BaseMultiSceneFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chenyuanwei on 15/11/1.
 */
public abstract class BaseResistanceGamingFragment extends BaseMultiSceneFragment {

    // Tag For Fragments.
    protected final String PROPOSE = "propose";

    protected final String DISCUSSION = "discussion";

    protected final String SELECTION = "selection";

    protected final String PRESS = "press";

    protected final String MISSION = "mission";

    protected final String VOTE = "vote";

    protected final String OVER = "over";

    protected final String WIN = "win";

    protected final String LOSE = "lose";

    protected Config mConfig;

    protected Map<String, TextView> mViewMap;

    private AlertDialog mTransitDialog;

    protected ArrayList<User> mUserList;

    protected ArrayList<Gamer> mGamers;

    protected Bookkeeper mBookkeeper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            initData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gaming, container, false);
        initView(v);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            initStaff();
        }
        start();
    }

    @Override
    protected void onSpeakDone() {

    }

    @Override
    protected void onSpeakStart() {

    }

    @Override
    protected void onSpeakError() {

    }

    @Override
    protected Fragment createFragment(String tag) {
        Fragment fragment = null;
        switch (tag) {
            case PRESS:
                fragment = LongPressFragment.createInstance(tag, getCurrentGamer().getName());
                break;
            case PROPOSE:
                Params params = new Params.Builder()
                        .setTag(tag)
                        .setAllowPass(mConfig.isOptionEnabled(Resistance.Option.ALLOW_PASS))
                        .setLimit(getMissionLimit())
                        .setStats(getGameStatus())
                        .build();
                fragment = GridFragment.createInstance(
                        params,
                        mUserList,
                        mGamers);
                break;
            case DISCUSSION:
                fragment = TimerFragment.createInstance(tag);
                break;
            case MISSION:
                fragment = MissionFragment.createInstance(tag, getCurrentPlayerIdentity());
                break;
            case VOTE:
                fragment = VoteFragment.createInstance(tag);
                break;
            case OVER:
                fragment = GridFragment.createInstance(
                        new Params.Builder().setTag(tag).setStats(getGameStatus()).build(),
                        mUserList,
                        mGamers);
                break;
        }
        return fragment;
    }

    @Override
    protected int getContainer() {
        return R.id.main;
    }

    protected void showTransitDialog() {
        if (mTransitDialog == null) {
            mTransitDialog = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK)
                    .setMessage(getString(R.string.transit_message))
                    .create();
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mTransitDialog.dismiss();
            }
        }, 1000);

        mTransitDialog.show();
    }

    protected void showMissionResultDialog() {
        AlertDialog.Builder dialogBuilder =
                new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);
        if (getMissionResult()) {
            String title = getString(R.string.string_builder_message_winresult_game) + "\n"
                    + (getFailCount()> 0 ? getString(R.string.string_message_failvotes_game, getFailCount())
                    : "");
            dialogBuilder
                    .setIcon(R.drawable.win)
                    .setTitle(title)
                    .setMessage(
                            getString(R.string.string_builder_message_winresult_game)
                                    + "\n"
                                    + "\n"
                                    + getString(R.string.last_transit_message)
                                    + ":" + getLeader().getName())
                    .setPositiveButton(getString(R.string.string_builder_positive_game), null);
        } else {
            dialogBuilder
                    .setIcon(R.drawable.lose)
                    .setTitle(getString(R.string.string_builder_title_loseresult_game)
                            + "\n"
                            + getString(
                            R.string.string_message_failvotes_game, getFailCount()))
                    .setMessage(getString(R.string.string_builder_message_loseresult_game)
                            + "\n"
                            + "\n"
                            + getString(R.string.last_transit_message)
                            + ":" + getLeader().getName())
                    .setPositiveButton(getString(R.string.string_builder_positive_game), null);
        }
        dialogBuilder.show();
    }

    protected void initData() {

        mUserList = getArguments().getParcelableArrayList("gamerList");

        mGamers = new ArrayList<>(mUserList.size());

        for (User user : mUserList) {
            Gamer gamer = new Gamer(user.getIdentity(), 5);
            mGamers.add(gamer);
        }

        mConfig = new Config();

        mConfig.load(getActivity());

        mBookkeeper = new Bookkeeper(mUserList, mGamers);
    }

    protected void updateStatusView(String key, int num) {
        mViewMap.get(key).setText(String.valueOf(num));
    }

    protected abstract void initStaff();

    protected abstract int getCurrentPlayerIdentity();

    protected abstract User getCurrentGamer();

    protected abstract User getLeader();

    protected abstract boolean getMissionResult();

    protected abstract int getMissionLimit();

    protected abstract int getFailCount();

    protected abstract void start();

    private void initView(View view) {

        // TODO:Makde a bettet

        int size = (int) getResources().getDimension(R.dimen.small_image_size);

        TextView textView_win = (TextView) view.findViewById(R.id.status_win);

        TextView textView_lose = (TextView) view.findViewById(R.id.status_lose);

        TextView textView_propose = (TextView) view.findViewById(R.id.status_propose_round);

        TextView textView_members = (TextView) view.findViewById(R.id.status_members_required);

        Drawable token_win = getResources().getDrawable(R.drawable.token_win);

        token_win.setBounds(0, 0, size, size);

        Drawable token_sabotage = getResources().getDrawable(R.drawable.token_sabotage);

        token_sabotage.setBounds(0, 0, size, size);

        Drawable vote_aye = getResources().getDrawable(R.drawable.vote_aye);

        vote_aye.setBounds(0, 0, size, size);

        Drawable propose = getResources().getDrawable(R.drawable.propose);

        propose.setBounds(0, 0, size, size);
        
        textView_win.setCompoundDrawables(token_win, null, null, null);

        textView_lose.setCompoundDrawables(token_sabotage, null, null, null);

        textView_propose.setCompoundDrawables(vote_aye, null, null, null);

        textView_members.setCompoundDrawables(propose, null, null, null);

        mViewMap = new HashMap<>();
        mViewMap.put(WIN, textView_win);
        mViewMap.put(LOSE, textView_lose);
        mViewMap.put(PROPOSE, textView_propose);
        mViewMap.put(MISSION, textView_members);
    }

    private int getGameStatus() {
        Resistance.GameEnd end = mBookkeeper.getGameEnd();

        switch (end) {
            case NOT_FINISHED:
                return Params.GAME_NOT_OVER;
            case RESISTANCE_WIN:
                return Params.RESISTANCE_WIN;
            default:
                return Params.SPY_WIN;
        }
    }
}
