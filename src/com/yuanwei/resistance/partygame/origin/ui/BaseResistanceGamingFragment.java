package com.yuanwei.resistance.partygame.origin.ui;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yuanwei.resistance.BuildConfig;
import com.yuanwei.resistance.R;
import com.yuanwei.resistance.SetupActivity;
import com.yuanwei.resistance.WelcomeActivity;
import com.yuanwei.resistance.constant.Constants;
import com.yuanwei.resistance.fragment.GridFragment;
import com.yuanwei.resistance.fragment.LongPressFragment;
import com.yuanwei.resistance.fragment.MissionFragment;
import com.yuanwei.resistance.fragment.TimerFragment;
import com.yuanwei.resistance.fragment.VoteFragment;
import com.yuanwei.resistance.model.Gamer;
import com.yuanwei.resistance.model.GridParams;
import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.model.protocol.PlotHost;
import com.yuanwei.resistance.moderator.BaseSwitcher;
import com.yuanwei.resistance.partygame.avalon.Config;
import com.yuanwei.resistance.partygame.avalon.texttospeech.LancelotNarrator;
import com.yuanwei.resistance.partygame.origin.model.BookKeeper;
import com.yuanwei.resistance.partygame.origin.model.Resistance;
import com.yuanwei.resistance.partygame.origin.model.StateKeeper;
import com.yuanwei.resistance.ui.DrawableUtils;
import com.yuanwei.resistance.ui.ShortInfoDialog;
import com.yuanwei.resistance.ui.fragment.BaseMultiSceneFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

/**
 * Created by chenyuanwei on 15/11/1.
 */
public abstract class BaseResistanceGamingFragment extends BaseMultiSceneFragment {

    public static final String EXIT = "exit";

    // Tag For Fragments.
    protected final String PROPOSE = "propose";

    protected final String DISCUSSION = "discussion";

    protected final String SELECTION = "selection";

    protected final String PRESS = "press";

    protected final String MISSION = "mission";

    protected final String VOTE = "vote";

    protected final String ASSASSINATION = "assassination";

    protected final String OVER = "over";

    protected final String REMOVE = "remove";

    // State of a Dialog
    protected final String REPLAY = "replay";

    // For Status View Only
    protected final String WIN = "win";

    protected final String LOSE = "lose";

    protected Config mConfig;

    protected ArrayList<User> mUserList;

    protected ArrayList<Gamer> mGamers;

    protected BookKeeper mBookKeeper;

    protected StateKeeper mStateKeeper;

    private AlertDialog mTransitDialog;

    private LancelotNarrator mLancelotNarrator;

    private Map<String, TextView> mViewMap;

    private boolean shouldSaveGame = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            initData();
            initNarrator();
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
    public void onDestroyView() {
        if (shouldSaveGame) new BookKeeper.Agent().save(getActivity(), mBookKeeper);
        if (BuildConfig.DEBUG) Log.d("Resistance:JSON", "Saved");
        mBookKeeper = null;
        mStateKeeper = null;
        mViewMap = null;
        mTransitDialog = null;
        super.onDestroyView();
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
    public void onDetach() {
        try {
            ((PlotHost) getActivity()).setPlotListener(null);
        } catch (ClassCastException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        super.onDetach();
    }

    @Override
    protected void onSpeakDone() {

    }

    @Override
    protected void onSpeakStart() {

    }

    @Override
    protected void onSpeakError() {
        Toast.makeText(
                getActivity(),
                getString(R.string.string_playsound_toast),
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected Fragment createFragment(String tag) {
        Fragment fragment = null;
        switch (tag) {
            case PROPOSE:
                GridParams gridParams = new GridParams.Builder()
                        .setGameId(mConfig.getGame())
                        .setTag(tag)
                        .setAllowPass(mConfig.isOptionEnabled(Resistance.Option.ALLOW_PASS))
                        .setLimit(getMissionLimit())
                                // TODO:: Use StateKeeper
                        .setStatus(mBookKeeper.getGameStatus())
                        .build();
                fragment = GridFragment.createInstance(
                        gridParams,
                        mUserList,
                        mGamers);
                break;
            case OVER:
                fragment = GridFragment.createInstance(
                        new GridParams.Builder()
                                .setGameId(mConfig.getGame())
                                .setTag(tag)
                                .setStatus(mBookKeeper.getGameStatus())
                                .build(),
                        mUserList,
                        mGamers);
                break;
            case ASSASSINATION:
                fragment = GridFragment.createInstance(
                        new GridParams.Builder()
                                .setGameId(mConfig.getGame())
                                .setTag(tag)
                                .setStatus(Resistance.GameStatus.ASSASSINATION)
                                .build(),
                        mUserList,
                        mGamers);
                break;
            case REMOVE:
                fragment = GridFragment.createInstance(
                        new GridParams.Builder()
                                .setGameId(mConfig.getGame())
                                .setTag(tag)
                                .setStatus(Resistance.GameStatus.MODIFYING_USERS)
                                .build(),
                        mUserList,
                        mGamers);
                break;
            case PRESS:
                fragment = LongPressFragment.createInstance(tag, getCurrentGamer().getName());
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
        }
        return fragment;
    }

    @Override
    protected int getContainer() {
        return R.id.main;
    }

    protected void showTransitDialog(String gamer) {
        if (mTransitDialog == null) {
            String string = gamer != null ?
                    getString(R.string.transit_message) + ":" + gamer :
                    getString(R.string.transit_message);

            mTransitDialog = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK)
                    .setMessage(string)
                    .create();
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mTransitDialog.dismiss();
            }
        }, 2000);

        mTransitDialog.show();
    }

    protected void showTransitDialog() {
        showTransitDialog(null);
    }

    public void showDialog(String tag) {
        final ShortInfoDialog dialog = new ShortInfoDialog(getActivity());
        bindDialog(dialog, tag);
        dialog.show();
    }

    private void bindDialog(final ShortInfoDialog dialog, String tag) {

        final TextView title = ButterKnife.findById(dialog, R.id.title);
        final TextView subtitle = ButterKnife.findById(dialog, R.id.subtitle);
        final Button positive = ButterKnife.findById(dialog, R.id.affirmative);
        final Button negative = ButterKnife.findById(dialog, R.id.negative);
        final Button neutral = ButterKnife.findById(dialog, R.id.neutral);

        switch (tag) {
            case MISSION:
                title.setText(prepareMissionDialogTitle());
                title.setTextColor(getResources().getColor(android.R.color.black));
                title.setCompoundDrawables(DrawableUtils.getDrawable(
                        getResources(),
                        getMissionResult() ? R.drawable.win : R.drawable.lose,
                        R.dimen.small_image_size), null, null, null);

                subtitle.setText(prepareMissionDialogSubtitle());

                positive.setText(getString(R.string.confirmation));
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                negative.setVisibility(View.GONE);

                break;
            case OVER:
                title.setText(getString(R.string.replay_dialog_title));
                title.setTextColor(getResources().getColor(android.R.color.darker_gray));

                positive.setText(getString(R.string.affirmative));
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onEventStart(REPLAY, BaseSwitcher.PRIMARY);
                        dialog.dismiss();
                    }
                });

                negative.setText(getString(R.string.negative));
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onEventStart(REPLAY, BaseSwitcher.SECONDARY);
                        dialog.dismiss();
                    }
                });

                neutral.setText(getString(R.string.cancel));
                neutral.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                neutral.setVisibility(View.VISIBLE);
                break;
            case EXIT:
                title.setText(getString(R.string.string_exit_title));
                title.setTextColor(getResources().getColor(android.R.color.black));

                subtitle.setText(getString(R.string.string_exit_message));

                positive.setText(getString(R.string.string_exit_positive));
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clear();
                        startWelcomeActivity();
                    }
                });

                neutral.setText(getString(R.string.string_exit_negative));
                neutral.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                neutral.setVisibility(View.VISIBLE);

                negative.setText(getString(R.string.save_quit));
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startWelcomeActivity();
                    }
                });
                break;
        }
    }

    private String prepareMissionDialogTitle() {
        if (getMissionResult()) {
            return getFailCount() == 0 ?
                    getString(R.string.mission_win_title) :
                    getString(R.string.mission_win_title)
                            + " "
                            + (getString(R.string.string_message_failvotes_game, getFailCount()));
        } else {
            return getString(R.string.mission_lose_title)
                    + " "
                    + getString(R.string.string_message_failvotes_game, getFailCount());
        }
    }

    private String prepareMissionDialogSubtitle() {
        return getString(getMissionResult() ?
                R.string.mission_win_message :
                R.string.mission_lose_message)
                + "\n"
                + "\n"
                + getString(R.string.last_transit_message)
                + ":" + getLeader().getName();
    }

    private void initData() {

        mStateKeeper = new StateKeeper(getActivity());

        mUserList = getArguments().getParcelableArrayList(Constants.USERLIST_KEY);

        if (mUserList == null) {
            mBookKeeper = new BookKeeper.Agent().load(getActivity());
            if (mBookKeeper != null) mBookKeeper.initiate();

            mUserList = mBookKeeper.getUserList();
            mGamers = mBookKeeper.getGamerList();

            mConfig = new Config(mBookKeeper.getGameId());
            mStateKeeper.load();
        } else {
            mGamers = new ArrayList<>(mUserList.size());
            for (User user : mUserList) {
                Gamer gamer = new Gamer(user.getIdentity(), 5);
                mGamers.add(gamer);
            }

            mConfig = new Config(getArguments().getInt(Constants.GAME));
            mBookKeeper = new BookKeeper(getGameId(), mUserList, mGamers);
        }

        mConfig.load(getActivity());
    }

    private void initNarrator() {
        mLancelotNarrator = new LancelotNarrator(getActivity(), mTTS);

        setNarrator(mLancelotNarrator);
    }

    private void initView(View view) {
        TextView textView_win = ButterKnife.findById(view, R.id.status_win);
        TextView textView_lose = ButterKnife.findById(view, R.id.status_lose);
        TextView textView_propose = ButterKnife.findById(view, R.id.status_propose_round);
        TextView textView_members = ButterKnife.findById(view, R.id.status_members_required);

        Drawable token_win = DrawableUtils.getDrawable(
                getResources(), R.drawable.token_win, R.dimen.small_image_size);
        Drawable token_sabotage = DrawableUtils.getDrawable(
                getResources(), R.drawable.token_sabotage, R.dimen.small_image_size);
        Drawable vote_aye = DrawableUtils.getDrawable(
                getResources(), R.drawable.vote_aye, R.dimen.small_image_size);
        Drawable propose = DrawableUtils.getDrawable(
                getResources(), R.drawable.propose, R.dimen.small_image_size);
        
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

    protected void startSetupActivity() {
        myStartActivity(new Intent()
                .putExtra("Cards", Constants.GAME_WITHOUT_CARDS)
                .putExtra(Constants.GAME, getGameId())
                .putExtra(Constants.USERLIST_KEY, mUserList)
                .setClass(getActivity(), SetupActivity.class));
        getActivity().finish();
    }

    protected void startWelcomeActivity() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), WelcomeActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    // TODO:: make it as a utility
    //On click event for rate this app button
    protected void rateAppOnGooglePlay() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //Try Google play
        intent.setData(Uri.parse("market://details?id=" + "com.yuanwei.resistance"));
        if (!myStartActivity(intent)) {
            //Market (Google play) app seems not installed, let's try to open a webbrowser
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?" + "com.yuanwei.resistance"));
            if (!myStartActivity(intent)) {
                //Well if this also fails, we have run out of options, inform the user.
                Toast.makeText(getActivity(), "Could not open Google Play, please install the market app.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected boolean myStartActivity(Intent intent) {
        try {
            startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), getString(R.string.string_toast2_game), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    protected void updateStatusView(String key, int num) {
        mViewMap.get(key).setText(String.valueOf(num));
    }

    protected int getGameId() {
        return mConfig.getGame();
    }

    protected void clear() {
        mBookKeeper.clear(getActivity());
        mStateKeeper.clear();
        shouldSaveGame = false;
    }

    private User getLeader() {
        return mBookKeeper.getUser(mStateKeeper.getLeader());
    }

    private int getCurrentPlayerIdentity() {
        return mBookKeeper.getGamer(getCurrentGamer()).getRoleId();
    }

    private int getMissionLimit() {
        return Resistance.getNumberForMission(
                mConfig.getNumberOfPlayers(),
                mStateKeeper.getRound());
    }

    private int getFailCount() {
        List<Pair<Integer, Integer>> list = mBookKeeper.getMissionExecutionResult();
        return list.get(list.size() - 1).second;
    }

    private User getCurrentGamer() {
        return mBookKeeper.getUser(mStateKeeper.getCurrentUserIndex());
    }

    private boolean getMissionResult() {
        List<Integer> list = mBookKeeper.getMissionResult();
        return list.get(list.size() - 1) == BaseSwitcher.PRIMARY;
    }

    protected abstract void initStaff();

    protected abstract void start();
}
